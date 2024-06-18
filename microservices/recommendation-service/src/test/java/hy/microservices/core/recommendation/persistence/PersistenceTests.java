package hy.microservices.core.recommendation.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import hy.microservices.core.recommendation.MongoDbTestBase;

@DataMongoTest
class PersistenceTests extends MongoDbTestBase {
  @Autowired
  private RecommendationRepository repository;
  private RecommendationEntity savedEntity;

  @BeforeEach
  void setupDb() {
    repository.deleteAll();
    RecommendationEntity entity = new RecommendationEntity(1, 2, "Author 1", 3, "Content 1");
    savedEntity = repository.save(entity);
    assertEqualsRecommendation(entity, savedEntity);
  }

  @Test
  void create() {
    RecommendationEntity newEntity = new RecommendationEntity(1, 3, "Author 2", 3, "Content 2");
    repository.save(newEntity);

    RecommendationEntity foundEntity = repository.findById(newEntity.getId()).get();
    assertEqualsRecommendation(newEntity, foundEntity);

    assertEquals(2, repository.count());
  }

  @Test
  void update() {
    savedEntity.setAuthor("Author 1 Updated");
    repository.save(savedEntity);

    RecommendationEntity foundEntity = repository.findById(savedEntity.getId()).get();
    assertEquals(1, (long) foundEntity.getVersion());
    assertEquals("Author 1 Updated", foundEntity.getAuthor());
  }

  @Test
  void delete() {
    repository.delete(savedEntity);
    assertFalse(repository.existsById(savedEntity.getId()));
  }

  @Test
  void getByProductId() {
    Iterable<RecommendationEntity> entities = repository.findByProductId(savedEntity.getProductId());

    assertThat(entities).hasSize(1);
    assertEqualsRecommendation(savedEntity, entities.iterator().next());
  }

  @Test
  void duplicateError() {
    RecommendationEntity entity = new RecommendationEntity(1, 2, "Author 3", 3, "Content 3");

    assertThrows(DuplicateKeyException.class, () -> repository.save(entity));
  }

  @Test
  void optimisticLockError() {
    RecommendationEntity entity1 = repository.findById(savedEntity.getId()).get();
    RecommendationEntity entity2 = repository.findById(savedEntity.getId()).get();

    entity1.setAuthor("Author 1 Updated");
    repository.save(entity1);

    entity2.setAuthor("Author 2 Updated");
    assertThrows(OptimisticLockingFailureException.class, () -> repository.save(entity2));

    RecommendationEntity updatedEntity = repository.findById(savedEntity.getId()).get();
    assertEquals(1, (int) updatedEntity.getVersion());
    assertEquals("Author 1 Updated", updatedEntity.getAuthor());
  }

  void assertEqualsRecommendation(RecommendationEntity expectedEntity, RecommendationEntity actualEntity) {
    assertEquals(expectedEntity.getId(), actualEntity.getId());
    assertEquals(expectedEntity.getVersion(), actualEntity.getVersion());
    assertEquals(expectedEntity.getProductId(), actualEntity.getProductId());
    assertEquals(expectedEntity.getRecommendationId(), actualEntity.getRecommendationId());
    assertEquals(expectedEntity.getAuthor(), actualEntity.getAuthor());
    assertEquals(expectedEntity.getRating(), actualEntity.getRating());
    assertEquals(expectedEntity.getContent(), actualEntity.getContent());
  }
}