package hy.api.core.property.lease;

import hy.api.core.property.tenant.Tenant;
import hy.api.core.property.unit.UnitSummary;

/**
 * Class represents a lease.
 *
 * @see LeaseDetails
 * @see UnitSummary
 * @see Tenant
 */
public class Lease {
  private int leaseId;
  private UnitSummary unitSummary;
  private Tenant tenant;
  private LeaseDetails leaseDetails;

  public Lease(int leaseId, UnitSummary unitSummary, Tenant tenant) {
    this.leaseId = leaseId;
    this.unitSummary = unitSummary;
    this.tenant = tenant;
  }

  public int getLeaseId() {
    return leaseId;
  }

  public void setLeaseId(int leaseId) {
    this.leaseId = leaseId;
  }

  public UnitSummary getUnitSummary() {
    return unitSummary;
  }

  public void setUnitSummary(UnitSummary unitSummary) {
    this.unitSummary = unitSummary;
  }

  public Tenant getTenant() {
    return tenant;
  }

  public void setTenant(Tenant tenant) {
    this.tenant = tenant;
  }

  public LeaseDetails getLeaseDetails() {
    return leaseDetails;
  }

  public void setLeaseDetails(LeaseDetails leaseDetails) {
    this.leaseDetails = leaseDetails;
  }

}
