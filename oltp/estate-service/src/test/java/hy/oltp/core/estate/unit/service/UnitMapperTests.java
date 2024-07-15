package hy.oltp.core.estate.unit.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import hy.api.core.estate.buildings.Building;
import hy.api.core.estate.lease.Lease;
import hy.api.core.estate.unit.Room;
import hy.api.core.estate.unit.RoomTypes;
import hy.api.core.estate.unit.Unit;
import hy.api.core.estate.unit.UnitDetail;
import hy.api.core.estate.unit.UnitStatus;
import hy.api.core.estate.unit.UnitSummary;
import hy.oltp.core.estate.unit.persistence.RoomEntity;
import hy.oltp.core.estate.unit.persistence.UnitDetailEntity;
import hy.oltp.core.estate.unit.persistence.UnitEntity;
import hy.oltp.core.estate.unit.persistence.UnitSummaryEntity;

class UnitMapperTests {
  private final UnitMapper mapper = UnitMapper.INSTANCE;

  @Test
  void mapperTest() {

    Building building = new Building(1, "name", "address", "city", "state", "zip", "description");
    Set<Lease> leases = Set.of(new Lease(1, null), new Lease(2, null), new Lease(3, null));

    UnitSummary summary = new UnitSummary("b101", 1, UnitStatus.AVAILABLE);
    Set<Room> rooms = Set.of(new Room(1, "room1", RoomTypes.DINING, 100, 100),
      new Room(2, "room2", RoomTypes.LIVING, 100, 100), new Room(3, "room3", RoomTypes.KITCHEN, 100, 100));
    UnitDetail detail = new UnitDetail(30, new BigDecimal(10));
    detail.setRooms(rooms);

    Unit unit = new Unit(1, summary, detail);
    unit.setBuilding(building);
    unit.setLeases(leases);
    UnitEntity entity = mapper.apiToEntity(unit);

    assertThat(unit.getId()).isEqualTo(entity.getId());
    assertThat(unit.getBuilding()
      .getId()).isEqualTo(entity.getBuilding()
        .getId());
  }

  // @formatter:off
  void summaryMapperTest(UnitSummary api, UnitSummaryEntity entity) {
    assertThat(api.getFloorNumber()).isEqualTo(entity.getFloorNumber());
    assertThat(api.getStatus()).isEqualTo(entity.getStatus());
    assertThat(api.getUnitNumber()).isEqualTo(entity.getUnitNumber());

    UnitSummary api2 = mapper.entityToApi(entity);
    assertThat(api.getFloorNumber()).isEqualTo(api2.getFloorNumber());
    assertThat(api.getStatus()).isEqualTo(api2.getStatus());
    assertThat(api.getUnitNumber()).isEqualTo(api2.getUnitNumber());
  }

  void detailMapperTest(UnitDetail api, UnitDetailEntity entity) {
    assertThat(api.getRent()).isEqualTo(entity.getRent());
    assertThat(api.getTotalSquareFeet()).isEqualTo(entity.getTotalSquareFeet());

    IntStream.range(0, api.getRooms().size())
      .mapToObj(i -> Map.entry(api.getRoomById(i), entity.getRoomById(i)))
      .collect(Collectors.toList())
      .forEach(x -> roomMapperTest(x.getKey(), x.getValue()));

    UnitDetail api2 = mapper.entityToApi(entity);
    assertThat(api.getRent()).isEqualTo(api2.getRent());
    assertThat(api.getTotalSquareFeet()).isEqualTo(api2.getTotalSquareFeet());

    IntStream.range(0, api2.getRooms().size())
      .mapToObj(i -> Map.entry(api.getRoomById(i), api2.getRoomById(i)))
      .collect(Collectors.toList())
      .forEach(x -> roomMapperTest(x.getKey(), x.getValue()));
  }
  // @formatter:on

  // @formatter:off
  void roomMapperTest(Room api, RoomEntity entity) {
    assertThat(api.getId()).isEqualTo(entity.getId());
    assertThat(api.getUnit().getId()).isEqualTo(entity.getUnit().getId());
    assertThat(api.getName()).isEqualTo(entity.getName());
    assertThat(api.getType()).isEqualTo(entity.getType());
    assertThat(api.getSquareFeet()).isEqualTo(entity.getSquareFeet());
    assertThat(api.getWindowsCount()).isEqualTo(entity.getWindowsCount());
  }
  // @formatter:on

  // @formatter:off
  void roomMapperTest(Room api, Room api2) {
    assertThat(api.getId()).isEqualTo(api2.getId());
    assertThat(api.getUnit().getId()).isEqualTo(api2.getUnit().getId());
    assertThat(api.getName()).isEqualTo(api2.getName());
    assertThat(api.getType()).isEqualTo(api2.getType());
    assertThat(api.getSquareFeet()).isEqualTo(api2.getSquareFeet());
    assertThat(api.getWindowsCount()).isEqualTo(api2.getWindowsCount());
  }
  // @formatter:on

}
