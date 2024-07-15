package hy.oltp.core.estate.lease.persistence;

import jakarta.persistence.PreUpdate;

public class LeaseEntityListener {

  @PreUpdate
  public void onPreUpdate(LeaseEntity leaseEntity) {
      leaseEntity.setVersion(leaseEntity.getVersion() + 1);
  }
}