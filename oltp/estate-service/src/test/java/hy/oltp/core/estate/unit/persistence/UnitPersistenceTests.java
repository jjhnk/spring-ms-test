package hy.oltp.core.estate.unit.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import hy.api.core.estate.lease.LeaseStatus;
import hy.api.core.estate.unit.UnitStatus;
import hy.oltp.core.estate.building.persistence.BuildingEntity;
import hy.oltp.core.estate.building.persistence.BuildingRepository;
import hy.oltp.core.estate.lease.persistence.LeaseDetailEntity;
import hy.oltp.core.estate.lease.persistence.LeaseEntity;
import hy.oltp.core.estate.lease.persistence.LeaseRepository;
import hy.oltp.core.estate.tenant.PostgreSqlTestBase;
import hy.oltp.core.estate.tenant.persistence.TenantEntity;
import hy.oltp.core.estate.tenant.persistence.TenantRepository;

@DataJpaTest(properties = {"spring.jpa.hibernate.ddl-auto=update"})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UnitPersistenceTests extends PostgreSqlTestBase {
  @Autowired
  private UnitRepository repository;
  @Autowired
  private BuildingRepository buildingRepository;
  @Autowired
  private TenantRepository tenantRepository;
  @Autowired
  private LeaseRepository leaseRepository;

  private UnitEntity saved;
  private BuildingEntity buildingEntity;
  private TenantEntity tenantEntity;
  private LeaseEntity leaseEntity;


  @BeforeEach
  void setupDb() {
    repository.deleteAll();
    buildingRepository.deleteAll();
    tenantRepository.deleteAll();
    leaseRepository.deleteAll();

    buildingEntity = new BuildingEntity("name", "address", "city", "state", "zipCode", "description");
    buildingRepository.save(buildingEntity);

    tenantEntity = new TenantEntity("firstName", "lastName", "email@email.com", "01012345678");
    tenantRepository.save(tenantEntity);

    UnitEntity entity = new UnitEntity(new UnitSummaryEntity("b101", 1, UnitStatus.AVAILABLE),
      new UnitDetailEntity(30, new BigDecimal(10)));
    entity.setBuilding(buildingEntity);
    saved = repository.save(entity);

    leaseEntity = new LeaseEntity(new LeaseDetailEntity(Instant.now(), Instant.now(), 100, 100, LeaseStatus.ACTIVE));
    leaseEntity.setTenant(tenantEntity);
    leaseEntity.setUnit(entity);
    leaseRepository.save(leaseEntity);

    assertThat(saved).isEqualTo(entity);
  }

  @Test
  void create() {
    UnitEntity newEntity = new UnitEntity(new UnitSummaryEntity("b102", 1, UnitStatus.AVAILABLE),
      new UnitDetailEntity(30, new BigDecimal(10)));
    newEntity.setBuilding(buildingEntity);
    repository.save(newEntity);

    UnitEntity found = repository.findById(newEntity.getId()).get();
    assertThat(found).isEqualTo(newEntity);
    assertThat(repository.count()).isEqualTo(2L);
  }

  @Test
  void update() {
    saved.getSummary().setUnitNumber("b102");
    repository.save(saved);

    UnitEntity found = repository.findById(saved.getId()).get();
    assertThat(found.getVersion()).isOne();
    assertThat(found.getSummary().getUnitNumber()).isEqualTo("b102");

    found.getDetail().setTotalSquareFeet(40);
    repository.save(found);

    UnitEntity found2 = repository.findById(found.getId()).get();
    assertThat(found2.getVersion()).isEqualTo(2L);
    assertThat(found2.getDetail().getTotalSquareFeet()).isEqualTo(40);
  }

  @Test
  void delete() {
    repository.deleteById(saved.getId());
    assertThat(repository.existsById(saved.getId())).isFalse();
  }

  @Test
  void optimisticLockError() {
    UnitEntity entity1 = repository.findById(saved.getId()).get();
    UnitEntity entity2 = repository.findById(saved.getId()).get();

    entity1.getSummary().setUnitNumber("b102");
    repository.save(entity1);

    entity2.getSummary().setUnitNumber("b102");
    assertThrows(ObjectOptimisticLockingFailureException.class, () -> repository.save(entity2));

    UnitEntity updated = repository.findById(saved.getId()).get();
    assertThat(updated.getVersion()).isOne();
    assertThat(updated.getSummary().getUnitNumber()).isEqualTo("b102");
  }

  @Test
  void getByBuildingId() {
    BuildingEntity buildingEntity2 = new BuildingEntity("name2", "address2", "city2", "state2", "zipCode2", "description2");
    buildingRepository.save(buildingEntity2);

    TenantEntity tenantEntity2 = new TenantEntity("firstNam2e", "lastName2", "email@email2.com", "02012345678");
    tenantRepository.save(tenantEntity2);

    UnitEntity entity2 = new UnitEntity(new UnitSummaryEntity("b102", 2, UnitStatus.AVAILABLE),
      new UnitDetailEntity(30, new BigDecimal(10)));
    entity2.setBuilding(buildingEntity2);
    repository.save(entity2);

    LeaseEntity leaseEntity2 = new LeaseEntity(new LeaseDetailEntity(Instant.now(), Instant.now(), 102, 102, LeaseStatus.ACTIVE));
    leaseEntity2.setTenant(tenantEntity2);
    leaseEntity2.setUnit(saved);
    leaseRepository.save(leaseEntity2);

    List<UnitEntity> found = repository.findByBuildingId(saved.getBuilding().getId());

    assertThat(repository.count()).isEqualTo(2L);
    assertThat(found).hasSize(1);
    assertThat(found.get(0)).isEqualTo(saved);
  }

  @Test
  void getByTenantId() {
    BuildingEntity buildingEntity2 = new BuildingEntity("name2", "address2", "city2", "state2", "zipCode2", "description2");
    buildingRepository.save(buildingEntity2);

    TenantEntity tenantEntity2 = new TenantEntity("firstNam2e", "lastName2", "email@email2.com", "02012345678");
    tenantRepository.save(tenantEntity2);

    UnitEntity entity2 = new UnitEntity(new UnitSummaryEntity("b102", 2, UnitStatus.AVAILABLE),
      new UnitDetailEntity(30, new BigDecimal(10)));
    entity2.setBuilding(buildingEntity2);
    repository.save(entity2);

    LeaseEntity leaseEntity2 = new LeaseEntity(new LeaseDetailEntity(Instant.now(), Instant.now(), 102, 102, LeaseStatus.ACTIVE));
    leaseEntity2.setTenant(tenantEntity2);
    leaseEntity2.setUnit(saved);
    leaseRepository.save(leaseEntity2);

    List<UnitEntity> found = repository.findByTenantId(tenantEntity.getId());

    assertThat(repository.count()).isEqualTo(2L);
    assertThat(found).hasSize(1);
    assertThat(found.get(0)).isEqualTo(saved);
  }

}
