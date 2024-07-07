package hy.api.core.estate.lease;

import hy.api.core.estate.tenant.Tenant;
import hy.api.core.estate.unit.Unit;

/**
 * Class represents a lease.
 *
 * @see LeaseDetail
 * @see Unit
 * @see Tenant
 */
public class Lease {
  private int id;
  private Unit unit;
  private Tenant tenant;
  private LeaseDetail leaseDetail;

  public Lease() {
    this(0, null, null, null);
  }

  public Lease(int id) {
    this(id, null, null, null);
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



}
