package hy.oltp.core.estate.tenant.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

import hy.oltp.core.estate.tenant.PostgreSqlTestBase;

@DataJpaTest(properties = {"spring.jpa.hibernate.ddl-auto=update"})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TenantPersistenceTests extends PostgreSqlTestBase {
  @Autowired
  private TenantRepository repository;
  private TenantEntity saved;

  @BeforeEach
  void setupDb() {
    repository.deleteAll();

    TenantEntity entity = new TenantEntity("firstName", "lastName", "email@email.com", "01012345678");
    saved = repository.save(entity);

    assertThat(saved).isEqualTo(entity);
  }

  @Test
  void create() {
    TenantEntity newEntity = new TenantEntity("firstName2", "lastName2", "email2@email.com", "22212345678");
    repository.save(newEntity);

    TenantEntity found = repository.findById(newEntity.getId()).get();
    assertThat(found).isEqualTo(newEntity);
    assertThat(repository.count()).isEqualTo(2L);
  }

  @Test
  void update() {
    saved.setFirstName("firstName2");
    repository.save(saved);

    TenantEntity found = repository.findById(saved.getId()).get();
    assertThat(found.getVersion()).isOne();
    assertThat(found.getFirstName()).isEqualTo("firstName2");
  }

  @Test
  void delete() {
    repository.delete(saved);
    assertThat(repository.existsById(saved.getId())).isFalse();
  }

  @Test
  void duplicateError() {
    TenantEntity entity = new TenantEntity("firstName", "lastName", "email@email.com", "01012345678");
    assertThrows(DataIntegrityViolationException.class, () -> repository.save(entity));
  }

  @Test
  void optimisticLockError() {
    TenantEntity entity1 = repository.findById(saved.getId()).get();
    TenantEntity entity2 = repository.findById(saved.getId()).get();

    entity1.setFirstName("firstName Updated 1");
    repository.save(entity1);

    entity2.setFirstName("firstName Updated 2");
    assertThrows(ObjectOptimisticLockingFailureException.class, () -> repository.save(entity2));

    TenantEntity updated = repository.findById(saved.getId()).get();
    assertThat(updated.getVersion()).isEqualTo(1);
    assertThat(updated.getFirstName()).isEqualTo("firstName Updated 1");

  }

  @Test
  void getByFirstName() {
    List<TenantEntity> found = repository.findByFirstName("firstName");

    assertThat(found).hasSize(1);
    assertThat(saved).isEqualTo(found.get(0));
  }

  @Test
  void getByLastName() {
    List<TenantEntity> found = repository.findByLastName("lastName");

    assertThat(found).hasSize(1);
    assertThat(saved).isEqualTo(found.get(0));
  }

  @Test
  void getByEmail() {
    List<TenantEntity> found = repository.findByEmail("email@email.com");

    assertThat(found).hasSize(1);
    assertThat(saved).isEqualTo(found.get(0));
  }

}
