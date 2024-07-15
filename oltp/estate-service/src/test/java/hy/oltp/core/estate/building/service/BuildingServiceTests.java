package hy.oltp.core.estate.building.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;

import hy.api.core.estate.buildings.Building;
import hy.api.exceptions.NotFoundException;
import hy.oltp.core.estate.building.persistence.BuildingEntity;
import hy.oltp.core.estate.building.persistence.BuildingRepository;
import hy.oltp.core.estate.common.service.CacheUtility;

class BuildingServiceTests {

  @Mock
  private BuildingRepository repository;

  @Mock
  private CacheUtility<BuildingEntity> cacheUtility;

  @Mock
  private BuildingMapper mapper;

  @InjectMocks
  private BuildingService buildingService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreateBuilding() {
    Building building = new Building(1, "name", "address", "city", "state", "zipCode", "description");
    BuildingEntity buildingEntity = new BuildingEntity("name", "address", "city", "state", "zipCode", "description");
    when(mapper.apiToEntity(building)).thenReturn(buildingEntity);
    when(repository.save(buildingEntity)).thenReturn(buildingEntity);
    when(mapper.entityToApi(buildingEntity)).thenReturn(building);

    Building result = buildingService.createBuilding(building);

    verify(repository, times(1)).save(buildingEntity);
    assertThat(building).isEqualTo(result);
  }

  @Test
  void testGetBuildingWithCache() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;
    BuildingEntity buildingEntity = new BuildingEntity();
    Building building = new Building(1, "name", "address", "city", "state", "zipCode", "description");

    when(cacheUtility.safeGetFromCache(headers, "buildings:", id)).thenReturn(buildingEntity);
    when(mapper.entityToApi(buildingEntity)).thenReturn(building);

    Building result = buildingService.getBuilding(headers, id);

