package hy.springcloud.gateway.config;

import static java.util.logging.Level.FINE;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.CompositeReactiveHealthContributor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Configuration
public class HealthCheckConfig {
  private static final Logger LOG = LoggerFactory.getLogger(HealthCheckConfig.class);

  private WebClient webClient;

  public HealthCheckConfig(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.build();
  }

  @Bean
  ReactiveHealthContributor coreServices() {
    final Map<String, ReactiveHealthIndicator> registry = new LinkedHashMap<>();

    registry.put("product", () -> getHealth("http://product:4004"));
    registry.put("recommendation", () -> getHealth("http://recommendation:4004"));
    registry.put("review", () -> getHealth("http://review:4004"));
    registry.put("product-composite", () -> getHealth("http://product-composite:4004"));
    registry.put("auth-server", () -> getHealth("http://auth-server:4004"));

    return CompositeReactiveHealthContributor.fromMap(registry);
  }

  private Mono<Health> getHealth(String baseUrl) {
    String url = baseUrl + "/actuator/health";
    LOG.info("Setting up a call to the Health API on URL: {}", url);
    return webClient.get()
      .uri(url)
      .retrieve()
      .bodyToMono(String.class)
      .map(s -> new Health.Builder().up().build())
      .onErrorResume(ex -> Mono.just(new Health.Builder().down(ex).build()))
      .log(LOG.getName(), FINE);
  }
}