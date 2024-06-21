package hy.microservices.core.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import hy.api.core.product.Product;
import hy.api.event.Event;
import hy.api.exceptions.InvalidInputException;
import hy.microservices.core.product.persistence.ProductRepository;
import hy.microservices.core.product.services.MessageProcessorConfig.EventConsumer;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = {"eureka.client.enabled=false"})
class ProductServiceApplicationTests extends MongoDbTestBase {

  @Autowired
  private WebTestClient client;

  @Autowired
  private ProductRepository repository;

  @Autowired
  @Qualifier("messageProcessor")
  private EventConsumer messageProcessor;

  @BeforeEach
  public void setupDb() {
    repository.deleteAll().block();
  }

  @Test
  void getProductById() {
    int productId = 1;
    assertThat(repository.findByProductId(productId).block()).isNull();
    assertThat(repository.count().block()).isZero();

    sendCreateProductEvent(productId);
    assertThat(repository.findByProductId(productId).block()).isNotNull();
    assertThat(repository.count().block()).isOne();

    getAndVerifyProduct(productId, OK)
      .jsonPath("$.productId").isEqualTo(productId);
  }

  @Test
  void duplicateError() {
    int productId = 1;
    assertThat(repository.findByProductId(productId).block()).isNull();

    sendCreateProductEvent(productId);
    assertThat(repository.findByProductId(productId).block()).isNotNull();

    InvalidInputException thrown = assertThrows(InvalidInputException.class, () -> sendCreateProductEvent(productId));
    assertEquals("Duplicate key, productId: " + productId, thrown.getMessage());
  }

  @Test
  void deleteProduct() {
    int productId = 1;

    sendCreateProductEvent(productId);
    assertThat(repository.findByProductId(productId).block()).isNotNull();

    sendDeleteProductEvent(productId);
    assertThat(repository.findByProductId(productId).block()).isNull();

    sendDeleteProductEvent(productId);
  }

  @Test
  void getProductInvalidParameterString() {
    getAndVerifyProduct("/invalid", BAD_REQUEST)
      .jsonPath("$.path").isEqualTo("/product/invalid")
      .jsonPath("$.message").isEqualTo("Type mismatch.");
  }

  @Test
  void getProductNotFound() {
    int productIdNotFound = 13;

    getAndVerifyProduct(productIdNotFound, NOT_FOUND)
      .jsonPath("$.path").isEqualTo("/product/" + productIdNotFound)
      .jsonPath("$.message").isEqualTo("No product found for productId: " + productIdNotFound);
  }

  @Test
  void getProductInvalidParameterNegativeValue() {
    int productInvalid = -1;

    getAndVerifyProduct(productInvalid, UNPROCESSABLE_ENTITY)
      .jsonPath("$.path").isEqualTo("/product/" + productInvalid)
      .jsonPath("$.message").isEqualTo("Invalid productId: " + productInvalid);
  }

  private WebTestClient.BodyContentSpec getAndVerifyProduct(int productId, HttpStatus expectedStatus) {
    return getAndVerifyProduct("/" + productId, expectedStatus);
  }

  private WebTestClient.BodyContentSpec getAndVerifyProduct(String productIdPath, HttpStatus expectedStatus) {
    return client.get()
      .uri("/product" + productIdPath)
      .accept(APPLICATION_JSON)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus)
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody();
  }

  private void sendCreateProductEvent(int productId) {
    Product product = new Product(productId, "name" + productId, productId, "SA");
    Event<Integer, Product> event = new Event<>(Event.Type.CREATE, productId, product);
    messageProcessor.accept(event);
  }

  private void sendDeleteProductEvent(int productId) {
    Event<Integer, Product> event = new Event<>(Event.Type.DELETE, productId, null);
    messageProcessor.accept(event);
  }
}
