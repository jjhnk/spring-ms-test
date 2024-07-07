package hy.oltp.core.estate.tenant.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
import hy.oltp.core.estate.unit.persistence.UnitDetailEntity;
import hy.oltp.core.estate.unit.persistence.UnitEntity;
import hy.oltp.core.estate.unit.persistence.UnitRepository;
import hy.oltp.core.estate.unit.persistence.UnitSummaryEntity;

@DataJpaTest(properties = {"spring.jpa.hibernate.ddl-auto=update"})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TenantPersistenceTests extends PostgreSqlTestBase {
  private static final Logger LOG = LoggerFactory.getLogger(TenantPersistenceTests.class);
  @Autowired
  private BuildingRepository buildingRepository;
  @Autowired
  private TenantRepository tenantRepository;
  @Autowired
  private LeaseRepository leaseRepository;
  @Autowired
  private UnitRepository unitRepository;

  private TenantEntity savedTenantEntity;

  @BeforeEach
  void setupDb() {
    buildingRepository.deleteAll();
    tenantRepository.deleteAll();
    leaseRepository.deleteAll();
    unitRepository.deleteAll();

    BuildingEntity buildingEntity = new BuildingEntity("name", "address", "city", "state", "zip");
    buildingEntity = buildingRepository.save(buildingEntity);

    UnitSummaryEntity unitSummaryEntity = new UnitSummaryEntity("b101", 1, UnitStatus.AVAILABLE);
    UnitDetailEntity unitDetailEntity = new UnitDetailEntity(30, null, new BigDecimal(10));

    UnitEntity unitEntity = new UnitEntity(buildingEntity, unitSummaryEntity, unitDetailEntity);
    unitEntity.setBuilding(buildingEntity);
    unitRepository.save(unitEntity);

    TenantEntity tenantEntity = new TenantEntity("firstName", "lastName", "email@email.com", "01012345678");
    savedTenantEntity = tenantRepository.save(tenantEntity);

    LeaseDetailEntity leaseDetail = new LeaseDetailEntity(new Date(), new Date(), 100, 100, LeaseStatus.ACTIVE);
    LeaseEntity leaseEntity1 = new LeaseEntity(unitEntity, tenantEntity, leaseDetail);
    LeaseEntity leaseEntity2 = new LeaseEntity(unitEntity, tenantEntity, leaseDetail);
    leaseEntity1.setUnit(unitEntity);
    leaseEntity2.setUnit(unitEntity);
    leaseEntity1.setTenant(tenantEntity);
    leaseEntity2.setTenant(tenantEntity);

    Set<LeaseEntity> leases = new HashSet<>();
    leases.add(leaseEntity1);
    leases.add(leaseEntity2);

    tenantEntity.setLeases(leases);
    unitEntity.setLeases(leases);
  }

  @Test
  @Transactional
  void create() {
    BuildingEntity buildingEntity = new BuildingEntity("name2", "address2", "city2", "state2", "zip2");
    buildingEntity = buildingRepository.save(buildingEntity);

    UnitSummaryEntity unitSummaryEntity = new UnitSummaryEntity("b102", 1, UnitStatus.AVAILABLE);
    UnitDetailEntity unitDetailEntity = new UnitDetailEntity(40, null, new BigDecimal(20));

    UnitEntity unitEntity = new UnitEntity(buildingEntity, unitSummaryEntity, unitDetailEntity);
    unitEntity.setBuilding(buildingEntity);
    unitRepository.save(unitEntity);

    TenantEntity tenantEntity = new TenantEntity("firstName2", "lastName2", "email2@email.com", "01012345679");
    savedTenantEntity = tenantRepository.save(tenantEntity);

    LeaseDetailEntity leaseDetail = new LeaseDetailEntity(new Date(), new Date(), 200, 200, LeaseStatus.ACTIVE);
    LeaseEntity leaseEntity1 = new LeaseEntity(unitEntity, tenantEntity, leaseDetail);
    LeaseEntity leaseEntity2 = new LeaseEntity(unitEntity, tenantEntity, leaseDetail);
    leaseEntity1.setUnit(unitEntity);
    leaseEntity2.setUnit(unitEntity);
    leaseEntity1.setTenant(tenantEntity);
    leaseEntity2.setTenant(tenantEntity);

    Set<LeaseEntity> leases = new HashSet<>();
    leases.add(leaseEntity1);
    leases.add(leaseEntity2);

    tenantEntity.setLeases(leases);
    unitEntity.setLeases(leases);

    TenantEntity saved = tenantRepository.findById(savedTenantEntity.getId()).get();

    assertThat(saved).isNotNull();
    assertThat(saved.getId()).isEqualTo(tenantEntity.getId());
    assertThat(saved.getFirstName()).isEqualTo(tenantEntity.getFirstName());
    assertThat(saved.getLastName()).isEqualTo(tenantEntity.getLastName());
    assertThat(saved.getEmail()).isEqualTo(tenantEntity.getEmail());
    assertThat(saved.getPhoneNumber()).isEqualTo(tenantEntity.getPhoneNumber());
    saved.getLeases().forEach(lease -> assertThat(lease).isIn(leases));
    assertThat(tenantRepository.count()).isEqualTo(2);
  }



}
