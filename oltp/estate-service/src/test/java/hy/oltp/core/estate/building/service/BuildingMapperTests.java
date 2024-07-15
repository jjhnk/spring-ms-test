package hy.oltp.core.estate.building.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import hy.api.core.estate.buildings.Building;
import hy.api.core.estate.unit.Unit;
import hy.api.core.estate.unit.UnitDetail;
import hy.api.core.estate.unit.UnitStatus;
import hy.api.core.estate.unit.UnitSummary;
import hy.oltp.core.estate.building.persistence.BuildingEntity;

class BuildingMapperTests {
  private final BuildingMapper mapper = BuildingMapper.INSTANCE;

  // @formatter:off
  @Test
  void mapperTest() {
    assertThat(mapper).isNotNull();

    Set<Unit> units = Set.of(
      new Unit(1, new UnitSummary("b102", 2, UnitStatus.AVAILABLE), new UnitDetail(30, new BigDecimal(10))),
      new Unit(2, new UnitSummary("b103", 3, UnitStatus.OCCUPIED), new UnitDetail(60, new BigDecimal(20))));

    Building api = new Building(1, "name", "address", "city", "state", "zip", "decription");
    api.setUnits(units);
    BuildingEntity entity = mapper.apiToEntity(api);

    assertThat(api.getId()).isEqualTo(entity.getId());
    assertThat(api.getName()).isEqualTo(entity.getName());
    assertThat(api.getAddress()).isEqualTo(entity.getAddress());
    assertThat(api.getCity()).isEqualTo(entity.getCity());
    assertThat(api.getState()).isEqualTo(entity.getState());
    assertThat(api.getZipCode()).isEqualTo(entity.getZipCode());
    assertThat(api.getDescription()).isEqualTo(entity.getDescription());

    var apiList = api.getUnits().stream().collect(Collectors.toList());
    apiList.sort((x, y) -> x.getId() - y.getId());
    var entityList = entity.getUnits().stream().collect(Collectors.toList());
    entityList.sort((x, y) -> x.getId() - y.getId());
    IntStream.range(0, apiList.size())
      .mapToObj(i -> Map.entry(apiList.get(i), entityList.get(i)))
      .collect(Collectors.toList())
      .forEach(x -> assertThat(x.getKey().getId()).isEqualTo(x.getValue().getId()));

    Building api2 = mapper.entityToApi(entity);
    assertThat(api.getId()).isEqualTo(api2.getId());
    assertThat(api.getName()).isEqualTo(api2.getName());
    assertThat(api.getAddress()).isEqualTo(api2.getAddress());
    assertThat(api.getCity()).isEqualTo(api2.getCity());
    assertThat(api.getState()).isEqualTo(api2.getState());
    assertThat(api.getZipCode()).isEqualTo(api2.getZipCode());
    assertThat(api.getDescription()).isEqualTo(api2.getDescription());

    var api2List = api2.getUnits().stream().collect(Collectors.toList());
    api2List.sort((x, y) -> x.getId() - y.getId());
    IntStream.range(0, apiList.size())
      .mapToObj(i -> Map.entry(apiList.get(i), api2List.get(i)))
      .collect(Collectors.toList())
      .forEach(x -> assertThat(x.getKey().getId()).isEqualTo(x.getValue().getId()));
  }
  // @formatter:on
}
