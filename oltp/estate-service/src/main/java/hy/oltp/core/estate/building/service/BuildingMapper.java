package hy.oltp.core.estate.building.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import hy.api.core.estate.buildings.Building;
import hy.api.core.estate.unit.Unit;
import hy.oltp.core.estate.building.persistence.BuildingEntity;
import hy.oltp.core.estate.unit.persistence.UnitEntity;
import hy.oltp.core.estate.unit.service.UnitMapper;

@Mapper(componentModel = "spring")
public interface BuildingMapper {
  BuildingMapper INSTANCE = Mappers.getMapper(BuildingMapper.class);

  @Named("entityToApi")
  Building entityToApi(BuildingEntity dto);

  @Named("entityToApiWithout")
  @Mapping(target = "units", ignore = true)
  Building entityToApiWithout(BuildingEntity dto);

  @Named("apiToEntity")
  @Mapping(target = "version", ignore = true)
  BuildingEntity apiToEntity(Building api);

  @Named("apiToEntityWithout")
  @Mapping(target = "units", ignore = true)
  @Mapping(target = "version", ignore = true)
  BuildingEntity apiToEntityWithout(Building api);

  @Mapping(target = "version", ignore = true)
  void updateEntityFromApi(Building api, @MappingTarget BuildingEntity entity);

  @Mapping(target = "units", ignore = true)
  @Mapping(target = "version", ignore = true)
  void updateEntityFromApiWithOutUnit(Building api, @MappingTarget BuildingEntity entity);

  @AfterMapping
  default Set<UnitEntity> unitApiToEntity(@MappingTarget Set<Unit> unit) {
    return unit.stream()
      .map(UnitMapper.INSTANCE::apiToEntityWithout)
      .collect(Collectors.toSet());
  }

  default Set<Unit> unitEntityToApi(@MappingTarget Set<UnitEntity> unitEntity) {
    return unitEntity.stream()
      .map(UnitMapper.INSTANCE::entityToApiWithout)
      .collect(Collectors.toSet());
  }
}
