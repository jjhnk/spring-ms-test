package hy.oltp.core.estate.lease.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import hy.api.core.estate.lease.LeaseStatus;
import hy.api.core.estate.unit.UnitStatus;
import hy.oltp.core.estate.building.persistence.BuildingEntity;
import hy.oltp.core.estate.building.persistence.BuildingRepository;
import hy.oltp.core.estate.tenant.PostgreSqlTestBase;
import hy.oltp.core.estate.tenant.persistence.TenantEntity;
import hy.oltp.core.estate.tenant.persistence.TenantRepository;
import hy.oltp.core.estate.unit.persistence.UnitDetailEntity;
import hy.oltp.core.estate.unit.persistence.UnitEntity;
import hy.oltp.core.estate.unit.persistence.UnitRepository;
import hy.oltp.core.estate.unit.persistence.UnitSummaryEntity;

@DataJpaTest(properties = {"spring.jpa.hibernate.ddl-auto=update"})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LeasePersistenceTests extends PostgreSqlTestBase {

  @Autowired
  private LeaseRepository repository;
  @Autowired
  private BuildingRepository buildingRepository;
  @Autowired
  private UnitRepository unitRepository;
  @Autowired
  private TenantRepository tenantRepository;
  private LeaseEntity saved;
  private UnitEntity unitEntity;
  private BuildingEntity buildingEntity;
  private TenantEntity tenantEntity;

  private Instant endedAt = Instant.now();
  private Instant startedAt = Instant.now()
    .minus(1, ChronoUnit.DAYS);

  @BeforeEach
  void setupDb() {
    repository.deleteAll();
    tenantRepository.deleteAll();
    unitRepository.deleteAll();

    buildingEntity = new BuildingEntity("name", "address", "city", "state", "zipCode", "description");
    buildingRepository.save(buildingEntity);

    unitEntity = new UnitEntity(new UnitSummaryEntity("b101", 1, UnitStatus.AVAILABLE),
      new UnitDetailEntity(30, new BigDecimal(10)));
    unitEntity.setBuilding(buildingEntity);
    unitRepository.save(unitEntity);

    tenantEntity = new TenantEntity("firstName", "lastName", "email@email.com", "01012345678");
    tenantRepository.save(tenantEntity);

    LeaseEntity entity = new LeaseEntity(new LeaseDetailEntity(startedAt, endedAt, 100, 100, LeaseStatus.EXPIRED));
    entity.setTenant(tenantEntity);
    entity.setUnit(unitEntity);
    saved = repository.save(entity);

    assertThat(saved).isEqualTo(entity);
  }

  @Test
  void create() {
    LeaseEntity newEntity = new LeaseEntity(new LeaseDetailEntity(startedAt, endedAt, 100, 100, LeaseStatus.EXPIRED));
    newEntity.setTenant(tenantEntity);
    newEntity.setUnit(unitEntity);
    repository.save(newEntity);

    LeaseEntity found = repository.findById(newEntity.getId())
      .get();
    assertThat(found).isEqualTo(newEntity);
    assertThat(repository.count()).isEqualTo(2L);
  }

  @Test
  void update() {
    saved.getLeaseDetail()
      .setStatus(LeaseStatus.ACTIVE);
    repository.save(saved);

    LeaseEntity found = repository.findById(saved.getId())
      .get();
    assertThat(found.getVersion()).isOne();
    assertThat(found.getLeaseDetail()
      .getStatus()).isEqualTo(LeaseStatus.ACTIVE);
  }

  @Test
  void delete() {
    repository.delete(saved);
    assertThat(repository.existsById(saved.getId())).isFalse();
  }

  @Test
  void getByTenantId() {
    List<LeaseEntity> found = repository.findByTenantId(tenantEntity.getId());

    assertThat(found).hasSize(1);
    assertThat(saved).isEqualTo(found.get(0));
  }

  @Test
  void getByUnitId() {
    List<LeaseEntity> found = repository.findByUnitId(unitEntity.getId());

    assertThat(found).hasSize(1);
    assertThat(saved).isEqualTo(found.get(0));
  }

  @Test
  void getByTenantIdAndUnitId() {
    List<LeaseEntity> found1 = repository.findByTenantIdAndUnitId(tenantEntity.getId(), 0);
    List<LeaseEntity> found2 = repository.findByTenantIdAndUnitId(0, unitEntity.getId());
    List<LeaseEntity> found3 = repository.findByTenantIdAndUnitId(tenantEntity.getId(), unitEntity.getId());

    assertThat(found1).isEmpty();
    assertThat(found2).isEmpty();
    assertThat(found3).hasSize(1);
    assertThat(found3.get(0)).isEqualTo(saved);
  }

}
