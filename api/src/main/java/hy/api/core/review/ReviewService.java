package hy.api.core.review;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/review")
public interface ReviewService {

  /**
   * Creates a review for a product.
   *
   * @param review the review to be created
   * @return the created review
   */
  @PostMapping(consumes = "application/json", produces = "application/json")
  Mono<Review> createReview(@RequestBody Review body);

  /**
   * Gets all reviews for a product.
   *
   * @param productId the ID of the product
   * @return a list of reviews for the product
   */
  @GetMapping(produces = "application/json")
  Flux<Review> getReviews(@RequestParam(value = "productId", required = true) int productId);

  /**
   * Deletes a review for a product.
   *
   * @param productId the ID of the product
   */
  @DeleteMapping
  Mono<Void> deleteReviews(@RequestParam(value = "productId", required = true) int productId);
}
