package hy.microservices.composite.product.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.adapter.ForwardedHeaderTransformer;

import lombok.extern.slf4j.Slf4j;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
@Slf4j
public class AppConfig {

  private final Integer threadPoolSize;
  private final Integer taskQueueSize;

  // @formatter:off
  public AppConfig(
    @Value("${app.threadPoolSize:10}") Integer threadPoolSize,
    @Value("${app.taskQueueSize:100}") Integer taskQueueSize) {
    this.threadPoolSize = threadPoolSize;
    this.taskQueueSize = taskQueueSize;
  }
  // @formatter:on

  @Bean
  Scheduler publishEventScheduler() {
    log.info("{}: Using {} thread pool size with {} task queue size", getClass().getSimpleName(), threadPoolSize,
      taskQueueSize);
    return Schedulers.newBoundedElastic(threadPoolSize, taskQueueSize, "publish-pool");
  }

  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  WebClient webClient(WebClient.Builder builder) {
    return builder.build();
  }

  @Bean
  public ForwardedHeaderTransformer forwardedHeaderTransformer() {
    return new ForwardedHeaderTransformer();
  }

}
