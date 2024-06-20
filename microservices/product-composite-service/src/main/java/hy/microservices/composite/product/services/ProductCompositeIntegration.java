package hy.microservices.composite.product.services;

import static java.util.logging.Level.FINE;
import static reactor.core.publisher.Flux.empty;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.databind.ObjectMapper;

import hy.api.core.product.Product;
import hy.api.core.product.ProductService;
import hy.api.core.recommendation.Recommendation;
import hy.api.core.recommendation.RecommendationService;
import hy.api.core.review.Review;
import hy.api.core.review.ReviewService;
import hy.api.event.Event;
import hy.api.exceptions.InvalidInputException;
import hy.api.exceptions.NotFoundException;
import hy.util.http.HttpErrorInfo;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Component
@Slf4j
public class ProductCompositeIntegration implements ProductService, RecommendationService, ReviewService {
  private static final String HTTP_PROTOCOL = "http://";

  private final WebClient webClient;
  private final ObjectMapper mapper;

  private final String productServiceUrl;
  private final String recommendationServiceUrl;
  private final String reviewServiceUrl;

  private final StreamBridge streamBridge;
  private final Scheduler publishScheduler;

  private static final String CLASS_NAME = ProductCompositeIntegration.class.getSimpleName();

  public ProductCompositeIntegration(ObjectMapper mapper, WebClient.Builder webClient,
    StreamBridge streamBridge, @Qualifier("publishEventScheduler") Scheduler scheduler,
    @Value("${app.product-service.host}") String productServiceHost,
    @Value("${app.product-service.port}") int productServicePort,
    @Value("${app.recommendation-service.host}") String recommendationServiceHost,
    @Value("${app.recommendation-service.port}") int recommendationServicePort,
    @Value("${app.review-service.host}") String reviewServiceHost,
    @Value("${app.review-service.port}") int reviewServicePort) {

    this.webClient = webClient.build();
    this.mapper = mapper;
    this.streamBridge = streamBridge;
    this.publishScheduler = scheduler;

    this.productServiceUrl = HTTP_PROTOCOL + productServiceHost + ":" + productServicePort;
    this.recommendationServiceUrl = HTTP_PROTOCOL + recommendationServiceHost + ":" + recommendationServicePort;
    this.reviewServiceUrl = HTTP_PROTOCOL + reviewServiceHost + ":" + reviewServicePort;
  }

  @Override
  public Mono<Product> createProduct(Product body) {
    log.debug("[POST] product id: {} @{}", body.getProductId(), productServiceUrl);
    return Mono
      .fromCallable(() -> {
        sendMessage("products-out-0", new Event<>(Event.Type.CREATE, body.getProductId(), body));
        return body;
      })
      .subscribeOn(publishScheduler);
  }

  @Override
  public Mono<Product> getProduct(int productId) {
    String url = productServiceUrl + "/product/" + productId;
    log.debug("[GET] product id:{} @{}", productId, url);
    return webClient.get()
      .uri(url)
      .retrieve()
      .bodyToMono(Product.class)
      .log(log.getName(), FINE)
      .onErrorMap(WebClientException.class, this::handleException);
  }

  @Override
  public Mono<Void> deleteProduct(int productId) {
    return Mono
      .fromRunnable(() -> sendMessage("products-out-0", new Event<Integer, Void>(Event.Type.DELETE, productId, null)))
      .subscribeOn(publishScheduler)
      .then();
  }

  @Override
  public Mono<Recommendation> createRecommendation(Recommendation body) {
    log.debug("[POST] recommendation id: {} @{}", body.getProductId(), recommendationServiceUrl);
    return Mono
      .fromCallable(() -> {
        sendMessage("recommendations-out-0", new Event<Integer, Recommendation>(Event.Type.CREATE, body.getProductId(), body));
        return body;
      })
      .subscribeOn(publishScheduler);
  }

