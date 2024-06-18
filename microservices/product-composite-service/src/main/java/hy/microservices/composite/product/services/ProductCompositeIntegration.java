package hy.microservices.composite.product.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import hy.api.core.product.Product;
import hy.api.core.product.ProductService;
import hy.api.core.recommendation.Recommendation;
import hy.api.core.recommendation.RecommendationService;
import hy.api.core.review.Review;
import hy.api.core.review.ReviewService;
import hy.api.exceptions.InvalidInputException;
import hy.api.exceptions.NotFoundException;
import hy.util.http.HttpErrorInfo;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProductCompositeIntegration implements ProductService, RecommendationService, ReviewService {
  private static final String HTTP_PROTOCOL = "http://";
  private final RestTemplate restTemplate;
  private final ObjectMapper mapper;

  private final String productServiceUrl;
  private final String recommendationServiceUrl;
  private final String reviewServiceUrl;

  public ProductCompositeIntegration(RestTemplate restTemplate, ObjectMapper mapper,
    @Value("${app.product-service.host}") String productServiceHost,
    @Value("${app.product-service.port}") int productServicePort,
    @Value("${app.recommendation-service.host}") String recommendationServiceHost,
    @Value("${app.recommendation-service.port}") int recommendationServicePort,
    @Value("${app.review-service.host}") String reviewServiceHost,
    @Value("${app.review-service.port}") int reviewServicePort) {
    this.restTemplate = restTemplate;
    this.mapper = mapper;
    this.productServiceUrl = HTTP_PROTOCOL + productServiceHost + ":" + productServicePort + "/product";
    this.recommendationServiceUrl = HTTP_PROTOCOL + recommendationServiceHost + ":" + recommendationServicePort + "/recommendation?productId=";
    this.reviewServiceUrl = HTTP_PROTOCOL + reviewServiceHost + ":" + reviewServicePort + "/review?productId=";
  }

  @Override
  public Product createProduct(Product body) {
    try {
      log.debug("[POST] product id:{}, @{}", body.getProductId(), productServiceUrl);
      return restTemplate.postForObject(productServiceUrl, body, Product.class);
    } catch (HttpClientErrorException e) {
      throw handleHttpClientException(e);
    }
  }

  @Override
  public Product getProduct(int productId) {
    try {
      String url = productServiceUrl + "/" + productId;

      log.debug("[GET] product id:{} @{}", productId, url);
      return restTemplate.getForObject(url, Product.class);
    } catch (HttpClientErrorException e) {
      throw handleHttpClientException(e);
    }
  }

  @Override
  public void deleteProduct(int productId) {
    try {
      String url = productServiceUrl + "/" + productId;

      log.debug("[DELETE] product id:{} @{}", productId, url);
      restTemplate.delete(url);
    } catch (HttpClientErrorException e) {
      throw handleHttpClientException(e);
    }
  }

  @Override
  public Review createReview(Review body) {
    try {
      log.debug("[POST] review id:{}, @{}", body.getProductId(), reviewServiceUrl);
      return restTemplate.postForObject(reviewServiceUrl, body, Review.class);
    } catch (HttpClientErrorException e) {
      throw handleHttpClientException(e);
    }
  }

  @Override
  public List<Review> getReviews(int productId) {
    try {
      String url = reviewServiceUrl + productId;
      List<Review> reviews =
        restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Review>>() {})
          .getBody();

      log.debug("[GET] {} reviews id:{}, @{}", reviews.size(), productId, url);
      return reviews;
    } catch (Exception ex) {
      log.warn("Got an exception while requesting reviews, return zero reviews: {}", ex.getMessage());
      return new ArrayList<>();
    }
  }

  @Override
  public void deleteReviews(int productId) {
    try {
      String url = reviewServiceUrl + productId;

      log.debug("[DELETE] review id:{}, @{}", productId, url);
      restTemplate.delete(url);
    } catch (HttpClientErrorException e) {
      throw handleHttpClientException(e);
    }
  }

  @Override
  public Recommendation createRecommendation(Recommendation body) {
    try {
      log.debug("[POST] recommendation id:{}, @{}", recommendationServiceUrl);
      return restTemplate.postForObject(recommendationServiceUrl, body, Recommendation.class);
    } catch (HttpClientErrorException e) {
      throw handleHttpClientException(e);
    }
  }

  @Override
  public List<Recommendation> getRecommendations(int productId) {
    try {
      String url = recommendationServiceUrl + productId;
      List<Recommendation> recommendations =
        restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Recommendation>>() {})
          .getBody();

      log.debug("[GET] {} recommendations id:{}, @{}", recommendations.size(), productId, url);
      return recommendations;
    } catch (Exception e) {
      log.warn("Got an exception while requesting recommendations: {}", e.getMessage());
      return new ArrayList<>();
    }
  }

  @Override
  public void deleteRecommendations(int productId) {
    try {
      String url = recommendationServiceUrl + productId;
      log.debug("[DELETE] recommendation id:{}, @{}", productId, url);
      restTemplate.delete(url);
    } catch (HttpClientErrorException e) {
      handleHttpClientException(e);
    }
  }

  private RuntimeException handleHttpClientException(HttpClientErrorException e) {
    switch (HttpStatus.resolve(e.getStatusCode()
      .value())) {
      case NOT_FOUND:
        throw new NotFoundException(getErrorMessage(e));

      case UNPROCESSABLE_ENTITY:
        throw new InvalidInputException(getErrorMessage(e));

      default:
        log.warn("Got an unexpected HTTP error: {}, msg: {}", e.getStatusCode(), e.getResponseBodyAsString());
        throw e;
    }
  }

  private String getErrorMessage(HttpClientErrorException ex) {
    try {
      return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class)
        .getMessage();
    } catch (IOException ioex) {
      return ex.getMessage();
    }
  }

}
