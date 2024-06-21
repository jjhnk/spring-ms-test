package hy.microservices.composite.product;

import static hy.api.event.Event.Type.CREATE;
import static hy.api.event.Event.Type.DELETE;
import static hy.microservices.composite.product.EventAssert.assertThat;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static reactor.core.publisher.Mono.just;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.test.web.reactive.server.WebTestClient;

import hy.api.composite.product.ProductAggregate;
import hy.api.composite.product.RecommendationSummary;
import hy.api.composite.product.ReviewSummary;
import hy.api.core.product.Product;
import hy.api.core.recommendation.Recommendation;
import hy.api.core.review.Review;
import hy.api.event.Event;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {TestSecurityConfig.class},
  properties = {"spring.security.oauth2.resourceserver.jwt.issuer-uri=", "eureka.client.enabled=false",
      "spring.main.allow-bean-definition-overriding=true"})
@Import({TestChannelBinderConfiguration.class})
@Slf4j
class MessagingTests {

  @Autowired
  private WebTestClient client;

  @Autowired
  private OutputDestination target;

  @BeforeEach
  void setUp() {
    purgeMessages("products");
    purgeMessages("reviews");
    purgeMessages("recommendations");
  }

  @Test
  void createCompositeProduct1() {
    ProductAggregate composite = new ProductAggregate(1, "name", 1, null, null, null);
    postAndVerifyProduct(composite, ACCEPTED);

    final List<String> productMessages = getMessages("products");
    final List<String> recommendationMessages = getMessages("recommendations");
    final List<String> reviewMessages = getMessages("reviews");

    assertThat(productMessages).hasSize(1);
    Event<Integer, Product> expectedEvent = new Event<>(CREATE, composite.getProductId(),
      new Product(composite.getProductId(), composite.getName(), composite.getWeight(), null));

    assertThat(expectedEvent).isMatch(productMessages.get(0));
    assertThat(recommendationMessages).isEmpty();
    assertThat(reviewMessages).isEmpty();
  }

  @Test
  void createCompositeProduct2() {
    ProductAggregate composite =
      new ProductAggregate(1, "name", 1, singletonList(new RecommendationSummary(1, "a", 1, "c")),
        singletonList(new ReviewSummary(1, "a", "s", "c")), null);
    postAndVerifyProduct(composite, ACCEPTED);

    final List<String> productMessages = getMessages("products");
    final List<String> recommendationMessages = getMessages("recommendations");
    final List<String> reviewMessages = getMessages("reviews");

    assertThat(productMessages).hasSize(1);
    Event<Integer, Product> expectedProductEvent = new Event<>(CREATE, composite.getProductId(),
      new Product(composite.getProductId(), composite.getName(), composite.getWeight(), null));
    assertThat(expectedProductEvent).isMatch(productMessages.get(0));

    assertThat(recommendationMessages).hasSize(1);
    RecommendationSummary rec = composite.getRecommendations().get(0);
    Event<Integer, Recommendation> expectedRecommendationEvent =
      new Event<>(CREATE, composite.getProductId(), new Recommendation(composite.getProductId(),
        rec.getRecommendationId(), rec.getAuthor(), rec.getRate(), rec.getContent(), null));
    assertThat(expectedRecommendationEvent).isMatch(recommendationMessages.get(0));

    assertThat(reviewMessages).hasSize(1);
    ReviewSummary rev = composite.getReviews().get(0);
    Event<Integer, Review> expectedReviewEvent =
      new Event<>(CREATE, composite.getProductId(), new Review(composite.getProductId(), rev.getReviewId(),
        rev.getAuthor(), rev.getSubject(), rev.getContent(), null));
    assertThat(expectedReviewEvent).isMatch(reviewMessages.get(0));
  }

  @Test
  void deleteCompositeProduct() {
    deleteAndVerifyProduct(1, HttpStatus.ACCEPTED);

    final List<String> productMessages = getMessages("products");
    final List<String> recommendationMessages = getMessages("recommendations");
    final List<String> reviewMessages = getMessages("reviews");

    assertThat(productMessages).hasSize(1);
    Event<Integer, Product> expectedProductEvent = new Event<>(DELETE, 1, null);
    assertThat(expectedProductEvent).isMatch(productMessages.get(0));

    assertThat(recommendationMessages).hasSize(1);
    Event<Integer, Recommendation> expectedRecommendationEvent = new Event<>(DELETE, 1, null);
    assertThat(expectedRecommendationEvent).isMatch(recommendationMessages.get(0));

    assertThat(reviewMessages).hasSize(1);
    Event<Integer, Review> expectedReviewEvent = new Event<>(DELETE, 1, null);
    assertThat(expectedReviewEvent).isMatch(reviewMessages.get(0));
  }

  private void purgeMessages(String bindingName) {
    getMessages(bindingName);
  }

  private List<String> getMessages(String bindingName) {
    List<String> messages = new ArrayList<>();
    boolean anyMoreMessages = true;

    while (anyMoreMessages) {
      Message<byte[]> message = getMessage(bindingName);
      log.info("Received message: {}", message);

      if (message == null) {
        anyMoreMessages = false;

      } else {
        messages.add(new String(message.getPayload()));
        log.info("Received message: {}", new String(message.getPayload()));
      }
    }
    return messages;
  }

  private Message<byte[]> getMessage(String bindingName) {
    try {
      return target.receive(0, bindingName);
    } catch (NullPointerException npe) {
      return null;
    }
  }

  private void postAndVerifyProduct(ProductAggregate compositeProduct, HttpStatus expectedStatus) {
    client.post()
      .uri("/product-composite")
      .body(just(compositeProduct), ProductAggregate.class)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus);
  }

  private void deleteAndVerifyProduct(int productId, HttpStatus expectedStatus) {
    client.delete()
      .uri("/product-composite/" + productId)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus);
  }
}
