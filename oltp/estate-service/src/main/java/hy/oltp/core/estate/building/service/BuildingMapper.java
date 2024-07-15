package hy.oltp.core.estate.building.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import hy.api.core.estate.buildings.Building;
import hy.api.core.estate.unit.Unit;
import hy.oltp.core.estate.building.persistence.BuildingEntity;
import hy.oltp.core.estate.unit.persistence.UnitEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BuildingMapper {
  BuildingMapper INSTANCE = Mappers.getMapper(BuildingMapper.class);

  Building entityToApi(BuildingEntity dto);

  @Mapping(target = "version", ignore = true)
  BuildingEntity apiToEntity(Building api);

  @Mapping(target = "version", ignore = true)
  @Mapping(target = "id", ignore = true)
  void updateEntityFromApi(Building api, @MappingTarget BuildingEntity entity);


  @Mapping(target = "building", ignore = true)
  @Mapping(target = "leases", ignore = true)
  Unit unitEntityToUnit(UnitEntity unitEntity);
}
