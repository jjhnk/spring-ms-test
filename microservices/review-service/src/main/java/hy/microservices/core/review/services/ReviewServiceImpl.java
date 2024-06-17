package hy.microservices.core.review.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import hy.api.core.review.Review;
import hy.api.core.review.ReviewService;
import hy.api.exceptions.InvalidInputException;
import hy.util.http.ServiceUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ReviewServiceImpl implements ReviewService {
  private final ServiceUtil serviceUtil;

  public ReviewServiceImpl(ServiceUtil serviceUtil) {
    this.serviceUtil = serviceUtil;
  }

  @Override
  public List<Review> getReviews(int productId) {

    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    if (productId == 213) {
      log.debug("No reviews found for productId: {}", productId);
      return new ArrayList<>();
    }

    List<Review> list = new ArrayList<>();
    list.add(new Review(productId, 1, "Author 1", "Subject 1", "Content 1", serviceUtil.getServiceAddress()));
    list.add(new Review(productId, 2, "Author 2", "Subject 2", "Content 2", serviceUtil.getServiceAddress()));
    list.add(new Review(productId, 3, "Author 3", "Subject 3", "Content 3", serviceUtil.getServiceAddress()));

    log.debug("/reviews response size: {}", list.size());

    return list;
  }
}
