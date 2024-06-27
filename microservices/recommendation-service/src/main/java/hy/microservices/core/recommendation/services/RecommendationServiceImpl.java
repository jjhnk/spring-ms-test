package hy.microservices.core.recommendation.services;

import java.util.logging.Level;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RestController;
import hy.api.core.recommendation.Recommendation;
import hy.api.core.recommendation.RecommendationService;
import hy.api.exceptions.InvalidInputException;
import hy.microservices.core.recommendation.persistence.RecommendationRepository;
import hy.util.http.ServiceUtil;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class RecommendationServiceImpl implements RecommendationService {
  private final ServiceUtil serviceUtil;
  private final RecommendationRepository repository;
  private final RecommendationMapper mapper;

  public RecommendationServiceImpl(ServiceUtil serviceUtil, RecommendationMapper mapper,
    RecommendationRepository repository) {
    this.serviceUtil = serviceUtil;
    this.repository = repository;
    this.mapper = mapper;
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

    return repository.findByProductId(productId)
      .log(log.getName(), Level.FINE)
      .map(mapper::entityToApi)
      .map(this::setServiceAddress);
  }

  @Override
  public Mono<Void> deleteRecommendations(int productId) {
    validateProduct(productId);

    log.info("RecommendationServiceImpl::deleteRecommendations: recommendations deleted for productId: {}", productId);
    return repository.deleteAll(repository.findByProductId(productId));
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
