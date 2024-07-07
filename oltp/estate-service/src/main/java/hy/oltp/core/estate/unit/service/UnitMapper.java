package hy.oltp.core.estate.unit.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import hy.api.core.estate.buildings.Building;
import hy.api.core.estate.lease.Lease;
import hy.api.core.estate.tenant.Tenant;
import hy.api.core.estate.unit.Room;
import hy.api.core.estate.unit.Unit;
import hy.api.core.estate.unit.UnitDetail;
import hy.api.core.estate.unit.UnitSummary;
import hy.oltp.core.estate.building.persistence.BuildingEntity;
import hy.oltp.core.estate.building.service.BuildingMapper;
import hy.oltp.core.estate.lease.persistence.LeaseEntity;
import hy.oltp.core.estate.lease.service.LeaseMapper;
import hy.oltp.core.estate.tenant.persistence.TenantEntity;
import hy.oltp.core.estate.tenant.service.TenantMapper;
import hy.oltp.core.estate.unit.persistence.RoomEntity;
import hy.oltp.core.estate.unit.persistence.UnitDetailEntity;
import hy.oltp.core.estate.unit.persistence.UnitEntity;
import hy.oltp.core.estate.unit.persistence.UnitSummaryEntity;

@Mapper(componentModel = "spring")
public interface UnitMapper {
  UnitMapper INSTANCE = Mappers.getMapper(UnitMapper.class);

  @Named("entityToApi")
  Unit entityToApi(UnitEntity entity);

  @Named("entityToApiWithout")
  @Mapping(target = "building", ignore = true)
  @Mapping(target = "leases", ignore = true)
  Unit entityToApiWithout(UnitEntity entity);

  @Named("apiToEntity")
  @Mapping(target = "version", ignore = true)
  UnitEntity apiToEntity(Unit unit);

  @Named("apiToEntityWithout")
  @Mapping(target = "building", ignore = true)
  @Mapping(target = "leases", ignore = true)
  @Mapping(target = "version", ignore = true)
  UnitEntity apiToEntityWithout(Unit unit);

  UnitDetailEntity apiToEntity(UnitDetail unitDetail);

  UnitDetail entityToApi(UnitDetailEntity entity);

  UnitSummaryEntity apiToEntity(UnitSummary unitSummary);

  UnitSummary entityToApi(UnitSummaryEntity unitSummaryEntity);

  @Named("entityToApi")
  Room entityToApi(RoomEntity entity);

  @Named("entityToApiWithout")
  @Mapping(target = "unit", ignore = true)
  Room entityToApiWithout(RoomEntity entity);

  @Named("apiToEntity")
  @Mapping(target = "version", ignore = true)
  RoomEntity apiToEntity(Room room);

  @Named("apiToEntityWithout")
  @Mapping(target = "unit", ignore = true)
  @Mapping(target = "version", ignore = true)
  RoomEntity apiToEntityWithout(Room room);

  @Mapping(source = "summary.unitNumber", target = "unitNumber")
  @Mapping(source = "summary.floorNumber", target = "floorNumber")
  @Mapping(source = "summary.status", target = "status")
  UnitSummary toUnitSummary(UnitEntity entity);

  default Tenant tenantEntityToApi(TenantEntity entity) {
    return TenantMapper.INSTANCE.entityToApi(entity);
  }

  default TenantEntity tenantApiToEntity(Tenant tenant) {
    return TenantMapper.INSTANCE.apiToEntity(tenant);
  }

  default Set<Lease> leaseEntitiesToApis(Set<LeaseEntity> entities) {
    return entities.stream()
      .map(LeaseMapper.INSTANCE::entityToApi)
      .collect(Collectors.toSet());
  }

  default Set<LeaseEntity> leaseApisToEntities(Set<Lease> leases) {
    return leases.stream()
      .map(LeaseMapper.INSTANCE::apiToEntity)
      .collect(Collectors.toSet());
  }

  default Building buildingEntityToApi(BuildingEntity entity) {
    return BuildingMapper.INSTANCE.entityToApi(entity);
  }

  default BuildingEntity buildingApiToEntity(Building building) {
    return BuildingMapper.INSTANCE.apiToEntity(building);
  }

  default Room roomEntityToApi(RoomEntity entity) {
    return UnitMapper.INSTANCE.entityToApi(entity);
  }

  default RoomEntity roomApiToEntity(Room room) {
    return UnitMapper.INSTANCE.apiToEntity(room);
  }
}
