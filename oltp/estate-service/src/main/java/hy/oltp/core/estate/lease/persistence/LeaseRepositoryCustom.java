package hy.oltp.core.estate.lease.persistence;

import java.util.List;

public interface LeaseRepositoryCustom {
  List<LeaseEntity> findByTenantId(int tenantId);

  List<LeaseEntity> findByUnitId(int unitId);

  List<LeaseEntity> findByTenantIdOrUnitId(int tenantId, int unitId);
}
