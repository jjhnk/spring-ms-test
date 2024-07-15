package hy.oltp.core.estate.lease.persistence;

import java.util.Objects;

import org.springframework.data.annotation.Version;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import hy.oltp.core.estate.tenant.persistence.TenantEntity;
import hy.oltp.core.estate.unit.persistence.UnitEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "leases", indexes = {@Index(name = "leases_unique_idx", unique = true, columnList = "lease_id")})
@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@EntityListeners(LeaseEntityListener.class)
public class LeaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Version
  private int version;

  @ManyToOne
  @JoinColumn(name = "unit_id", nullable = false)
  // @JsonBackReference
  private UnitEntity unit;

  @ManyToOne
  @JoinColumn(name = "tenant_id", nullable = false)
  // @JsonBackReference
  private TenantEntity tenant;

  @Embedded
  private LeaseDetailEntity leaseDetail;

  public LeaseEntity(LeaseDetailEntity leaseDetail) {
    this.leaseDetail = leaseDetail;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, leaseDetail);
  }

  // @formatter:off
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    LeaseEntity other = (LeaseEntity) obj;
    return id == other.id && leaseDetail.equals(other.leaseDetail);
  }
  // @formatter:on
}