  @Override
  public Flux<Recommendation> getRecommendations(int productId) {
    String url = recommendationServiceUrl + "/recommendation?productId=" + productId;
    log.debug("[GET] recommendations id:{}, @{}", productId, url);
    return webClient.get()
      .uri(url)
      .retrieve()
      .bodyToFlux(Recommendation.class)
      .log(log.getName(), FINE)
      .onErrorResume(err -> empty());
  }

  @Override
  public Mono<Void> deleteRecommendations(int productId) {
    log.debug("[DELETE] recommendations id:{}, @{}", productId, recommendationServiceUrl);
    return Mono
      .fromRunnable(() -> sendMessage("recommendations-out-0", new Event<Integer, Void>(Event.Type.DELETE, productId, null)))
      .subscribeOn(publishScheduler)
      .then();
  }

  @Override
  public Mono<Review> createReview(Review body) {
    log.debug("[POST] review id: {} @{}", body.getProductId(), reviewServiceUrl);
    return Mono
      .fromCallable(() -> {
        sendMessage("reviews-out-0", new Event<Integer, Review>(Event.Type.CREATE, body.getProductId(), body));
        return body;
      })
      .subscribeOn(publishScheduler);
  }

  @Override
  public Flux<Review> getReviews(int productId) {
    String url = reviewServiceUrl + "/review?productId=" + productId;
    log.debug("[GET] reviews id:{}, @{}", productId, url);
    return webClient.get()
      .uri(url)
      .retrieve()
      .bodyToFlux(Review.class)
      .log(log.getName(), FINE)
      .onErrorResume(err -> empty());
  }

  @Override
  public Mono<Void> deleteReviews(int productId) {
    log.debug("[DELETE] reviews id:{}, @{}", productId, reviewServiceUrl);
    return Mono
      .fromRunnable(() -> sendMessage("reviews-out-0", new Event<Integer, Void>(Event.Type.DELETE, productId, null)))
      .subscribeOn(publishScheduler)
      .then();
  }

  public Mono<Health> getProductHealth() {
    return getHealth(productServiceUrl);
  }

  public Mono<Health> getRecommendationHealth() {
    return getHealth(recommendationServiceUrl);
  }

  public Mono<Health> getReviewHealth() {
    return getHealth(reviewServiceUrl);
  }

  private Mono<Health> getHealth(String url) {
    url += "/actuator/health";
    log.debug("{}::getHealth() check health on URL: {}", CLASS_NAME, url);
    return webClient.get()
      .uri(url)
      .retrieve()
      .bodyToMono(String.class)
      .map(s -> new Health.Builder().up().build())
      .onErrorResume(ex -> Mono.just(new Health.Builder().down(ex).build()))
      .log(log.getName(), FINE);
  }

  private <T> void sendMessage(String bindingName, Event<Integer, T> event) {
    log.debug("{} Sending a {} message to {}", getClass().getSimpleName(), event.getType(), bindingName);
    Message<Event<Integer, T>> message = MessageBuilder.withPayload(event)
      .setHeader("partitionKey", event.getKey())
      .build();
    streamBridge.send(bindingName, message);
  }

  private Throwable handleException(Throwable ex) {

    if (!(ex instanceof WebClientResponseException)) {
      log.warn("Got a unexpected error: {}, will rethrow it", ex.toString());
      return ex;
    }

    WebClientResponseException wcre = (WebClientResponseException) ex;

    switch (HttpStatus.resolve(wcre.getStatusCode()
      .value())) {

      case NOT_FOUND:
        return new NotFoundException(getErrorMessage(wcre));

      case UNPROCESSABLE_ENTITY:
        return new InvalidInputException(getErrorMessage(wcre));

      default:
        log.warn("Got an unexpected HTTP error: {}, will rethrow it", wcre.getStatusCode());
        log.warn("Error body: {}", wcre.getResponseBodyAsString());
        return ex;
    }
  }

  private String getErrorMessage(WebClientResponseException ex) {
    try {
      return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class)
        .getMessage();
    } catch (IOException ioex) {
      return ex.getMessage();
    }
  }
}
