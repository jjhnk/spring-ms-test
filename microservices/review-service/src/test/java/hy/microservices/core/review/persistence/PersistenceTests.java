package hy.microservices.core.review.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import hy.microservices.core.review.MySqlTestBase;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersistenceTests extends MySqlTestBase {
  @Autowired
  private ReviewRepository repository;
  private ReviewEntity savedEntity;

  @BeforeEach
  void setupDb() {
    repository.deleteAll();
    ReviewEntity entity = new ReviewEntity(1, 1, "Awesome Movie", "Awesome Movie", "Awesome Movie");
    savedEntity = repository.save(entity);

    assertEquals(entity, savedEntity);
  }

  @Test
  void create() {
    ReviewEntity newEntity = new ReviewEntity(1, 3, "Author", "Subject", "Content");
    repository.save(newEntity);

    ReviewEntity foundEntity = repository.findById(newEntity.getId()).get();
    assertEquals(newEntity, foundEntity);
    assertEquals(2, repository.count());
  }

  @Test
  void update() {
    savedEntity.setAuthor("Updated Author");
    repository.save(savedEntity);

    ReviewEntity foundEntity = repository.findById(savedEntity.getId()).get();
    assertEquals(1, (long) foundEntity.getVersion());
    assertEquals("Updated Author", foundEntity.getAuthor());
  }

  @Test
  void delete() {
    repository.delete(savedEntity);
    assertFalse(repository.existsById(savedEntity.getId()));
  }

  @Test
  void findByProductId() {
    List<ReviewEntity> entityList = repository.findByProductId(savedEntity.getProductId());

    assertThat(entityList).hasSize(1);
    assertEquals(savedEntity, entityList.get(0));
  }

  @Test
  void duplicateError() {
    ReviewEntity entity = new ReviewEntity(1, 1, "Duplicate", "Duplicate", "Duplicate");
    assertThrows(DataIntegrityViolationException.class, () -> {
      repository.save(entity);
    });
  }

  @Test
  void optimisticLockError() {
    ReviewEntity entity1 = repository.findById(savedEntity.getId()).get();
    ReviewEntity entity2 = repository.findById(savedEntity.getId()).get();

    entity1.setAuthor("Author 1 Updated");
    repository.save(entity1);

    entity2.setAuthor("Author 2 Updated");
    assertThrows(ObjectOptimisticLockingFailureException.class, () -> repository.save(entity2));

    ReviewEntity updatedEntity = repository.findById(savedEntity.getId()).get();
    assertThat(updatedEntity.getVersion()).isEqualTo(1);
    assertThat(updatedEntity.getAuthor()).isEqualTo("Author 1 Updated");
  }
}
