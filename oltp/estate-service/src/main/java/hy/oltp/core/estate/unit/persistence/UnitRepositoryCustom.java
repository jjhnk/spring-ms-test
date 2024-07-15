package hy.oltp.core.estate.unit.persistence;

import java.util.List;

public interface UnitRepositoryCustom {
  List<UnitEntity> findByBuildingIdAndTenantId(int buildingId, int tenantId);

  List<UnitEntity> findByBuildingId(int buildingId);

  List<UnitEntity> findByTenantId(int tenantId);
}
