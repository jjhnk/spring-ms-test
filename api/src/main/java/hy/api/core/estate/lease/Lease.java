package hy.api.core.estate.lease;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import hy.api.core.estate.tenant.Tenant;
import hy.api.core.estate.unit.Unit;

/**
 * Class represents a lease.
 *
 * @see LeaseDetail
 * @see Unit
 * @see Tenant
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Lease.class)
@JsonInclude(Include.NON_NULL)
public class Lease {
  private int id;
  // @JsonBackReference(@JsonBackReference(value = "unit-lease"))
  private Unit unit;
  // @JsonBackReference(@JsonBackReference(value = "tenant-lease"))
  private Tenant tenant;
  private LeaseDetail leaseDetail;

  public Lease() {}

  public Lease(int id, LeaseDetail leaseDetail) {
    this(id, null, null, leaseDetail);
  }

  public Lease(int id, Unit unit, Tenant tenant, LeaseDetail leaseDetail) {
    this.id = id;
    this.unit = unit;
    this.tenant = tenant;
    this.leaseDetail = leaseDetail;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Unit getUnit() {
    return unit;
  }

  public void setUnit(Unit unit) {
    this.unit = unit;
  }

  public Tenant getTenant() {
    return tenant;
  }

  public void setTenant(Tenant tenant) {
    this.tenant = tenant;
  }

  public LeaseDetail getLeaseDetail() {
    return leaseDetail;
  }

  public void setLeaseDetail(LeaseDetail leaseDetails) {
    this.leaseDetail = leaseDetails;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, leaseDetail);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Lease other = (Lease) obj;
    return id == other.id && Objects.equals(leaseDetail, other.leaseDetail);
  }


  @Override
  // @formatter:off
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\"lease\":{");
    sb.append("\"id\":" + id);
    if (unit != null) {
      sb.append(",\"unit\"" + unit.toReferredString());
    }
    if (tenant != null) {
      sb.append(",\"tenant\"" + tenant.toReferredString());
    }
    if (leaseDetail != null) {
      sb.append(",\"detail\"" + leaseDetail.toString());
    }
    sb.append('}');
    return sb.toString();
  }
  // @formatter:on

  public String toReferedString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    sb.append("\"id\":" + id);
    if (leaseDetail != null) {
      sb.append(',' + leaseDetail.toString());
    }
    sb.append('}');
    return sb.toString();
  }
}
