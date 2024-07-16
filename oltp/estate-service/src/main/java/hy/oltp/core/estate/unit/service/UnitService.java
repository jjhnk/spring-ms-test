package hy.oltp.core.estate.unit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import hy.api.core.estate.unit.Room;
import hy.api.core.estate.unit.Unit;
import hy.api.core.estate.unit.UnitDetail;
import hy.api.exceptions.InvalidInputException;
import hy.api.exceptions.NotFoundException;
import hy.oltp.core.estate.common.service.CacheUtility;
import hy.oltp.core.estate.unit.persistence.RoomEntity;
import hy.oltp.core.estate.unit.persistence.RoomRepository;
import hy.oltp.core.estate.unit.persistence.UnitEntity;
import hy.oltp.core.estate.unit.persistence.UnitRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UnitService {
  private static final String MSG_NOT_FOUND_WITH_ID = "Unit not found with id ";
  private static final String CACHE_KEY = "units:";

  private final UnitRepository unitRepository;
  private final RoomRepository roomRepository;
  private final CacheUtility<UnitEntity> unitCacheUtility;
  private final CacheUtility<RoomEntity> roomCacheUtility;
  private final UnitMapper unitMapper;
  private final RoomMapper roomMapper;

  public UnitService(
    UnitRepository unitRepository,
    RoomRepository roomRepository,
    CacheUtility<UnitEntity> unitCacheUtility,
    CacheUtility<RoomEntity> roomCacheUtility,
    UnitMapper unitMapper,
    RoomMapper roomMapper) {
    this.unitRepository = unitRepository;
    this.roomRepository = roomRepository;
    this.unitCacheUtility = unitCacheUtility;
    this.roomCacheUtility = roomCacheUtility;
    this.unitMapper = unitMapper == null
      ? UnitMapper.INSTANCE
      : unitMapper;
    this.roomMapper = roomMapper == null
      ? RoomMapper.INSTANCE
      : roomMapper;
  }

  public Unit createUnit(Unit unit) {
    log.info("Creating unit {}", unit);
    var entity = unitMapper.apiToEntity(unit);
    entity.init();
    var saved = unitRepository.save(entity);
    return unitMapper.entityToApi(saved);
  }

  public Unit getUnit(HttpHeaders headers, int id) {
    var found = getUnitEntityFromCacheOrRepository(headers, id);
    unitCacheUtility.safeAddToCache(headers, CACHE_KEY + id, found);
    return unitMapper.entityToApi(found);
  }

  public List<Unit> getUnits(HttpHeaders headers, int buildingId, int tenantId) {
    if (buildingId == 0 && tenantId == 0) {
      return getUnitEntitiesFromCacheOrRepository(headers);
    }

    List<UnitEntity> unitEntities;
    if (buildingId > 0 && tenantId > 0) {
      unitEntities = unitRepository.findByBuildingIdAndTenantId(buildingId, tenantId);
    } else if (buildingId > 0) {
      unitEntities = unitRepository.findByBuildingId(buildingId);
    } else if (tenantId > 0) {
      unitEntities = unitRepository.findByTenantId(tenantId);
    } else {
      unitEntities = new ArrayList<>();
    }

    return unitEntities.stream()
      .map(unitMapper::entityToApi)
      .collect(Collectors.toList());
  }

  public Unit updateUnit(HttpHeaders headers, int id, Unit unit) {
    var found = getUnitEntityFromCacheOrRepository(headers, id);

    unitMapper.updateEntityFromApi(unit, found);

    var saved = unitRepository.save(found);
    unitCacheUtility.safeAddToCache(headers, CACHE_KEY + id, saved);
    return unitMapper.entityToApi(found);
  }

  public void deleteUnit(HttpHeaders headers, int id) {
    var found = getUnitEntityFromCacheOrRepository(headers, id);
    unitRepository.delete(found);
    unitCacheUtility.safeRemoveFromCache(headers, CACHE_KEY + id);
  }

  public UnitDetail createUnitDetail(int unitId, UnitDetail unitDetail) {
    var found = getUnitEntityFromCacheOrRepository(null, unitId);
    if (!found.getDetail().isDefault()) {
      throw new InvalidInputException("Unit already has a detail");
    }
    unitMapper.updateEntityFromApi(unitDetail, found.getDetail());
    var saved = unitRepository.save(found);
    log.info("Creating unit detail {} on {}", unitDetail, unitId);
    return unitMapper.entityToApi(saved.getDetail());
  }

  public UnitDetail getUnitDetail(HttpHeaders headers, int id) {
    var found = getUnitEntityFromCacheOrRepository(headers, id);
    unitCacheUtility.safeAddToCache(headers, CACHE_KEY + id, found);
    return unitMapper.entityToApi(found.getDetail());
  }

  public UnitDetail updateUnitDetail(HttpHeaders headers, int id, UnitDetail unitDetail) {
    var found = getUnitEntityFromCacheOrRepository(headers, id);
    unitMapper.updateEntityFromApi(unitDetail, found.getDetail());

    var saved = unitRepository.save(found);
    unitCacheUtility.safeAddToCache(headers, CACHE_KEY + id, saved);
    return unitMapper.entityToApi(saved.getDetail());
  }

  public void deleteUnitDetail(HttpHeaders headers, int id) {
    var found = getUnitEntityFromCacheOrRepository(headers, id);
    found.getDetail().clear();

    var saved = unitRepository.save(found);
    unitCacheUtility.safeAddToCache(headers, CACHE_KEY + id, saved);
  }

  public Room createRoom(int unitId, Room room) {
    log.info("Creating room {}", room);
    var saved = roomRepository.saveByUnitId(roomMapper.apiToEntity(room), unitId);
    return roomMapper.entityToApi(saved);
  }

  public List<Room> createRooms(int unitId, List<Room> rooms) {
    log.info("Creating rooms {}", rooms);
    List<RoomEntity> entities = rooms.stream()
      .map(roomMapper::apiToEntity)
      .collect(Collectors.toList());
    roomRepository.saveAllByUnitId(entities, unitId);

    return entities.stream()
      .map(roomMapper::entityToApi)
      .collect(Collectors.toList());
  }

  public Room getRoom(HttpHeaders headers, int unitId, int roomId) {
    var entity = getRoomEntityFromCacheOrRepository(headers, unitId, roomId);
    roomCacheUtility.safeAddToCache(headers, CACHE_KEY + unitId, entity);
    return roomMapper.entityToApi(entity);
  }

  public List<Room> getRooms(HttpHeaders headers, int unitId) {
    return getRoomEntitiesFromCacheOrRepository(headers, unitId);
  }

  public Room updateRoom(HttpHeaders headers, int unitId, int roomId, Room room) {
    var entity = getRoomEntityFromCacheOrRepository(headers, unitId, roomId);
    roomMapper.updateEntityFromApi(room, entity);
    roomRepository.save(entity);
    roomCacheUtility.safeAddToCache(headers, CACHE_KEY + unitId, entity);
    return roomMapper.entityToApi(entity);
  }

  public void deleteRoom(HttpHeaders headers, int unitId, int roomId) {
    var unitEntity = getUnitEntityFromCacheOrRepository(headers, unitId);
    var toDelete = unitEntity.getDetail().getRoomById(roomId);
    unitEntity.getDetail().removeRoom(toDelete);

    unitRepository.save(unitEntity);
    roomCacheUtility.safeRemoveFromCache(headers, CACHE_KEY + unitId + ":" + roomId);
  }

  private UnitEntity getUnitEntityFromCacheOrRepository(HttpHeaders headers, int id) {
    var cached = unitCacheUtility.safeGetFromCache(headers, CACHE_KEY + id, id);
    if (cached != null) {
      return cached;
    }

    return unitRepository.findById(id)
      .orElseThrow(() -> new NotFoundException(MSG_NOT_FOUND_WITH_ID + id));
  }

  private List<Unit> getUnitEntitiesFromCacheOrRepository(HttpHeaders headers) {
    // TODO: consider whether caching or not entity when post
    // var cached = unitCacheUtility.safeGetListFromCache(headers, CACHE_KEY);
    // if (cached != null && !cached.isEmpty()) {
    //   return cached.stream()
    //     .map(unitMapper::entityToApi)
    //     .collect(Collectors.toList());
    // }

    var found = unitRepository.findAll();
    return found.stream()
      .map(unitMapper::entityToApi)
      .collect(Collectors.toList());
  }

  private RoomEntity getRoomEntityFromCacheOrRepository(HttpHeaders headers, int unitId, int roomId) {
    var cached = roomCacheUtility.safeGetFromCache(headers, CACHE_KEY + unitId + ":", roomId);
    if (cached != null) {
      return cached;
    }

    var found = roomRepository.findByIdAndUnitId(roomId, unitId);
    if (found == null) {
      throw new NotFoundException(MSG_NOT_FOUND_WITH_ID + roomId);
    }
    return found;

  }

  private List<Room> getRoomEntitiesFromCacheOrRepository(HttpHeaders headers, int unitId) {
    // TODO: consider whether caching or not entity when post
    /* var cached = roomCacheUtility.safeGetListFromCache(headers, CACHE_KEY + unitId + ":");
    if (cached != null && !cached.isEmpty()) {
      return cached.stream()
        .map(roomMapper::entityToApi)
        .collect(Collectors.toList());
    } */

    return roomRepository.findAllByUnitId(unitId)
      .stream()
      .map(roomMapper::entityToApi)
      .collect(Collectors.toList());
  }
}
