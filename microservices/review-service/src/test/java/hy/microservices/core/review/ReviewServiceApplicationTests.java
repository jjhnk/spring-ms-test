package hy.microservices.core.review;

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
import hy.api.core.review.Review;
import hy.microservices.core.review.persistence.ReviewRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ReviewServiceApplicationTests extends MySqlTestBase {

	@Autowired
	private WebTestClient client;

	@Autowired
	private ReviewRepository repository;

	@BeforeEach
	void setupDb() {
		repository.deleteAll();
	}

	@Test
	void getReviewsByProductId() {
		int productId = 1;
		assertThat(repository.findByProductId(productId)).isEmpty();

		postAndVerifyReview(productId, 1, HttpStatus.OK);
		postAndVerifyReview(productId, 2, HttpStatus.OK);
		postAndVerifyReview(productId, 3, HttpStatus.OK);
		assertThat(repository.findByProductId(productId)).hasSize(3);

		getAndVerifyReviewsByProductId(productId, HttpStatus.OK)
			.jsonPath("$.length()").isEqualTo(3)
			.jsonPath("$[2].productId").isEqualTo(productId)
			.jsonPath("$[2].reviewId").isEqualTo(3);
	}

	@Test
	void duplicateError() {
		int productId = 1;
		int reviewId = 1;
		assertThat(repository.count()).isZero();

		postAndVerifyReview(productId, reviewId, HttpStatus.OK)
			.jsonPath("$.productId").isEqualTo(productId)
			.jsonPath("$.reviewId").isEqualTo(reviewId);
		assertThat(repository.count()).isOne();

		postAndVerifyReview(productId, reviewId, HttpStatus.UNPROCESSABLE_ENTITY)
			.jsonPath("$.path").isEqualTo("/review")
			.jsonPath("$.message").isEqualTo("Duplicate key, Product Id: 1 Review Id: 1");
		assertThat(repository.count()).isOne();
	}

	@Test
	void deleteReviews() {
		int productId = 1;
		int reviewId = 1;

		postAndVerifyReview(productId, reviewId, HttpStatus.OK);
		assertThat(repository.findByProductId(productId)).hasSize(1);

		deleteAndVerifyReviewsByProductId(productId, HttpStatus.OK);
		assertThat(repository.findByProductId(productId)).isEmpty();

		deleteAndVerifyReviewsByProductId(productId, HttpStatus.OK);
	}

	@Test
	void getReviewsMissingParameter() {
		getAndVerifyReviewsByProductId("", BAD_REQUEST)
			.jsonPath("$.path").isEqualTo("/review")
			.jsonPath("$.message").isEqualTo("Required query parameter 'productId' is not present.");
	}

	@Test
	void getReviewsInvalidParameter() {

		getAndVerifyReviewsByProductId("?productId=no-integer", BAD_REQUEST)
			.jsonPath("$.path").isEqualTo("/review")
			.jsonPath("$.message").isEqualTo("Type mismatch.");
	}

	@Test
	void getReviewsNotFound() {
		getAndVerifyReviewsByProductId("?productId=213", OK)
			.jsonPath("$.length()").isEqualTo(0);
	}

	private WebTestClient.BodyContentSpec getAndVerifyReviewsByProductId(int productId, HttpStatus expectedStatus) {
		return getAndVerifyReviewsByProductId("?productId=" + productId, expectedStatus);
	}

	private WebTestClient.BodyContentSpec getAndVerifyReviewsByProductId(String query, HttpStatus expectedStatus) {
		return client.get()
		.uri("/review" + query)
		.accept(APPLICATION_JSON)
		.exchange()
		.expectStatus().isEqualTo(expectedStatus)
		.expectHeader().contentType(APPLICATION_JSON)
		.expectBody();
	}

	private WebTestClient.BodyContentSpec postAndVerifyReview(int productId, int reviewId, HttpStatus expectedStatus) {
		Review review =
			new Review(productId, reviewId, "Author" + reviewId, "Subject" + reviewId, "Content" + reviewId, "SA");
		return client.post()
			.uri("/review")
			.body(just(review), Review.class)
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isEqualTo(expectedStatus)
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody();
	}

	private WebTestClient.BodyContentSpec deleteAndVerifyReviewsByProductId(int productId, HttpStatus expectedStatus) {
		return client.delete()
			.uri("/review?productId=" + productId)
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isEqualTo(expectedStatus)
			.expectBody();
	}
}
