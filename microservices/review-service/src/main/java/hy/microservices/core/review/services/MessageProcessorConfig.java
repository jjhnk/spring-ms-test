package hy.microservices.core.review.services;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hy.api.core.review.Review;
import hy.api.core.review.ReviewService;
import hy.api.event.Event;
import hy.api.exceptions.EventProcessingException;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class MessageProcessorConfig {
  private final ReviewService reviewService;

  public MessageProcessorConfig(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  @Bean
  EventConsumer messageProcessor() {
    return new EventConsumer(reviewService);
  }

  public class EventConsumer implements Consumer<Event<Integer, Review>> {
    private final ReviewService reviewService;
    private final String className = getClass().getSimpleName();

    public EventConsumer(ReviewService reviewService) {
      this.reviewService = reviewService;
    }

    @Override
    public void accept(Event<Integer, Review> event) {
      log.info("{} received event: {}", className, event);

      switch (event.getType()) {
        case CREATE:
          Review review = event.getData();
          log.info("{} create Review: {}", className, review);
          reviewService.createReview(review).block();
          break;
        case DELETE:
          int productId = event.getKey();
          log.info("{} delete ReviewId: {}", className, productId);
          reviewService.deleteReviews(productId)
            .block();
          break;
        default:
          String errorMessage = "Incorrect event type: " + event.getType() + ", expected a CREATE or DELETE event";
          log.warn(errorMessage);
          throw new EventProcessingException(errorMessage);
      }

      log.info("{} processed event: {}", className, event);
    }
  }

}
