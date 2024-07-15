package hy.oltp.core.estate.building.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import hy.api.core.estate.unit.UnitStatus;
import hy.oltp.core.estate.tenant.PostgreSqlTestBase;
import hy.oltp.core.estate.unit.persistence.UnitDetailEntity;
import hy.oltp.core.estate.unit.persistence.UnitEntity;
import hy.oltp.core.estate.unit.persistence.UnitSummaryEntity;

@DataJpaTest(properties = {"spring.jpa.hibernate.ddl-auto=update"})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BuildingPersistenceTests extends PostgreSqlTestBase {
  @Autowired
  private BuildingRepository repository;
  private BuildingEntity saved;

  // @formatter:off
  @BeforeEach
  void setupDb() {
    repository.deleteAll();
    BuildingEntity entity = new BuildingEntity("name", "address", "city", "state", "zipCode", "description");

    var unit1 = new UnitEntity(new UnitSummaryEntity("b101", 1, UnitStatus.AVAILABLE),
      new UnitDetailEntity(30, new BigDecimal(10)));
    unit1.setBuilding(entity);
    var unit2 = new UnitEntity(new UnitSummaryEntity("b102", 2, UnitStatus.AVAILABLE),
      new UnitDetailEntity(30, new BigDecimal(10)));
    unit2.setBuilding(entity);

    entity.setUnits(Set.of(unit1, unit2));
    saved = repository.save(entity);

    assertThat(saved).usingRecursiveComparison().isEqualTo(entity);
  }

  @Test
  @Transactional
  void create() {
    BuildingEntity newEntity = new BuildingEntity("name2", "address2", "city2", "state2", "zipCode2", "description2");
    repository.save(newEntity);

    BuildingEntity found = repository.findById(newEntity.getId()).get();
    assertThat(found).usingRecursiveComparison().isEqualTo(newEntity);
    assertThat(repository.count()).isEqualTo(2L);
  }

  @Test
  void update() {
    saved.setName("name2");
    repository.save(saved);

    BuildingEntity found = repository.findById(saved.getId()).get();
    assertThat(found.getVersion()).isOne();
    assertThat(found.getName()).isEqualTo("name2");
  }

  @Test
  void delete() {
    repository.delete(saved);
    assertThat(repository.existsById(saved.getId())).isFalse();
  }

  @Test
  @Transactional
  void getByAddress() {
    List<BuildingEntity> found = repository.findByAddress("address");

    assertThat(found).hasSize(1);
    assertThat(saved).usingRecursiveComparison().isEqualTo(found.get(0));
  }

  @Test
  @Transactional
  void getByCity() {
    List<BuildingEntity> found = repository.findByCity("city");

    assertThat(found).hasSize(1);
    assertThat(saved).usingRecursiveComparison().isEqualTo(found.get(0));
  }

  @Test
  @Transactional
  void getByState() {
    List<BuildingEntity> found = repository.findByState("state");

    assertThat(found).hasSize(1);
    assertThat(saved).usingRecursiveComparison().isEqualTo(found.get(0));
  }

  @Test
  @Transactional
  void getByZipCode() {
    List<BuildingEntity> found = repository.findByZipCode("zipCode");

    assertThat(found).hasSize(1);
    assertThat(saved).usingRecursiveComparison().isEqualTo(found.get(0));
  }

  @Test
  @Transactional
  void getByName() {
    List<BuildingEntity> found = repository.findByName("name");

    assertThat(found).hasSize(1);
    assertThat(saved).usingRecursiveComparison().isEqualTo(found.get(0));
  }

  @Test
  void optimisticLockError() {
    BuildingEntity entity1 = repository.findById(saved.getId()).get();
    BuildingEntity entity2 = repository.findById(saved.getId()).get();

    entity1.setName("name Updated 1");
    repository.save(entity1);

    entity2.setName("name Updated 2");
    assertThrows(ObjectOptimisticLockingFailureException.class, () -> repository.save(entity2));

    BuildingEntity updated = repository.findById(saved.getId()).get();
    assertThat(updated.getVersion()).isEqualTo(1);
    assertThat(updated.getName()).isEqualTo("name Updated 1");
  }
  // @formatter:on
}
