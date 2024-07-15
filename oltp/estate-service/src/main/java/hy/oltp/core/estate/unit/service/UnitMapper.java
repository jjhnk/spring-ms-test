package hy.oltp.core.estate.unit.service;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import hy.api.core.estate.buildings.Building;
import hy.api.core.estate.lease.Lease;
import hy.api.core.estate.unit.Room;
import hy.api.core.estate.unit.Unit;
import hy.api.core.estate.unit.UnitDetail;
import hy.api.core.estate.unit.UnitSummary;
import hy.oltp.core.estate.building.persistence.BuildingEntity;
import hy.oltp.core.estate.lease.persistence.LeaseEntity;
import hy.oltp.core.estate.unit.persistence.RoomEntity;
import hy.oltp.core.estate.unit.persistence.UnitDetailEntity;
import hy.oltp.core.estate.unit.persistence.UnitEntity;
import hy.oltp.core.estate.unit.persistence.UnitSummaryEntity;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UnitMapper {
  UnitMapper INSTANCE = Mappers.getMapper(UnitMapper.class);

  Unit entityToApi(UnitEntity entity);

  @Mapping(target = "version", ignore = true)
  UnitEntity apiToEntity(Unit unit);

  @Mapping(target = "version", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "detail", qualifiedByName = "updateDetailEntityFromApi")
  void updateEntityFromApi(Unit unit, @MappingTarget UnitEntity entity);

  UnitDetailEntity apiToEntity(UnitDetail unitDetail);

  UnitDetail entityToApi(UnitDetailEntity entity);

  @Named("updateDetailEntityFromApi")
  void updateEntityFromApi(UnitDetail unitDetail, @MappingTarget UnitDetailEntity entity);

  UnitSummaryEntity apiToEntity(UnitSummary unitSummary);

  UnitSummary entityToApi(UnitSummaryEntity unitSummaryEntity);

  UnitSummary toUnitSummary(UnitEntity entity);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id")
  Building buildingEntityToBuilding(BuildingEntity buildingEntity);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id")
  Lease leaseEntityToLease(LeaseEntity leaseEntity);

  @Mapping(target = "unit", ignore = true)
  Room roomEntityToRoom(RoomEntity roomEntity);
}
