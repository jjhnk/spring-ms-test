package hy.microservices.core.review.services;

import static java.util.logging.Level.FINE;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RestController;

import hy.api.core.review.Review;
import hy.api.core.review.ReviewService;
import hy.api.exceptions.InvalidInputException;
import hy.microservices.core.review.config.RedisConfig;
import hy.microservices.core.review.persistence.ReviewEntity;
import hy.microservices.core.review.persistence.ReviewRepository;
import hy.util.http.ServiceUtil;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Slf4j
@RestController
public class ReviewServiceImpl implements ReviewService {
  private static final String CACHE_CONTROL_NO_STORE = "no-store";
  private static final String CACHE_KEY = "product:reviews:";
  private final ServiceUtil serviceUtil;
  private final ReviewRepository repository;
  private final ReviewMapper mapper;
  private final Scheduler jdbcScheduler;
  private final ValueOperations<String, List<ReviewEntity>> valueOps;
  private final boolean isRedisEnabled;

  public ReviewServiceImpl(ServiceUtil serviceUtil, ReviewRepository repository, ReviewMapper mapper,
    ValueOperations<String, List<ReviewEntity>> valueOps, @Qualifier("jdbcScheduler") Scheduler jdbcScheduler,
    RedisConfig redisConfig) {
    this.serviceUtil = serviceUtil;
    this.repository = repository;
    this.mapper = mapper;
    this.jdbcScheduler = jdbcScheduler;
    this.valueOps = valueOps;
    this.isRedisEnabled = redisConfig.isRedisEnabled();
  }

  @Override
  public Mono<Review> createReview(Review body) {
    validateProductId(body);

    return Mono.fromCallable(() -> internalCreateReview(body))
      .subscribeOn(jdbcScheduler);
  }

  private Review internalCreateReview(Review body) {
    try {
      log.info("ReviewServiceImpl::createReview - productId: {}/{}", body.getProductId(), body.getReviewId());
      var entity = mapper.apiToEntity(body);
      return mapper.entityToApi(repository.save(entity));
    } catch (DataIntegrityViolationException dve) {
      throw new InvalidInputException(
        "Duplicate key, productId: " + body.getProductId() + " Review Id: " + body.getReviewId());
    }
  }

  @Override
  public Flux<Review> getReviews(HttpHeaders headers, int productId) {
    validateProductId(productId);

    log.info("ReviewServiceImpl::getReviews - productId: {}", productId);
    List<ReviewEntity> cached = getReviewsFromCache(headers, productId);
    if (cached != null && !cached.isEmpty()) {
      return Flux.fromIterable(cached)
        .map(mapper::entityToApi)
        .map(this::setServiceAddress);
    }

    List<ReviewEntity> reviewEntities = repository.findByProductId(productId);
    setReviewsInCache(headers, productId, reviewEntities);
    List<Review> reviews = mapper.entityListToApiList(reviewEntities);
    reviews.forEach(this::setServiceAddress);

    return Mono.just(reviews)
      .flatMapMany(Flux::fromIterable)
      .log(log.getName(), FINE)
      .subscribeOn(jdbcScheduler);
  }

  private List<ReviewEntity> getReviewsFromCache(HttpHeaders headers, int productId) {
    if (!isCacheable(headers)) {
      return Collections.emptyList();
    }

    String cacheKey = CACHE_KEY + productId;
    return valueOps.get(cacheKey);
  }

  private void setReviewsInCache(HttpHeaders headers, int productId, List<ReviewEntity> reviews) {
    if (isCacheable(headers)) {
      String cacheKey = CACHE_KEY + productId;
      valueOps.set(cacheKey, reviews);
    }
  }

  private Review setServiceAddress(Review e) {
    e.setServiceAddress(serviceUtil.getServiceAddress());
    return e;
  }

  // @formatter:off
  @Override
  public Mono<Void> deleteReviews(int productId) {
    log.info("ReviewServiceImpl::deleteReviews - productId: {}", productId);
    if (isCacheable(null)) {
      valueOps.getOperations().delete(CACHE_KEY + productId);

    }
    return Mono.fromRunnable(() -> repository.deleteAll(repository.findByProductId(productId)))
      .subscribeOn(jdbcScheduler)
      .then();
  }
  // @formatter:on

  boolean isCacheable(HttpHeaders headers) {
    if (!isRedisEnabled) {
      return false;
    }

    if (headers == null) {
      return true;
    }

    String cacheControl = headers.getCacheControl();
    return cacheControl == null || !cacheControl.contains(CACHE_CONTROL_NO_STORE);
  }

  private void validateProductId(int productId) {
    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }
  }

  private void validateProductId(Review review) {
    validateProductId(review.getProductId());
  }
}
