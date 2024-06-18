package hy.microservices.core.recommendation;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static reactor.core.publisher.Mono.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import hy.api.core.recommendation.Recommendation;
import hy.microservices.core.recommendation.persistence.RecommendationRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RecommendationServiceApplicationTests extends MongoDbTestBase {

  @Autowired
  private WebTestClient client;

  @Autowired
  private RecommendationRepository repository;

  @BeforeEach
  void setupDb() {
    repository.deleteAll();
  }

  @Test
  void getRecommendationsByProductId() {
    int productId = 1;
    assertThat(repository.findByProductId(productId)).isEmpty();

    postAndVerifyRecommendation(productId, 1, HttpStatus.OK);
    postAndVerifyRecommendation(productId, 2, HttpStatus.OK);
    postAndVerifyRecommendation(productId, 3, HttpStatus.OK);
    assertThat(repository.findByProductId(productId)).hasSize(3);

    getAndVerifyRecommendationsByProductId(productId, HttpStatus.OK)
      .jsonPath("$.length()").isEqualTo(3)
      .jsonPath("$[2].productId").isEqualTo(productId)
      .jsonPath("$[2].recommendationId").isEqualTo(3);
  }

  @Test
  void duplicateError() {
    int productId = 1;
    int recommendationId = 1;

    postAndVerifyRecommendation(productId, recommendationId, HttpStatus.OK)
      .jsonPath("$.productId").isEqualTo(productId)
      .jsonPath("$.recommendationId").isEqualTo(recommendationId);
    assertThat(repository.count()).isEqualTo(1);

    postAndVerifyRecommendation(productId, recommendationId, HttpStatus.UNPROCESSABLE_ENTITY)
      .jsonPath("$.path").isEqualTo("/recommendation")
      .jsonPath("$.message").isEqualTo("Duplicate key, ProductId: 1, RecommendationId: 1");
    assertThat(repository.count()).isEqualTo(1);
  }

  @Test
  void deleteRecommendations() {
    int productId = 1;
    int recommendationId = 1;

    postAndVerifyRecommendation(productId, recommendationId, HttpStatus.OK);
    assertThat(repository.findByProductId(productId)).hasSize(1);

    deleteAndVerifyRecommendationsByProductId(productId, HttpStatus.OK);
    assertThat(repository.findByProductId(productId)).isEmpty();

    deleteAndVerifyRecommendationsByProductId(productId, HttpStatus.OK);
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

  private WebTestClient.BodyContentSpec postAndVerifyRecommendation(int productId, int recommendationId,
    HttpStatus expectedStatus) {
    Recommendation recommendation = new Recommendation(productId, recommendationId, "author", 1, "content", null);
    return client.post()
      .uri("/recommendation")
      .body(just(recommendation), Recommendation.class)
      .accept(APPLICATION_JSON)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus)
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody();
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

  private WebTestClient.BodyContentSpec deleteAndVerifyRecommendationsByProductId(int productId,
    HttpStatus expectedStatus) {
    return client.delete()
      .uri("/recommendation?productId=" + productId)
      .accept(APPLICATION_JSON)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus)
      .expectBody();
  }
}
