package hy.api.core.recommendation;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The RecommendationService interface defines the REST API for managing recommendations.
 *
 * @see Recommendation
 */
@RequestMapping("/recommendation")
public interface RecommendationService {

  /**
   * Creates a new recommendation.
   *
   * @param body the recommendation to be created
   * @return the created recommendation
   */
  @PostMapping(consumes = "application/json", produces = "application/json")
  Mono<Recommendation> createRecommendation(@RequestBody Recommendation body);

  /**
   * Creates a new recommendation.
   *
   * @param body the recommendation to be created
   * @return the created recommendation
   */
  @GetMapping(produces = "application/json")
  Flux<Recommendation> getRecommendations(
    @RequestHeader HttpHeaders headers,
    @RequestParam(value = "productId", required = true) int productId);

  /**
   * Deletes a recommendation by product ID.
   *
   * @param productId the ID of the product to delete recommendations for
   */
  @DeleteMapping
  Mono<Void> deleteRecommendations(@RequestParam(value = "productId", required = true) int productId);
}
