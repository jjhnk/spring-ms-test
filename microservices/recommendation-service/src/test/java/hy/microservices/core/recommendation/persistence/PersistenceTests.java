package hy.microservices.core.recommendation.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import hy.microservices.core.recommendation.MongoDbTestBase;

@DataMongoTest(properties = {"spring.cloud.config.enabled=false"})
class PersistenceTests extends MongoDbTestBase {
  @Autowired
  private RecommendationRepository repository;
  private RecommendationEntity savedEntity;

  @BeforeEach
  void setupDb() {
    repository.deleteAll().block();

    RecommendationEntity entity = new RecommendationEntity(1, 2, "Author 1", 3, "Content 1");
    savedEntity = repository.save(entity).block();

    assertEqualsRecommendation(entity, savedEntity);
  }

  @Test
  void create() {
    RecommendationEntity newEntity = new RecommendationEntity(1, 3, "Author 2", 3, "Content 2");
    repository.save(newEntity).block();

    RecommendationEntity foundEntity = repository.findById(newEntity.getId()).block();
    assertEqualsRecommendation(newEntity, foundEntity);

    assertThat(repository.count().block()).isEqualTo(2);
  }

  @Test
  void update() {
    savedEntity.setAuthor("Author 1 Updated");
    repository.save(savedEntity).block();

    RecommendationEntity foundEntity = repository.findById(savedEntity.getId()).block();
    assertThat(foundEntity.getVersion()).isOne();
    assertThat(foundEntity.getAuthor()).isEqualTo("Author 1 Updated");
  }

  @Test
  void delete() {
    repository.delete(savedEntity).block();
    assertThat(repository.existsById(savedEntity.getId()).block()).isFalse();
  }

  @Test
  void getByProductId() {
    Iterable<RecommendationEntity> entities = repository.findByProductId(savedEntity.getProductId()).collectList().block();

    assertThat(entities).hasSize(1);
    assertEqualsRecommendation(savedEntity, entities.iterator().next());
  }

  @Test
  void duplicateError() {
    RecommendationEntity entity = new RecommendationEntity(1, 2, "Author 3", 3, "Content 3");
    var save = repository.save(entity);
    assertThrows(DuplicateKeyException.class, () -> save.block());
  }

  @Test
  void optimisticLockError() {
    RecommendationEntity entity1 = repository.findById(savedEntity.getId()).block();
    RecommendationEntity entity2 = repository.findById(savedEntity.getId()).block();

    entity1.setAuthor("Author 1 Updated");
    repository.save(entity1).block();

    entity2.setAuthor("Author 2 Updated");
    var save = repository.save(entity2);
    assertThrows(OptimisticLockingFailureException.class, () -> save.block());

    RecommendationEntity updatedEntity = repository.findById(savedEntity.getId()).block();
    assertThat(updatedEntity.getVersion()).isOne();
    assertThat(updatedEntity.getAuthor()).isEqualTo("Author 1 Updated");
  }

  void assertEqualsRecommendation(RecommendationEntity expectedEntity, RecommendationEntity actualEntity) {
    assertThat(expectedEntity.getId()).isEqualTo(actualEntity.getId());
    assertThat(expectedEntity.getVersion()).isEqualTo(actualEntity.getVersion());
    assertThat(expectedEntity.getProductId()).isEqualTo(actualEntity.getProductId());
    assertThat(expectedEntity.getRecommendationId()).isEqualTo(actualEntity.getRecommendationId());
    assertThat(expectedEntity.getAuthor()).isEqualTo(actualEntity.getAuthor());
    assertThat(expectedEntity.getRating()).isEqualTo(actualEntity.getRating());
    assertThat(expectedEntity.getContent()).isEqualTo(actualEntity.getContent());
  }
}