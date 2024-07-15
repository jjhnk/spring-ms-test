package hy.oltp.core.estate.building.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import hy.api.core.estate.buildings.Building;
import hy.api.exceptions.NotFoundException;
import hy.oltp.core.estate.building.persistence.BuildingEntity;
import hy.oltp.core.estate.building.persistence.BuildingRepository;
import hy.oltp.core.estate.common.service.CacheUtility;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BuildingService {
  private static final String MSG_NOT_FOUND_WITH_ID = "Building not found with id ";
  private static final String CACHE_KEY = "buildings:";

  private final BuildingRepository repository;
  private final CacheUtility<BuildingEntity> cacheUtility;
  private final BuildingMapper mapper;

  public BuildingService(BuildingRepository repository, CacheUtility<BuildingEntity> cacheUtility, BuildingMapper mapper) {
    this.repository = repository;
    this.cacheUtility = cacheUtility;
    this.mapper = mapper == null ? BuildingMapper.INSTANCE : mapper;
  }

  public Building createBuilding(Building estate) {
    log.info("Creating building {}", estate);
    var saved = repository.save(mapper.apiToEntity(estate));
    return mapper.entityToApi(saved);
  }

  public Building getBuilding(HttpHeaders headers, int id) {
    var entity = getEntityFromCacheOrRepository(headers, id);
    cacheUtility.safeAddToCache(headers, CACHE_KEY, entity);
    return mapper.entityToApi(entity);
  }

  public List<Building> getBuildings(HttpHeaders headers) {
    return getEntitiesFromCacheOrRepository(headers);
  }

  public Building updateBuilding(HttpHeaders headers, int id, Building building) {
    var found = getEntityFromCacheOrRepository(headers, id);
    mapper.updateEntityFromApi(building, found);
    var saved = repository.save(found);
    cacheUtility.safeAddToCache(headers, CACHE_KEY, saved);
    return mapper.entityToApi(saved);
  }

  public void deleteBuilding(HttpHeaders headers, int id) {
    log.info("Deleting building {}", id);
    var found = getEntityFromCacheOrRepository(headers, id);
    repository.delete(found);
    cacheUtility.safeRemoveFromCache(headers, CACHE_KEY + id);
  }

  private BuildingEntity getEntityFromCacheOrRepository(HttpHeaders headers, int id) {
    var cached = cacheUtility.safeGetFromCache(headers, CACHE_KEY, id);
    if (cached != null) {
      return cached;
    }

    return repository.findById(id)
      .orElseThrow(() -> new NotFoundException(MSG_NOT_FOUND_WITH_ID + id));
  }

  private List<Building> getEntitiesFromCacheOrRepository(HttpHeaders headers) {
    var cached = cacheUtility.safeGetListFromCache(headers, CACHE_KEY);
    if (cached != null && !cached.isEmpty()) {
      return cached.stream()
        .map(mapper::entityToApi)
        .collect(Collectors.toList());
    }

    return repository.findAll()
      .stream()
      .map(mapper::entityToApi)
      .collect(Collectors.toList());
  }
}
