package hy.microservices.core.product.services;

import static java.util.logging.Level.FINE;

import java.time.Duration;
import java.util.Random;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RestController;

import hy.api.core.product.Product;
import hy.api.core.product.ProductService;
import hy.api.exceptions.InvalidInputException;
import hy.api.exceptions.NotFoundException;
import hy.microservices.core.product.config.RedisConfig;
import hy.microservices.core.product.persistence.ProductEntity;
import hy.microservices.core.product.persistence.ProductRepository;
import hy.util.http.ServiceUtil;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ProductServiceImpl implements ProductService {
  private static final String CACHE_CONTROL_NO_STORE = "no-store";
  private static final String CACHE_KEY = "product:products:";
  private final ServiceUtil serviceUtil;
  private final ProductRepository repository;
  private final ProductMapper mapper;
  private final Random randomNumberGenerator = new Random();
  private final ValueOperations<String, Product> valueOps;
  private final boolean isRedisEnabled;

  public ProductServiceImpl(ServiceUtil serviceUtil, ProductRepository repository, ProductMapper mapper,
    ValueOperations<String, Product> valueOps, RedisConfig redisConfig) {
    this.serviceUtil = serviceUtil;
    this.repository = repository;
    this.mapper = mapper;
    this.valueOps = valueOps;
    this.isRedisEnabled = redisConfig.isRedisEnabled();
  }

  @Override
  public Mono<Product> createProduct(Product body) {
    validateProductId(body);

    return repository.save(mapper.apiToEntity(body))
      .log(log.getName(), FINE)
      .onErrorMap(DuplicateKeyException.class,
        ex -> new InvalidInputException("Duplicate key, productId: " + body.getProductId()))
      .map(mapper::entityToApi);
  }

  @Override
  public Mono<Product> getProduct(HttpHeaders headers, int productId, int delay, int faultPercent) {
    validateProductId(productId);
    log.info("ProductServiceImpl::getProduct - productId: {}", productId);

    Product cached = getProductFromCache(headers, productId);
    if (cached != null) {
      return Mono.just(cached);
    }

    return repository.findByProductId(productId)
      .map(e -> throwErrorIfBadLuck(e, faultPercent))
      .delayElement(Duration.ofSeconds(delay))
      .switchIfEmpty(Mono.error(new NotFoundException("No product found for productId: " + productId)))
      .log(log.getName(), FINE)
      .map(mapper::entityToApi)
      .map(this::setProductServiceAddress)
      .doOnNext(product -> setProductInCache(headers, productId, product));
  }

  private Product getProductFromCache(HttpHeaders headers, int productId) {
    if (!isCacheable(headers)) {
      return null;
    }

    String cacheKey = CACHE_KEY + productId;
    return valueOps.get(cacheKey);
  }

  private void setProductInCache(HttpHeaders headers, int productId, Product product) {
    if (isCacheable(headers)) {
      valueOps.set(CACHE_KEY + productId, product);
    }
  }

  @Override
  public Mono<Void> deleteProduct(int productId) {
    validateProductId(productId);

    log.info("ProductServiceImpl::deleteProduct - productId: {}", productId);
    if (isCacheable(null)) {
      valueOps.getOperations().delete(CACHE_KEY + productId);
    }

    return repository.findByProductId(productId)
      .log(log.getName(), FINE)
      .map(repository::delete)
      .flatMap(e -> e);
  }

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

  private void validateProductId(Product product) {
    validateProductId(product.getProductId());
  }

  private void validateProductId(int productId) {
    if (productId < 1)
      throw new InvalidInputException("Invalid productId: " + productId);
  }

  private Product setProductServiceAddress(Product product) {
    product.setServiceAddress(serviceUtil.getServiceAddress());
    return product;
  }

  private ProductEntity throwErrorIfBadLuck(ProductEntity entity, int faultPercent) {

    if (faultPercent == 0) {
      return entity;
    }

    int randomThreshold = getRandomNumber(1, 100);

    if (faultPercent >= randomThreshold) {
      log.info("Bad luck, an error occurred, {} >= {}", faultPercent, randomThreshold);
      throw new RuntimeException("Something went wrong...");
    }

    return entity;
  }

  private int getRandomNumber(int min, int max) {
    if (max < min) {
      throw new IllegalArgumentException("Max must be greater than min");
    }

    return randomNumberGenerator.nextInt((max - min) + 1) + min;
  }
}
