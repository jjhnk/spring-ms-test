package hy.oltp.core.estate.unit.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import hy.api.core.estate.unit.RoomTypes;
import hy.api.core.estate.unit.UnitStatus;
import hy.oltp.core.estate.building.persistence.BuildingEntity;
import hy.oltp.core.estate.building.persistence.BuildingRepository;
import hy.oltp.core.estate.tenant.PostgreSqlTestBase;

@DataJpaTest(properties = {"spring.jpa.hibernate.ddl-auto=update"})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoomPersistenceTests extends PostgreSqlTestBase {
  @Autowired
  private BuildingRepository buildingRepository;
  @Autowired
  private UnitRepository unitRepository;
  @Autowired
  private RoomRepository repository;

  private UnitEntity savedUnitEntity;
  private RoomEntity savedRoomEntity;

  @BeforeEach
  public void setupDb() {
    buildingRepository.deleteAll();
    unitRepository.deleteAll();
    repository.deleteAll();

    BuildingEntity buildingEntity = new BuildingEntity("name", "address", "city", "state", "zipCode", "description");
    buildingRepository.save(buildingEntity);

    UnitEntity unitEntity = new UnitEntity(new UnitSummaryEntity("b101", 1, UnitStatus.AVAILABLE),
      new UnitDetailEntity(30, new BigDecimal(10)));
    unitEntity.setBuilding(buildingEntity);
    savedUnitEntity =  unitRepository.save(unitEntity);

    RoomEntity entity = new RoomEntity("Room 1", RoomTypes.DINING, 101, 102);
    entity.setUnit(unitEntity);
    savedRoomEntity = repository.save(entity);

    assertThat(savedRoomEntity).isEqualTo(entity);
  }

  @Test
  void create() {
    RoomEntity entity = new RoomEntity("Room 2", RoomTypes.LIVING, 101, 102);
    entity.setUnit(savedUnitEntity);
    repository.save(entity);

    RoomEntity found = repository.findById(entity.getId()).get();
    assertThat(found).isEqualTo(entity);
    assertThat(repository.count()).isEqualTo(2L);
  }

  @Test
  void update() {
    savedRoomEntity.setName("Room 2 Updated");
    repository.save(savedRoomEntity);

    RoomEntity found = repository.findById(savedRoomEntity.getId()).get();
    assertThat(found.getVersion()).isOne();
    assertThat(found.getName()).isEqualTo("Room 2 Updated");
  }

  @Test
  void delete() {
    repository.delete(savedRoomEntity);
    assertThat(repository.count()).isOne();
  }

  @Test
  void optimisticLockError() {
    RoomEntity entity1 = repository.findById(savedRoomEntity.getId()).get();
    RoomEntity entity2 = repository.findById(savedRoomEntity.getId()).get();

    entity1.setName("Room 2 Updated 1");
    repository.save(entity1);

    entity2.setName("Room 2 Updated 2");
    assertThrows(ObjectOptimisticLockingFailureException.class, () -> repository.save(entity2));

    RoomEntity updated = repository.findById(savedRoomEntity.getId()).get();
    assertThat(updated.getVersion()).isEqualTo(1);
    assertThat(updated.getName()).isEqualTo("Room 2 Updated 1");
  }

  @Test
  void createWithUnit() {
    RoomEntity entity = new RoomEntity("Room 2", RoomTypes.LIVING, 101, 102);
    repository.saveByUnitId(entity, savedUnitEntity.getId());

    RoomEntity found = repository.findById(entity.getId()).get();
    assertThat(found).isEqualTo(entity);
    assertThat(repository.count()).isEqualTo(2L);
  }

  @Test
  void createWithInvalidUnitError() {
    RoomEntity entity = new RoomEntity("Room 2", RoomTypes.LIVING, 101, 102);

    assertThrows(InvalidDataAccessApiUsageException.class, () -> repository.saveByUnitId(entity, 100));
  }

  @Test
  void createAllWithUnit() {
    RoomEntity entity1 = new RoomEntity("Room 1", RoomTypes.LIVING, 101, 102);
    RoomEntity entity2 = new RoomEntity("Room 2", RoomTypes.BEDROOMS, 201, 202);

    List<RoomEntity> entities = new ArrayList<>();
    entities.add(entity1);
    entities.add(entity2);

    repository.saveAllByUnitId(entities, savedUnitEntity.getId());

    assertThat(repository.count()).isEqualTo(3L);

    RoomEntity found = repository.findById(entity1.getId()).get();
    assertThat(found).isEqualTo(entity1);
    found = repository.findById(entity2.getId()).get();
    assertThat(found).isEqualTo(entity2);
  }

  @Test
  void getByIdAndUnitId() {
    RoomEntity entity1 = new RoomEntity("Room 1", RoomTypes.LIVING, 101, 102);
    RoomEntity entity2 = new RoomEntity("Room 2", RoomTypes.BEDROOMS, 201, 202);

    List<RoomEntity> entities = new ArrayList<>();
    entities.add(entity1);
    entities.add(entity2);

    repository.saveAllByUnitId(entities, savedUnitEntity.getId());

    RoomEntity found = repository.findByIdAndUnitId(entity1.getId(), savedUnitEntity.getId());
    assertThat(found).isEqualTo(entity1);
    found = repository.findByIdAndUnitId(entity2.getId(), savedUnitEntity.getId());
    assertThat(found).isEqualTo(entity2);
  }

  @Test
  void getAllByUnitId() {
    RoomEntity entity1 = new RoomEntity("Room 1", RoomTypes.LIVING, 101, 102);
    RoomEntity entity2 = new RoomEntity("Room 2", RoomTypes.BEDROOMS, 201, 202);

    List<RoomEntity> entities = new ArrayList<>();
    entities.add(entity1);
    entities.add(entity2);

    repository.saveAllByUnitId(entities, savedUnitEntity.getId());

    assertThat(repository.findAllByUnitId(savedUnitEntity.getId())).containsExactlyInAnyOrder(savedRoomEntity, entity1, entity2);
  }

  @Test
  @Transactional
  void deleteByIdAndUnitId() {
    RoomEntity entity1 = new RoomEntity("Room 1", RoomTypes.LIVING, 101, 102);
    RoomEntity entity2 = new RoomEntity("Room 2", RoomTypes.BEDROOMS, 201, 202);

    List<RoomEntity> entities = new ArrayList<>();
    entities.add(entity1);
    entities.add(entity2);

    repository.saveAllByUnitId(entities, savedUnitEntity.getId());

    repository.deleteByIdAndUnitId(entity1.getId(), savedUnitEntity.getId());
    assertThat(repository.count()).isEqualTo(2L);
    assertThat(repository.findById(entity1.getId())).isEmpty();
  }
}
