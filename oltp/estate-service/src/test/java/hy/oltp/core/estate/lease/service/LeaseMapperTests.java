package hy.oltp.core.estate.lease.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import hy.api.core.estate.lease.Lease;
import hy.api.core.estate.lease.LeaseDetail;
import hy.api.core.estate.lease.LeaseStatus;
import hy.api.core.estate.tenant.Tenant;
import hy.api.core.estate.unit.Unit;
import hy.oltp.core.estate.lease.persistence.LeaseDetailEntity;
import hy.oltp.core.estate.lease.persistence.LeaseEntity;

class LeaseMapperTests {
  private final LeaseMapper mapper = LeaseMapper.INSTANCE;

  // @formatter:off
  @Test
  void leaseMapperTest() {
    var now = Instant.now();
    var yesterday = now.minus(1, ChronoUnit.DAYS);
    LeaseDetail detail = new LeaseDetail(yesterday, now, 10.0, 10.0, LeaseStatus.ACTIVE);
    Tenant tenant = new Tenant(1, "firstName", "lastName", "email@email.com", "01012345678");
    Unit unit = new Unit();
    Lease lease = new Lease(1, unit, tenant, detail);

    LeaseEntity entity = mapper.apiToEntity(lease);
    assertThat(lease.getId()).isEqualTo(entity.getId());
    assertThat(lease.getUnit().getId()).isEqualTo(entity.getUnit().getId());
    assertThat(lease.getTenant().getId()).isEqualTo(entity.getTenant().getId());
    isDetailEqual(lease.getLeaseDetail(), entity.getLeaseDetail());

    Lease lease2 = mapper.entityToApi(entity);
    assertThat(lease.getId()).isEqualTo(lease2.getId());
    assertThat(lease.getUnit().getId()).isEqualTo(lease2.getUnit().getId());
    assertThat(lease.getTenant().getId()).isEqualTo(lease2.getTenant().getId());
    isDetailEqual(lease.getLeaseDetail(), lease2.getLeaseDetail());
  }
  // @formatter:on

  void isDetailEqual(LeaseDetail api, LeaseDetailEntity entity) {
    assertThat(api.getStartedAt()).isEqualTo(entity.getStartedAt());
    assertThat(api.getEndedAt()).isEqualTo(entity.getEndedAt());
    assertThat(api.getRentAmount()).isEqualTo(entity.getRentAmount());
    assertThat(api.getSecurityDeposit()).isEqualTo(entity.getSecurityDeposit());
    assertThat(api.getStatus()).isEqualTo(entity.getStatus());
  }

  void isDetailEqual(LeaseDetail api, LeaseDetail api2) {
    assertThat(api.getStartedAt()).isEqualTo(api2.getStartedAt());
    assertThat(api.getEndedAt()).isEqualTo(api2.getEndedAt());
    assertThat(api.getRentAmount()).isEqualTo(api2.getRentAmount());
    assertThat(api.getSecurityDeposit()).isEqualTo(api2.getSecurityDeposit());
    assertThat(api.getStatus()).isEqualTo(api2.getStatus());
  }
}
