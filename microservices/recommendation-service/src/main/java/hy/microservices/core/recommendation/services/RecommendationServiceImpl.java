package hy.microservices.core.recommendation.services;

import java.util.List;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import hy.api.core.recommendation.Recommendation;
import hy.api.core.recommendation.RecommendationService;
import hy.api.exceptions.InvalidInputException;
import hy.microservices.core.recommendation.persistence.RecommendationEntity;
import hy.microservices.core.recommendation.persistence.RecommendationRepository;
import hy.util.http.ServiceUtil;
import lombok.extern.slf4j.Slf4j;

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
  public Recommendation createRecommendation(Recommendation body) {
    try {
      RecommendationEntity entity = repository.save(mapper.apiToEntity(body));

      log.debug("RecommendationServiceImpl::createRecommendation - entity: {}", entity);
      return mapper.entityToApi(entity);
    } catch (DuplicateKeyException dke) {
      throw new InvalidInputException("Duplicate key, ProductId: " + body.getProductId() + ", RecommendationId: " + body.getRecommendationId());
    }
  }

  @Override
  public List<Recommendation> getRecommendations(int productId) {
    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    List<RecommendationEntity> entityList = repository.findByProductId(productId);
    List<Recommendation> apiList = mapper.entityListToApiList(entityList);
    apiList.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));

    log.info("RecommendationServiceImpl::getRecommendations: responsesize: {}", apiList.size());
    return apiList;
  }

  @Override
  public void deleteRecommendations(int productId) {
    log.info("RecommendationServiceImpl::deleteRecommendations: recommendations deleted for productId: {}", productId);
    repository.deleteAll(repository.findByProductId(productId));
  }
}
