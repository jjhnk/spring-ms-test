package hy.microservices.core.product;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static reactor.core.publisher.Mono.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import hy.api.core.product.Product;
import hy.microservices.core.product.persistence.ProductRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests extends MongoDbTestBase {

  @Autowired
  private WebTestClient client;

  @Autowired
  private ProductRepository repository;

  @BeforeEach
  public void setupDb() {
    repository.deleteAll();
  }

  @Test
  void getProductById() {
    int productId = 1;

    postAndVerifyProduct(productId, OK);
    assertThat(repository.findByProductId(productId)).isPresent();

    getAndVerifyProduct(productId, OK)
      .jsonPath("$.productId").isEqualTo(productId);
  }

  @Test
  void duplicateError() {
    int productId = 1;

    postAndVerifyProduct(productId, OK).jsonPath("$.productId")
      .isEqualTo(productId);
    assertThat(repository.findByProductId(productId)).isPresent();

    postAndVerifyProduct(productId, UNPROCESSABLE_ENTITY)
      .jsonPath("$.path").isEqualTo("/product")
      .jsonPath("$.message").isEqualTo("Duplicate key, Product Id: " + productId);
  }

  @Test
  void deleteProduct() {
    int productId = 1;
    postAndVerifyProduct(productId, OK);
    assertThat(repository.findByProductId(productId)).isPresent();

    deleteAndVerifyProduct(productId, OK);
    assertThat(repository.findByProductId(productId)).isEmpty();

    deleteAndVerifyProduct(productId, OK);
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

  private WebTestClient.BodyContentSpec postAndVerifyProduct(int productId, HttpStatus expectedStatus) {
    Product product = new Product(productId, "name" + productId, 1, "adr");

    return client.post()
      .uri("/product")
      .body(just(product), Product.class)
      .accept(APPLICATION_JSON)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus)
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody();
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

  private WebTestClient.BodyContentSpec deleteAndVerifyProduct(int productId, HttpStatus expectedStatus) {
    return client.delete()
      .uri("/product/" + productId)
      .accept(APPLICATION_JSON)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus)
      .expectBody();
  }

}
