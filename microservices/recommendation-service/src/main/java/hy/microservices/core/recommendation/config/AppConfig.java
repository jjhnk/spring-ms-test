package hy.microservices.core.recommendation.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.index.ReactiveIndexOperations;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;

import hy.microservices.core.recommendation.persistence.RecommendationEntity;

@Configuration
public class AppConfig {
  private final ReactiveMongoOperations mongoTemplate;

  public AppConfig(ReactiveMongoOperations mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @EventListener(ContextRefreshedEvent.class)
  public void initIndicesAfterStartup() {
    // @formatter:off
    MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext =
      mongoTemplate.getConverter().getMappingContext();
    // @formatter:on
    MongoPersistentEntityIndexResolver indexResolver = new MongoPersistentEntityIndexResolver(mappingContext);

    ReactiveIndexOperations indexOps = mongoTemplate.indexOps(RecommendationEntity.class);
    indexResolver.resolveIndexFor(RecommendationEntity.class)
      .forEach(indexOps::ensureIndex);
  }

}
