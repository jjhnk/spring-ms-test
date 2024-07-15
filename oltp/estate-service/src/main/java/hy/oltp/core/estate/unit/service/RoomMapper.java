package hy.oltp.core.estate.unit.service;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import hy.api.core.estate.unit.Room;
import hy.api.core.estate.unit.Unit;
import hy.api.core.estate.unit.UnitDetail;
import hy.oltp.core.estate.unit.persistence.RoomEntity;
import hy.oltp.core.estate.unit.persistence.UnitDetailEntity;
import hy.oltp.core.estate.unit.persistence.UnitEntity;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {

  RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

  Room entityToApi(RoomEntity entity);

  @Mapping(target = "version", ignore = true)
  RoomEntity apiToEntity(Room room);

  @Mapping(target = "id", ignore = true)
  void updateEntityFromApi(Room room, @MappingTarget RoomEntity entity);

  @Mapping(target = "building", ignore = true)
  @Mapping(target = "leases", ignore = true)
  Unit unitEntityToUnit(UnitEntity unitEntity);




  @Mapping(target = "rooms", ignore = true)
  UnitDetail unitDetailEntityToUnitDetail(UnitDetailEntity unitDetailEntity);


}
