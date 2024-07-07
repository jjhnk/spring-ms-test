package hy.oltp.core.estate.building.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hy.api.core.estate.buildings.Building;
import hy.api.core.estate.unit.Unit;
import hy.api.core.estate.unit.UnitDetail;
import hy.api.core.estate.unit.UnitStatus;
import hy.api.core.estate.unit.UnitSummary;
import hy.oltp.core.estate.building.persistence.BuildingEntity;
import hy.oltp.core.estate.unit.service.UnitMapper;

class BuildingMapperTests {
  private final BuildingMapper mapper = BuildingMapper.INSTANCE;
  private final UnitMapper unitMapper = UnitMapper.INSTANCE;
  private static final Logger LOG = LoggerFactory.getLogger(BuildingMapperTests.class);

  @Test
  void mapperTest() {
    assertThat(mapper).isNotNull();

    Set<Unit> units = new HashSet<>();
    var summary = new UnitSummary("b102", 2, UnitStatus.AVAILABLE);
    var detail = new UnitDetail(30, new BigDecimal(10), null);
    var unit = new Unit(1, null, null, summary, detail);
    units.add(unit);

    Building api = new Building(1, units, "name", "address", "city", "state", "zip", "decription");
    BuildingEntity entity = mapper.apiToEntity(api);

    assertThat(api.getId()).isEqualTo(entity.getId());
    assertThat(api.getName()).isEqualTo(entity.getName());
    assertThat(api.getAddress()).isEqualTo(entity.getAddress());
    assertThat(api.getCity()).isEqualTo(entity.getCity());
    assertThat(api.getState()).isEqualTo(entity.getState());
    assertThat(api.getZipCode()).isEqualTo(entity.getZipCode());
    assertThat(api.getDescription()).isEqualTo(entity.getDescription());
    var target = entity.getUnits().stream().map(unitMapper::entityToApi).collect(Collectors.toSet());
    assertThat(api.getUnits()).containsExactlyInAnyOrderElementsOf(target);

    Building api2 = mapper.entityToApi(entity);
    assertThat(api.getId()).isEqualTo(api2.getId());
    assertThat(api.getName()).isEqualTo(api2.getName());
    assertThat(api.getAddress()).isEqualTo(api2.getAddress());
    assertThat(api.getCity()).isEqualTo(api2.getCity());
    assertThat(api.getState()).isEqualTo(api2.getState());
    assertThat(api.getZipCode()).isEqualTo(api2.getZipCode());
    assertThat(api.getDescription()).isEqualTo(api2.getDescription());
  }

  @Test
  void testEntityToApi() {
    Building api = new Building(1, null, "name", "address", "city", "state", "zip", "decription");
    BuildingEntity entity = new BuildingEntity(2, 2, null, "", "", "", "", "", "");
    mapper.updateEntityFromApi(api, entity);

    assertThat(api.getId()).isEqualTo(entity.getId());
    assertThat(api.getName()).isEqualTo(entity.getName());
    assertThat(api.getAddress()).isEqualTo(entity.getAddress());
    assertThat(api.getCity()).isEqualTo(entity.getCity());
    assertThat(api.getState()).isEqualTo(entity.getState());
    assertThat(api.getZipCode()).isEqualTo(entity.getZipCode());
    assertThat(api.getDescription()).isEqualTo(entity.getDescription());
  }
}
