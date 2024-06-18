package hy.microservices.core.review.services;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;
import hy.api.core.review.Review;
import hy.api.core.review.ReviewService;
import hy.api.exceptions.InvalidInputException;
import hy.microservices.core.review.persistence.ReviewEntity;
import hy.microservices.core.review.persistence.ReviewRepository;
import hy.util.http.ServiceUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ReviewServiceImpl implements ReviewService {
  private final ServiceUtil serviceUtil;
  private final ReviewRepository repository;
  private final ReviewMapper mapper;

  public ReviewServiceImpl(ServiceUtil serviceUtil, ReviewRepository repository, ReviewMapper mapper) {
    this.serviceUtil = serviceUtil;
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Review createReview(Review body) {
    try {
      log.debug("ReviewServiceImpl::createReview - body: {}", body);
      ReviewEntity entity = repository.save(mapper.apiToEntity(body));

      return mapper.entityToApi(entity);
    } catch (DataIntegrityViolationException dve) {
      throw new InvalidInputException("Duplicate key, Product Id: " + body.getProductId() + " Review Id: " + body.getReviewId());
    }
  }

  @Override
  public List<Review> getReviews(int productId) {

    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    List<Review> apiList = mapper.entityListToApiList(repository.findByProductId(productId));
    apiList.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));

    log.debug("ReviewServiceImpl::getReviews: responsesize: {}", apiList.size());
    return apiList;
  }

  @Override
  public void deleteReviews(int productId) {
    log.debug("ReviewServiceImpl::deleteReviews - productId: {}", productId);
    repository.deleteAll(repository.findByProductId(productId));
  }
}
