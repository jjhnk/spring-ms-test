package hy.microservices.core.review.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
public class AppConfig {
  private final Integer threadPoolSize;
  private final Integer taskQueueSize;

  public AppConfig(@Value("${app.threadPoolSize:10}") Integer threadPoolSize,
    @Value("${app.taskQueueSize:100}") Integer taskQueueSize) {
    this.threadPoolSize = threadPoolSize;
    this.taskQueueSize = taskQueueSize;
  }

  @Bean
  Scheduler jdbcScheduler() {
    return Schedulers.newBoundedElastic(threadPoolSize, taskQueueSize, "jdbc-pool");
  }

}
