package hy.microservices.core.recommendation.services;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hy.api.core.recommendation.Recommendation;
import hy.api.core.recommendation.RecommendationService;
import hy.api.event.Event;
import hy.api.exceptions.EventProcessingException;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class MessageProcessorConfig {
  private final RecommendationService recommendationService;

  public MessageProcessorConfig(RecommendationService recommendationService) {
    this.recommendationService = recommendationService;
  }

  @Bean
  EventConsumer messageProcessor() {
    return new EventConsumer(recommendationService);
  }

  public class EventConsumer implements Consumer<Event<Integer, Recommendation>> {
    private final RecommendationService recommendationService;
    private final String className = getClass().getSimpleName();

    public EventConsumer(RecommendationService recommendationService) {
      this.recommendationService = recommendationService;
    }

    @Override
    public void accept(Event<Integer, Recommendation> event) {
      log.info("{} received event: {}", className, event);

      switch (event.getType()) {
        case CREATE:
          Recommendation recommendation = event.getData();
          log.info("{} create recommendation: {}", className, recommendation);
          recommendationService.createRecommendation(recommendation)
            .block();
          break;
        case DELETE:
          int productId = event.getKey();
          log.info("{} delete Recommendation Id: {}", className, productId);
          recommendationService.deleteRecommendations(productId)
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
