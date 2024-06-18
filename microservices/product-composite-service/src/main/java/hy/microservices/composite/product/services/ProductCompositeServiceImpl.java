package hy.microservices.composite.product.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.RestController;
import hy.api.composite.product.ProductAggregate;
import hy.api.composite.product.ProductCompositeService;
import hy.api.composite.product.RecommendationSummary;
import hy.api.composite.product.ReviewSummary;
import hy.api.composite.product.ServiceAddresses;
import hy.api.core.product.Product;
import hy.api.core.recommendation.Recommendation;
import hy.api.core.review.Review;
import hy.util.http.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductCompositeServiceImpl implements ProductCompositeService {
  private final ServiceUtil serviceUtil;
  private final ProductCompositeIntegration integration;

  @Override
  public void createProduct(ProductAggregate body) {
    try {
      Product product = new Product(body.getProductId(), body.getName(), body.getWeight(), null);
      integration.createProduct(product);

      if (body.getRecommendations() != null) {
        body.getRecommendations()
          .forEach(r -> {
            Recommendation recommendation = new Recommendation(body.getProductId(), r.getRecommendationId(),
              r.getAuthor(), r.getRate(), r.getContent(), null);
            integration.createRecommendation(recommendation);
          });
      }

      if (body.getReviews() != null) {
        body.getReviews()
          .forEach(r -> {
            Review review =
              new Review(body.getProductId(), r.getReviewId(), r.getAuthor(), r.getSubject(), r.getContent(), null);
            integration.createReview(review);
          });
      }
    } catch (RuntimeException re) {
      log.warn("Failed to create aggregate", re);
      throw new RuntimeException(re);
    }
  }

  @Override
  public ProductAggregate getProduct(int productId) {
    Product product = integration.getProduct(productId);
    List<Recommendation> recommendations = integration.getRecommendations(productId);
    List<Review> reviews = integration.getReviews(productId);
    return createProductAggregate(product, recommendations, reviews, serviceUtil.getServiceAddress());
  }

  @Override
  public void deleteProduct(int productId) {
    integration.deleteProduct(productId);
    integration.deleteRecommendations(productId);
    integration.deleteReviews(productId);
  }

  private ProductAggregate createProductAggregate(Product product, List<Recommendation> recommendations,
    List<Review> reviews, String serviceAddress) {

    int productId = product.getProductId();
    String name = product.getName();
    int weight = product.getWeight();

    List<RecommendationSummary> recommendationSummaries = (recommendations == null)
      ? null
      : recommendations.stream()
        .map(r -> new RecommendationSummary(r.getRecommendationId(), r.getAuthor(), r.getRate(), r.getContent()))
        .collect(Collectors.toList());

    List<ReviewSummary> reviewSummaries = (reviews == null)
      ? null
      : reviews.stream().map(r -> new ReviewSummary(r.getReviewId(), r.getAuthor(), r.getSubject(), r.getContent()))
        .collect(Collectors.toList());

    String productAddress = product.getServiceAddress();
    String reviewAddress = (reviews != null && !reviews.isEmpty())
      ? reviews.get(0).getServiceAddress()
      : "";
    String recommendationAddress = (recommendations != null && !recommendations.isEmpty())
      ? recommendations.get(0).getServiceAddress()
      : "";

    ServiceAddresses serviceAddresses =
      new ServiceAddresses(serviceAddress, productAddress, reviewAddress, recommendationAddress);

    return new ProductAggregate(productId, name, weight, recommendationSummaries, reviewSummaries, serviceAddresses);
  }

}
