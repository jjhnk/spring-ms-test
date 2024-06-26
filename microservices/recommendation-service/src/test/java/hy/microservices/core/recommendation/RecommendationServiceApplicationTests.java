package hy.microservices.core.recommendation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import hy.api.core.recommendation.Recommendation;
import hy.api.event.Event;
import hy.api.exceptions.InvalidInputException;
import hy.microservices.core.recommendation.persistence.RecommendationRepository;
import hy.microservices.core.recommendation.services.MessageProcessorConfig.EventConsumer;

// @formatter:off
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RecommendationServiceApplicationTests extends MongoDbTestBase {

  @Autowired
  private WebTestClient client;

  @Autowired
  private RecommendationRepository repository;

  @Autowired
  @Qualifier("messageProcessor")
  private EventConsumer messageProcessor;

  @BeforeEach
  void setupDb() {
    repository.deleteAll()
      .block();
  }

  @Test
  void getRecommendationsByProductId() {
    int productId = 1;
    assertThat(repository.findByProductId(productId)
      .collectList()
      .block()).isEmpty();

    sendCreateRecommendationEvent(productId, 1);
    sendCreateRecommendationEvent(productId, 2);
    sendCreateRecommendationEvent(productId, 3);
    assertThat(repository.findByProductId(productId)
      .collectList()
      .block()).hasSize(3);

    getAndVerifyRecommendationsByProductId(productId, HttpStatus.OK)
      .jsonPath("$.length()").isEqualTo(3)
      .jsonPath("$[2].productId").isEqualTo(productId)
      .jsonPath("$[2].recommendationId").isEqualTo(3);
  }

  @Test
  void duplicateError() {
    int productId = 1;
    int recommendationId = 1;

    sendCreateRecommendationEvent(productId, recommendationId);
    assertThat(repository.count()
      .block()).isEqualTo(1);

    InvalidInputException thrown =
      assertThrows(InvalidInputException.class, () -> sendCreateRecommendationEvent(productId, recommendationId));
    assertThat(thrown.getMessage()).isEqualTo("Duplicate key, productId: 1, Recommendation Id: 1");
    assertThat(repository.count()
      .block()).isEqualTo(1);
  }

  @Test
  void deleteRecommendations() {
    int productId = 1;
    int recommendationId = 1;

    sendCreateRecommendationEvent(productId, recommendationId);
    assertThat(repository.findByProductId(productId).collectList().block()).hasSize(1);

    sendDeleteRecommendationEvent(productId);
    assertThat(repository.findByProductId(productId).collectList().block()).isEmpty();

    sendDeleteRecommendationEvent(productId);
  }

  @Test
  void getRecommendationsMissingParameter() {
    getAndVerifyRecommendationsByProductId("", HttpStatus.BAD_REQUEST)
      .jsonPath("$.path").isEqualTo("/recommendation")
      .jsonPath("$.message").isEqualTo("Required query parameter 'productId' is not present.");
  }

  @Test
  void getRecommendationsInvalidParameter() {
    getAndVerifyRecommendationsByProductId("?productId=no-integer", HttpStatus.BAD_REQUEST)
      .jsonPath("$.path").isEqualTo("/recommendation")
      .jsonPath("$.message").isEqualTo("Type mismatch.");
  }

  @Test
  void getRecommendationsNotFound() {
    getAndVerifyRecommendationsByProductId("?productId=213", HttpStatus.OK)
      .jsonPath("$.length()").isEqualTo(0);
  }

  private WebTestClient.BodyContentSpec getAndVerifyRecommendationsByProductId(int productId,
    HttpStatus expectedStatus) {
    return getAndVerifyRecommendationsByProductId("?productId=" + productId, expectedStatus);
  }

  private WebTestClient.BodyContentSpec getAndVerifyRecommendationsByProductId(String query,
    HttpStatus expectedStatus) {
    return client.get()
      .uri("/recommendation" + query)
      .accept(APPLICATION_JSON)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus)
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody();
  }

  private void sendCreateRecommendationEvent(int productId, int recommendationId) {
    Recommendation recommendation = new Recommendation(productId, recommendationId, "Author " + recommendationId,
      recommendationId, "Content " + recommendationId, "SA");
    Event<Integer, Recommendation> event = new Event<>(Event.Type.CREATE, productId, recommendation);
    messageProcessor.accept(event);
  }

  private void sendDeleteRecommendationEvent(int productId) {
    Event<Integer, Recommendation> event = new Event<>(Event.Type.DELETE, productId, null);
    messageProcessor.accept(event);
  }
}
// @formatter:on