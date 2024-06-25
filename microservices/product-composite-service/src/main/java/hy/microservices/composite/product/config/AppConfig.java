package hy.microservices.composite.product.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.slf4j.Slf4j;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
@Slf4j
public class AppConfig {
  @Value("${api.common.version}")
  String apiVersion;
  @Value("${api.common.title}")
  String apiTitle;
  @Value("${api.common.description}")
  String apiDescription;
  @Value("${api.common.termsOfService}")
  String apiTermsOfService;
  @Value("${api.common.license}")
  String apiLicense;
  @Value("${api.common.licenseUrl}")
  String apiLicenseUrl;
  @Value("${api.common.externalDocDesc}")
  String apiExternalDocDesc;
  @Value("${api.common.externalDocUrl}")
  String apiExternalDocUrl;
  @Value("${api.common.contact.name}")
  String apiContactName;
  @Value("${api.common.contact.url}")
  String apiContactUrl;
  @Value("${api.common.contact.email}")
  String apiContactEmail;

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
  OpenAPI getOpenApi() {
    return new OpenAPI().info(new Info().title(apiTitle)
      .description(apiDescription)
      .version(apiVersion)
      .contact(new Contact().name(apiContactName)
        .url(apiContactUrl)
        .email(apiContactEmail))
      .termsOfService(apiTermsOfService)
      .license(new License().name(apiLicense)
        .url(apiLicenseUrl)))
      .externalDocs(new ExternalDocumentation().description(apiExternalDocDesc)
        .url(apiExternalDocUrl));
  }

  @Bean
  WebClient webClient(WebClient.Builder builder) {
    return builder.build();
  }

}
