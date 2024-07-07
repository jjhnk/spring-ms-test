package hy.oltp.core.estate.lease.persistence;

import org.springframework.data.annotation.Version;

import hy.oltp.core.estate.tenant.persistence.TenantEntity;
import hy.oltp.core.estate.unit.persistence.UnitEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "leases",
  indexes = {@Index(name = "leases_unique_idx", unique = true, columnList = "lease_id")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Version
  private int version;

  @ManyToOne
  @JoinColumn(name = "unit_id", nullable = false)
  private UnitEntity unit;

  @ManyToOne
  @JoinColumn(name = "tenant_id", nullable = false)
  private TenantEntity tenant;

  @Embedded
  private LeaseDetailEntity leaseDetail;

  public LeaseEntity(UnitEntity unit, TenantEntity tenant, LeaseDetailEntity leaseDetail) {
    this.unit = unit;
    this.tenant = tenant;
    this.leaseDetail = leaseDetail;
  }
}
