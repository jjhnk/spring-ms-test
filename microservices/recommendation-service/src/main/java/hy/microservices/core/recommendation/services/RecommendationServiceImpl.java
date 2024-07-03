package hy.microservices.core.recommendation.services;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RestController;

import hy.api.core.recommendation.Recommendation;
import hy.api.core.recommendation.RecommendationService;
import hy.api.exceptions.InvalidInputException;
import hy.microservices.core.recommendation.config.RedisConfig;
import hy.microservices.core.recommendation.persistence.RecommendationEntity;
import hy.microservices.core.recommendation.persistence.RecommendationRepository;
import hy.util.http.ServiceUtil;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class RecommendationServiceImpl implements RecommendationService {
  private static final String CACHE_CONTROL_NO_STORE = "no-store";
  private static final String CACHE_KEY = "product:recommendations:";
  private final ServiceUtil serviceUtil;
  private final RecommendationRepository repository;
  private final RecommendationMapper mapper;
  private final ValueOperations<String, List<RecommendationEntity>> valueOps;
  private final boolean isRedisEnabled;

  public RecommendationServiceImpl(ServiceUtil serviceUtil, RecommendationMapper mapper,
    RecommendationRepository repository, ValueOperations<String, List<RecommendationEntity>> valueOps, RedisConfig redisConfig) {
    this.serviceUtil = serviceUtil;
    this.repository = repository;
    this.mapper = mapper;
    this.valueOps = valueOps;
    this.isRedisEnabled = redisConfig.isRedisEnabled();
  }

  @Override
  public Mono<Recommendation> createRecommendation(Recommendation body) {
    validateProductId(body);

    return repository.save(mapper.apiToEntity(body))
      .log(log.getName(), Level.FINE)
      .onErrorMap(DuplicateKeyException.class,
        ex -> new InvalidInputException(
          "Duplicate key, productId: " + body.getProductId() + ", Recommendation Id: " + body.getRecommendationId()))
      .map(mapper::entityToApi);
  }

  @Override
  public Flux<Recommendation> getRecommendations(HttpHeaders headers, int productId) {
    validateProduct(productId);

    List<RecommendationEntity> cached = getRecommendationsFromCache(headers, productId);
    if (cached != null && !cached.isEmpty()) {
      return Flux.fromIterable(cached)
        .map(mapper::entityToApi)
        .map(this::setServiceAddress);
    }

    List<RecommendationEntity> recommendations = new CopyOnWriteArrayList<>();

    return repository.findByProductId(productId)
      .log(log.getName(), Level.FINE)
      .doOnNext(recommendations::add)
      .map(mapper::entityToApi)
      .map(this::setServiceAddress)
      .doOnComplete(() -> setRecommendationsInCache(headers, productId, recommendations));
  }

  private List<RecommendationEntity> getRecommendationsFromCache(HttpHeaders headers, int productId) {
    if (!isCacheable(headers)) {
      return Collections.emptyList();
    }

    String cacheKey = CACHE_KEY + productId;
    return valueOps.get(cacheKey);
  }

  private void setRecommendationsInCache(HttpHeaders headers, int productId, List<RecommendationEntity> recommendations) {
    if (isCacheable(headers)) {
      String cacheKey = CACHE_KEY + productId;
      valueOps.set(cacheKey, recommendations);
    }
  }

  @Override
  public Mono<Void> deleteRecommendations(int productId) {
    validateProduct(productId);
    log.info("RecommendationServiceImpl::deleteRecommendations: recommendations deleted for productId: {}", productId);

    if (isCacheable(null)) {
      valueOps.getOperations().delete(CACHE_KEY + productId);
    }

    return repository.deleteAll(repository.findByProductId(productId));
  }

  // to use in cacheable annotation
  public boolean isCacheable(HttpHeaders headers) {
    if (!isRedisEnabled) {
      return false;
    }

    if (headers == null) {
      return true;
    }

    String cacheControl = headers.getCacheControl();
    return cacheControl == null || !cacheControl.contains(CACHE_CONTROL_NO_STORE);
  }

  private void validateProductId(Recommendation body) {
    validateProduct(body.getProductId());
  }

  private void validateProduct(int productId) {
    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }
  }

  private Recommendation setServiceAddress(Recommendation e) {
    e.setServiceAddress(serviceUtil.getServiceAddress());
    return e;
  }
}
