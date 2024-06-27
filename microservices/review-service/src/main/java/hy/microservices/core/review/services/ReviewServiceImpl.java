package hy.microservices.core.review.services;

import static java.util.logging.Level.FINE;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RestController;

import hy.api.core.review.Review;
import hy.api.core.review.ReviewService;
import hy.api.exceptions.InvalidInputException;
import hy.microservices.core.review.persistence.ReviewRepository;
import hy.util.http.ServiceUtil;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Slf4j
@RestController
public class ReviewServiceImpl implements ReviewService {
  private final ServiceUtil serviceUtil;
  private final ReviewRepository repository;
  private final ReviewMapper mapper;
  private final Scheduler jdbcScheduler;

  public ReviewServiceImpl(ServiceUtil serviceUtil, ReviewRepository repository, ReviewMapper mapper,
    @Qualifier("jdbcScheduler") Scheduler jdbcScheduler) {
    this.serviceUtil = serviceUtil;
    this.repository = repository;
    this.mapper = mapper;
    this.jdbcScheduler = jdbcScheduler;
  }

  @Override
  public Mono<Review> createReview(Review body) {
    validateProductId(body);

    return Mono.fromCallable(() -> internalCreateReview(body))
      .subscribeOn(jdbcScheduler);
  }

  private Review internalCreateReview(Review body) {
    try {
      log.debug("ReviewServiceImpl::createReview - productId: {}/{}", body.getProductId(), body.getReviewId());
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

    log.debug("ReviewServiceImpl::getReviews - productId: {}", productId);
    return Mono.fromCallable(() -> internalGetReviews(productId))
      .flatMapMany(Flux::fromIterable)
      .log(log.getName(), FINE)
      .subscribeOn(jdbcScheduler);
  }

  private List<Review> internalGetReviews(int productId) {
    List<Review> apiList = mapper.entityListToApiList(repository.findByProductId(productId));
    apiList.forEach(this::setServiceAddress);
    return apiList;
  }

  private void setServiceAddress(Review e) {
    e.setServiceAddress(serviceUtil.getServiceAddress());
  }

  @Override
  public Mono<Void> deleteReviews(int productId) {
    log.debug("ReviewServiceImpl::deleteReviews - productId: {}", productId);
    return Mono.fromRunnable(() -> repository.deleteAll(repository.findByProductId(productId)))
      .subscribeOn(jdbcScheduler)
      .then();
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
