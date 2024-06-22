package hy.microservices.core.product.services;

import static java.util.logging.Level.FINE;

import java.time.Duration;
import java.util.Random;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;

import hy.api.core.product.Product;
import hy.api.core.product.ProductService;
import hy.api.exceptions.InvalidInputException;
import hy.api.exceptions.NotFoundException;
import hy.microservices.core.product.persistence.ProductEntity;
import hy.microservices.core.product.persistence.ProductRepository;
import hy.util.http.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
  private final ServiceUtil serviceUtil;
  private final ProductRepository repository;
  private final ProductMapper mapper;
  private final Random randomNumberGenerator = new Random();

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
  public Mono<Product> getProduct(int productId, int delay, int faultPercent) {
    validateProductId(productId);

    return repository.findByProductId(productId)
      .map(e -> throwErrorIfBadLuck(e, faultPercent))
      .delayElement(Duration.ofSeconds(delay))
      .switchIfEmpty(Mono.error(new NotFoundException("No product found for productId: " + productId)))
      .log(log.getName(), FINE)
      .map(mapper::entityToApi)
      .map(this::setProductServiceAddress);
  }

  @Override
  public Mono<Void> deleteProduct(int productId) {
    validateProductId(productId);

    log.debug("ProductServiceImpl::deleteProduct - productId: {}", productId);
    return repository.findByProductId(productId)
      .log(log.getName(), FINE)
      .map(repository::delete)
      .flatMap(e -> e);
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
