package hy.microservices.composite.product.services;

import static java.util.logging.Level.FINE;
import static reactor.core.publisher.Flux.empty;

import java.io.IOException;
import java.net.URI;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

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
import hy.util.http.ServiceUtil;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Component
@Slf4j
public class ProductCompositeIntegration implements ProductService, RecommendationService, ReviewService {
  private static final String HTTP_PROTOCOL = "http://";
  private static final String PRODUCT_SERVICE_URL = HTTP_PROTOCOL + "product";
  private static final String RECOMMENDATION_SERVICE_URL = HTTP_PROTOCOL + "recommendation";
  private static final String REVIEW_SERVICE_URL = HTTP_PROTOCOL + "review";

  private final WebClient webClient;
  private final ObjectMapper mapper;
  private final StreamBridge streamBridge;
  private final Scheduler publishScheduler;
  private final ServiceUtil serviceUtil;


  public ProductCompositeIntegration(ObjectMapper mapper, WebClient webClient, StreamBridge streamBridge,
    @Qualifier("publishEventScheduler") Scheduler scheduler, ServiceUtil serviceUtil) {

    this.webClient = webClient;
    this.mapper = mapper;
    this.streamBridge = streamBridge;
    this.publishScheduler = scheduler;
    this.serviceUtil = serviceUtil;
  }

  // @formatter:off
  @Override
  public Mono<Product> createProduct(Product body) {
    log.debug("[POST] product id: {} @{}", body.getProductId(), PRODUCT_SERVICE_URL);
    return Mono.fromCallable(() -> {
        sendMessage("products-out-0", new Event<>(Event.Type.CREATE, body.getProductId(), body));
        return body;
      })
      .subscribeOn(publishScheduler);
  }
  // @formatter:on

  @Override
  @Retry(name = "product")
  @TimeLimiter(name = "product")
  @CircuitBreaker(name = "product", fallbackMethod = "getProductFallbackValue")
  public Mono<Product> getProduct(int productId, int delay, int faultPercent) {
    URI uri = UriComponentsBuilder.fromUriString(PRODUCT_SERVICE_URL + "/product/{productId}")
      .queryParam("delay", delay)
      .queryParam("faultPercent", faultPercent)
      .build(productId);

    log.debug("[GET] product id:{} @{}", productId, uri);
    return webClient.get()
      .uri(uri)
      .retrieve()
      .bodyToMono(Product.class)
      .log(log.getName(), FINE)
      .onErrorMap(WebClientException.class, this::handleException);
  }

  @SuppressWarnings("unused")
  // private method will remove fields
  Mono<Product> getProductFallbackValue(int productId, int delay, int faultPercent,
    CallNotPermittedException ex) {
    log.warn(
      "Creating a fail-fast fallback product for productId = {}, delay = {}, faultPercent = {} and exception = {} ",
      productId,
      delay,
      faultPercent,
      ex.toString());

    if (productId == 13) {
      String errMsg = "Product Id: " + productId + " not found in fallback cache!";
      log.warn(errMsg);
      throw new NotFoundException(errMsg);
    }

    return Mono.just(
      new Product(productId, "Fallback product" + productId, productId, serviceUtil.getServiceAddress()));
  }

  @Override
  public Mono<Void> deleteProduct(int productId) {
    return Mono
      .fromRunnable(() -> sendMessage("products-out-0", new Event<Integer, Void>(Event.Type.DELETE, productId, null)))
      .subscribeOn(publishScheduler)
      .then();
  }

  // @formatter:off
  @Override
  public Mono<Recommendation> createRecommendation(Recommendation body) {
    log.debug("[POST] recommendation id: {} @{}", body.getProductId(), RECOMMENDATION_SERVICE_URL);
    return Mono.fromCallable(() -> {
        sendMessage("recommendations-out-0",
        new Event<Integer, Recommendation>(Event.Type.CREATE, body.getProductId(), body));
        return body;
      })
      .subscribeOn(publishScheduler);
  }
  // @formatter:on

  @Override
  public Flux<Recommendation> getRecommendations(int productId) {
    URI url = UriComponentsBuilder.fromUriString(RECOMMENDATION_SERVICE_URL + "/recommendation")
      .queryParam("productId", productId)
      .build()
      .toUri();

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
    log.debug("[DELETE] recommendations id:{}, @{}", productId, RECOMMENDATION_SERVICE_URL);
    return Mono
      .fromRunnable(
        () -> sendMessage("recommendations-out-0", new Event<Integer, Void>(Event.Type.DELETE, productId, null)))
      .subscribeOn(publishScheduler)
      .then();
  }

  // @formatter:off
  @Override
  public Mono<Review> createReview(Review body) {
    log.debug("[POST] review id: {} @{}", body.getProductId(), REVIEW_SERVICE_URL);
    return Mono
      .fromCallable(() -> {
        sendMessage("reviews-out-0", new Event<Integer, Review>(Event.Type.CREATE, body.getProductId(), body));
        return body;
      })
      .subscribeOn(publishScheduler);
  }
  // @formatter:on

  @Override
  public Flux<Review> getReviews(int productId) {
    URI url = UriComponentsBuilder.fromUriString(REVIEW_SERVICE_URL + "/review")
      .queryParam("productId", productId)
      .build()
      .toUri();

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
    log.debug("[DELETE] reviews id:{}, @{}", productId, REVIEW_SERVICE_URL);
    return Mono
      .fromRunnable(() -> sendMessage("reviews-out-0", new Event<Integer, Void>(Event.Type.DELETE, productId, null)))
      .subscribeOn(publishScheduler)
      .then();
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