    verify(cacheUtility, times(1)).safeGetFromCache(headers, "buildings:", id);
    verify(mapper, times(1)).entityToApi(buildingEntity);
    assertThat(building).isEqualTo(result);
  }

  @Test
  void testGetBuildingWithRepository() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;
    BuildingEntity buildingEntity = new BuildingEntity();
    Building building = new Building(1, "name", "address", "city", "state", "zipCode", "description");

    when(cacheUtility.safeGetFromCache(headers, "buildings:", id)).thenReturn(null);
    when(repository.findById(id)).thenReturn(Optional.of(buildingEntity));
    when(mapper.entityToApi(buildingEntity)).thenReturn(building);

    Building result = buildingService.getBuilding(headers, id);

    verify(repository, times(1)).findById(id);
    verify(mapper, times(1)).entityToApi(buildingEntity);
    assertThat(building).isEqualTo(result);
  }

  @Test
  void testGetBuildingNotFound() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;

    when(cacheUtility.safeGetFromCache(headers, "buildings:", id)).thenReturn(null);
    when(repository.findById(id)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> buildingService.getBuilding(headers, id));

    verify(repository, times(1)).findById(id);
  }

  @Test
  void testGetBuildingsWithCache() {
    HttpHeaders headers = new HttpHeaders();
    List<BuildingEntity> buildingEntities =
      Arrays.asList(new BuildingEntity("name", "address", "city", "state", "zipCode", "description"),
        new BuildingEntity("name2", "address2", "city2", "state2", "zipCode2", "description2"));
    List<Building> buildings =
      Arrays.asList(new Building(1, "name", "address", "city", "state", "zipCode", "description"),
        new Building(2, "name2", "address2", "city2", "state2", "zipCode2", "description2"));

    when(cacheUtility.safeGetListFromCache(headers, "buildings:")).thenReturn(buildingEntities);
    when(mapper.entityToApi(any(BuildingEntity.class))).thenReturn(buildings.get(0), buildings.get(1));

    List<Building> result = buildingService.getBuildings(headers);

    verify(mapper, times(buildingEntities.size())).entityToApi(any(BuildingEntity.class));
    assertThat(buildings).hasSameElementsAs(result);
  }

  @Test
  void testGetBuildingsWithRepository() {
    HttpHeaders headers = new HttpHeaders();
    List<BuildingEntity> buildingEntities =
      Arrays.asList(new BuildingEntity("name", "address", "city", "state", "zipCode", "description"),
        new BuildingEntity("name2", "address2", "city2", "state2", "zipCode2", "description2"));
    List<Building> buildings =
      Arrays.asList(new Building(1, "name", "address", "city", "state", "zipCode", "description"),
        new Building(2, "name2", "address2", "city2", "state2", "zipCode2", "description2"));

    when(cacheUtility.safeGetListFromCache(headers, "buildings:")).thenReturn(null);
    when(repository.findAll()).thenReturn(buildingEntities);
    when(mapper.entityToApi(any(BuildingEntity.class))).thenReturn(buildings.get(0), buildings.get(1));

    List<Building> result = buildingService.getBuildings(headers);

    verify(mapper, times(buildingEntities.size())).entityToApi(any(BuildingEntity.class));
    verify(repository, times(1)).findAll();
    assertThat(buildings).hasSameElementsAs(result);
  }

  @Test
  void testGetBuildingsNotFound() {
    HttpHeaders headers = new HttpHeaders();

    when(cacheUtility.safeGetListFromCache(headers, "buildings:")).thenReturn(null);
    when(repository.findAll()).thenReturn(List.of());

    List<Building> result = buildingService.getBuildings(headers);

    verify(repository, times(1)).findAll();
    assertThat(result).isEmpty();
  }

  @Test
  void testUpdateBuilding() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;
    Building building = new Building(1, "name", "address", "city", "state", "zipCode", "description");
    BuildingEntity buildingEntity = new BuildingEntity();

    when(cacheUtility.safeGetFromCache(headers, "buildings:", id)).thenReturn(buildingEntity);
    doNothing().when(mapper)
      .updateEntityFromApi(building, buildingEntity);
    when(repository.save(buildingEntity)).thenReturn(buildingEntity);
    when(mapper.entityToApi(buildingEntity)).thenReturn(building);

    Building result = buildingService.updateBuilding(headers, id, building);

    verify(repository, times(1)).save(buildingEntity);
    verify(mapper, times(1)).entityToApi(buildingEntity);
    assertThat(building).isEqualTo(result);
  }

  @Test
  void testDeleteBuilding() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;
    BuildingEntity buildingEntity = new BuildingEntity();

    when(cacheUtility.safeGetFromCache(headers, "buildings:", id)).thenReturn(buildingEntity);

    buildingService.deleteBuilding(headers, id);

    verify(repository, times(1)).delete(buildingEntity);
    verify(cacheUtility, times(1)).safeRemoveFromCache(headers, "buildings:" + id);
  }

  @Test
  void testGetBuildingCacheMiss() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;

    when(cacheUtility.safeGetFromCache(headers, "buildings:", id)).thenReturn(null);
    when(repository.findById(id)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> buildingService.getBuilding(headers, id));

    verify(cacheUtility, times(1)).safeGetFromCache(headers, "buildings:", id);
    verify(repository, times(1)).findById(id);
  }

  @Test
  void testUpdateCacheAfterGetBuilding() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;
    BuildingEntity buildingEntity = new BuildingEntity();
    Building building = new Building(1, "name", "address", "city", "state", "zipCode", "description");

    when(cacheUtility.safeGetFromCache(headers, "buildings:", id)).thenReturn(null);
    when(repository.findById(id)).thenReturn(Optional.of(buildingEntity));
    when(mapper.entityToApi(buildingEntity)).thenReturn(building);

    Building result = buildingService.getBuilding(headers, id);

    verify(repository, times(1)).findById(id);
    verify(mapper, times(1)).entityToApi(buildingEntity);
    verify(cacheUtility, times(1)).safeAddToCache(headers, "buildings:", buildingEntity);
    assertThat(building).isEqualTo(result);
  }
}
