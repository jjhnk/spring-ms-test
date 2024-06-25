package hy.microservices.core.review;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import hy.api.core.review.Review;
import hy.api.event.Event;
import hy.api.exceptions.InvalidInputException;
import hy.microservices.core.review.persistence.ReviewRepository;
import hy.microservices.core.review.services.MessageProcessorConfig.EventConsumer;

// @formatter:off
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = {
		"logging.level.hy=DEBUG",
		"spring.cloud.config.enabled=false",
		"spring.jpa.hibernate.ddl-auto=update",
		"spring.cloud.stream.defaultBinder=rabbit"})
// @formatter:on
class ReviewServiceApplicationTests extends MySqlTestBase {

	@Autowired
	private WebTestClient client;

	@Autowired
	private ReviewRepository repository;

	@Autowired
	@Qualifier("messageProcessor")
	private EventConsumer messageProcessor;

	@BeforeEach
	void setupDb() {
		repository.deleteAll();
	}

	@Test
	void getReviewsByProductId() {
		int productId = 1;
		assertThat(repository.findByProductId(productId)).isEmpty();

		sendCreateReviewEvent(productId, 1);
		sendCreateReviewEvent(productId, 2);
		sendCreateReviewEvent(productId, 3);
		assertThat(repository.findByProductId(productId)).hasSize(3);

		// @formatter:off
		getAndVerifyReviewsByProductId(productId, HttpStatus.OK)
			.jsonPath("$.length()").isEqualTo(3)
			.jsonPath("$[2].productId").isEqualTo(productId)
			.jsonPath("$[2].reviewId").isEqualTo(3);
		// @formatter:on
	}

	@Test
	void duplicateError() {
		int productId = 1;
		int reviewId = 1;
		assertThat(repository.count()).isZero();

		sendCreateReviewEvent(productId, reviewId);
		assertThat(repository.count()).isOne();

		InvalidInputException thrown =
			assertThrows(InvalidInputException.class, () -> sendCreateReviewEvent(productId, reviewId));
		assertThat(thrown.getMessage()).contains("Duplicate key, productId: 1 Review Id: 1");
		assertThat(repository.count()).isOne();
	}

	@Test
	void deleteReviews() {
		int productId = 1;
		int reviewId = 1;

		sendCreateReviewEvent(productId, reviewId);
		assertThat(repository.findByProductId(productId)).hasSize(1);

		sendDeleteReviewEvent(productId);
		assertThat(repository.findByProductId(productId)).isEmpty();

		sendDeleteReviewEvent(productId);
	}

	@Test
	void getReviewsMissingParameter() {
		// @formatter:off
		getAndVerifyReviewsByProductId("", BAD_REQUEST)
			.jsonPath("$.path").isEqualTo("/review")
			.jsonPath("$.message").isEqualTo("Required query parameter 'productId' is not present.");
		// @formatter:on
	}

	@Test
	void getReviewsInvalidParameter() {
		getAndVerifyReviewsByProductId("?productId=no-integer", BAD_REQUEST)
		// @formatter:off
			.jsonPath("$.path").isEqualTo("/review")
			.jsonPath("$.message").isEqualTo("Type mismatch.");
		// @formatter:on
	}

	@Test
	void getReviewsNotFound() {
		// @formatter:off
		getAndVerifyReviewsByProductId("?productId=213", OK)
			.jsonPath("$.length()").isEqualTo(0);
		// @formatter:on
	}

	private WebTestClient.BodyContentSpec getAndVerifyReviewsByProductId(int productId, HttpStatus expectedStatus) {
		return getAndVerifyReviewsByProductId("?productId=" + productId, expectedStatus);
	}

	private WebTestClient.BodyContentSpec getAndVerifyReviewsByProductId(String query, HttpStatus expectedStatus) {
		// @formatter:off
		return client.get()
			.uri("/review" + query)
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isEqualTo(expectedStatus)
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody();
			// @formatter:on
	}

	private void sendCreateReviewEvent(int productId, int reviewId) {
		Review review =
			new Review(productId, reviewId, "Author " + reviewId, "Subject " + reviewId, "Content " + reviewId, "SA");
		Event<Integer, Review> event = new Event<>(Event.Type.CREATE, productId, review);
		messageProcessor.accept(event);
	}

	private void sendDeleteReviewEvent(int productId) {
		Event<Integer, Review> event = new Event<>(Event.Type.DELETE, productId, null);
		messageProcessor.accept(event);
	}
}
