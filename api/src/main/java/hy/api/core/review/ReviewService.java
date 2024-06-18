package hy.api.core.review;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/review")
public interface ReviewService {

  /**
   * Creates a review for a product.
   *
   * @param review the review to be created
   * @return the created review
   */
  @PostMapping(consumes = "application/json", produces = "application/json")
  Review createReview(@RequestBody Review body);

  /**
   * Gets all reviews for a product.
   *
   * @param productId the ID of the product
   * @return a list of reviews for the product
   */
  @GetMapping(produces = "application/json")
  List<Review> getReviews(@RequestParam(value = "productId", required = true) int productId);

  /**
   * Deletes a review for a product.
   *
   * @param productId the ID of the product
   */
  @DeleteMapping
  void deleteReviews(@RequestParam(value = "productId", required = true) int productId);
}
