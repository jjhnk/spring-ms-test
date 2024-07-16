package hy.oltp.core.estate.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;

import hy.api.core.estate.unit.Room;
import hy.api.core.estate.unit.RoomTypes;
import hy.api.core.estate.unit.Unit;
import hy.api.core.estate.unit.UnitDetail;
import hy.api.core.estate.unit.UnitStatus;
import hy.api.core.estate.unit.UnitSummary;
import hy.api.exceptions.NotFoundException;
import hy.oltp.core.estate.common.service.CacheUtility;
import hy.oltp.core.estate.unit.persistence.RoomEntity;
import hy.oltp.core.estate.unit.persistence.RoomRepository;
import hy.oltp.core.estate.unit.persistence.UnitDetailEntity;
import hy.oltp.core.estate.unit.persistence.UnitEntity;
import hy.oltp.core.estate.unit.persistence.UnitRepository;
import hy.oltp.core.estate.unit.persistence.UnitSummaryEntity;

class UnitServiceTests {

  private static final String CACHE_CONTROL_KEY = "units:";
  @Mock(name = "unitRepository")
  private UnitRepository unitRepository;
  @Mock(name = "roomRepository")
  private RoomRepository roomRepository;
  @Mock(name = "unitCache")
  private CacheUtility<UnitEntity> unitCacheUtility;
  @Mock(name = "roomCache")
  private CacheUtility<RoomEntity> roomCacheUtility;
  @Mock
  private UnitMapper unitMapper;
  @Mock
  private RoomMapper roomMapper;
  @InjectMocks
  private UnitService unitService;

  private UnitSummary unitSummary1 = new UnitSummary("b101", 1, UnitStatus.AVAILABLE);
  private UnitSummary unitSummary2 = new UnitSummary("b102", 2, UnitStatus.AVAILABLE);
  private UnitSummaryEntity unitSummaryEntity1 = new UnitSummaryEntity("b101", 1, UnitStatus.AVAILABLE);
  private UnitSummaryEntity unitSummaryEntity2 = new UnitSummaryEntity("b102", 2, UnitStatus.AVAILABLE);
  private UnitDetail unitDetail1 = new UnitDetail(1, new BigDecimal(1));
  private UnitDetail unitDetail2 = new UnitDetail(2, new BigDecimal(2));
  private UnitDetailEntity unitDetailEntity1 = new UnitDetailEntity(1, new BigDecimal(1));
  private UnitDetailEntity unitDetailEntity2 = new UnitDetailEntity(2, new BigDecimal(2));

  private Room room1 = new Room(1, "b101", RoomTypes.DINING, 1, 1);
  private Room room2 = new Room(2, "b102", RoomTypes.LIVING, 2, 2);
  private RoomEntity roomEntity1 = new RoomEntity("b101", RoomTypes.DINING, 1, 1);
  private RoomEntity roomEntity2 = new RoomEntity("b102", RoomTypes.LIVING, 2, 2);

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    // see https://stackoverflow.com/questions/63889701/injectmocks-is-wrongly-injecting-the-same-mock-into-2-different-fields-of-simila
    unitService = new UnitService(unitRepository, roomRepository, unitCacheUtility, roomCacheUtility, unitMapper, roomMapper);
  }


  @Test
  void testCreateUnit() {
    Unit unit = new Unit(1, unitSummary1, unitDetail1);
    UnitEntity unitEntity = new UnitEntity(unitSummaryEntity1, unitDetailEntity1);
    when(unitMapper.apiToEntity(unit)).thenReturn(unitEntity);
    when(unitRepository.save(unitEntity)).thenReturn(unitEntity);
    when(unitMapper.entityToApi(unitEntity)).thenReturn(unit);

    Unit result = unitService.createUnit(unit);

    verify(unitRepository, times(1)).save(unitEntity);
    assertThat(unit).isEqualTo(result);
  }

  @Test
  void testGetUnitWithCache() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;
    UnitEntity unitEntity = new UnitEntity(unitSummaryEntity1, unitDetailEntity1);
    Unit unit = new Unit(1, unitSummary1, unitDetail1);

    when(unitCacheUtility.safeGetFromCache(headers, CACHE_CONTROL_KEY + id, id)).thenReturn(unitEntity);
    when(unitMapper.entityToApi(unitEntity)).thenReturn(unit);

    Unit result = unitService.getUnit(headers, id);

    verify(unitCacheUtility, times(1)).safeGetFromCache(headers, CACHE_CONTROL_KEY + id, id);
    verify(unitMapper, times(1)).entityToApi(unitEntity);
    assertThat(unit).isEqualTo(result);
  }

  @Test
  void testGetUnitWithRepository() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;
    UnitEntity unitEntity = new UnitEntity(unitSummaryEntity1, unitDetailEntity1);
    Unit unit = new Unit(1, unitSummary1, unitDetail1);

    when(unitCacheUtility.safeGetFromCache(headers, CACHE_CONTROL_KEY, id)).thenReturn(null);
    when(unitRepository.findById(id)).thenReturn(Optional.of(unitEntity));
    when(unitMapper.entityToApi(unitEntity)).thenReturn(unit);

    Unit result = unitService.getUnit(headers, id);

    verify(unitRepository, times(1)).findById(id);
    verify(unitMapper, times(1)).entityToApi(unitEntity);
    assertThat(unit).isEqualTo(result);
  }

  @Test
  void testGetUnitNotFound() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;

    when(unitCacheUtility.safeGetFromCache(headers, CACHE_CONTROL_KEY, id)).thenReturn(null);
    when(unitRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> unitService.getUnit(headers, id));

    verify(unitRepository, times(1)).findById(id);
  }

  @Test
  void testGetUnitsWithCache() {
    HttpHeaders headers = new HttpHeaders();
    List<UnitEntity> unitEntities = Arrays.asList(new UnitEntity(unitSummaryEntity1, unitDetailEntity1),
      new UnitEntity(unitSummaryEntity2, unitDetailEntity2));
    List<Unit> units = Arrays.asList(new Unit(1, unitSummary1, unitDetail1), new Unit(2, unitSummary2, unitDetail2));

    when(unitRepository.findAll()).thenReturn(unitEntities);
    when(unitMapper.entityToApi(any(UnitEntity.class))).thenReturn(units.get(0), units.get(1));

    List<Unit> result = unitService.getUnits(headers, 0, 0);

    assertThat(units).hasSameElementsAs(result);
  }

  @Test
  void testGetUnitsWithRepository() {
    HttpHeaders headers = new HttpHeaders();
    List<UnitEntity> unitEntities = Arrays.asList(new UnitEntity(unitSummaryEntity1, unitDetailEntity1),
      new UnitEntity(unitSummaryEntity2, unitDetailEntity2));
    List<Unit> units = Arrays.asList(new Unit(1, unitSummary1, unitDetail1), new Unit(2, unitSummary2, unitDetail2));

    when(unitCacheUtility.safeGetListFromCache(headers, CACHE_CONTROL_KEY)).thenReturn(null);
    when(unitRepository.findAll()).thenReturn(unitEntities);
    when(unitMapper.entityToApi(any(UnitEntity.class))).thenReturn(units.get(0), units.get(1));

    List<Unit> result = unitService.getUnits(headers, 0, 0);

    verify(unitMapper, times(unitEntities.size())).entityToApi(any(UnitEntity.class));
    verify(unitRepository, times(1)).findAll();
    assertThat(units).hasSameElementsAs(result);
  }

  @Test
  void testGetUnitsNotFound() {
    HttpHeaders headers = new HttpHeaders();

    when(unitCacheUtility.safeGetListFromCache(headers, CACHE_CONTROL_KEY)).thenReturn(null);
    when(unitRepository.findAll()).thenReturn(List.of());

    List<Unit> result = unitService.getUnits(headers, 0, 0);

    verify(unitRepository, times(1)).findAll();
    assertThat(result).isEmpty();
  }

  @Test
  void testUpdateUnit() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;
    Unit unit = new Unit(1, unitSummary1, unitDetail1);
    UnitEntity unitEntity = new UnitEntity(unitSummaryEntity1, unitDetailEntity1);

    when(unitCacheUtility.safeGetFromCache(headers, CACHE_CONTROL_KEY + id, id)).thenReturn(unitEntity);
    doNothing().when(unitMapper).updateEntityFromApi(unit, unitEntity);
    when(unitRepository.save(unitEntity)).thenReturn(unitEntity);
    when(unitMapper.entityToApi(unitEntity)).thenReturn(unit);

    Unit result = unitService.updateUnit(headers, id, unit);

    verify(unitRepository, times(1)).save(unitEntity);
    verify(unitMapper, times(1)).entityToApi(unitEntity);
    assertThat(unit).isEqualTo(result);
  }

  @Test
  void testDeleteUnit() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;
    UnitEntity unitEntity = new UnitEntity(unitSummaryEntity1, unitDetailEntity1);

    when(unitCacheUtility.safeGetFromCache(headers, CACHE_CONTROL_KEY + id, id)).thenReturn(unitEntity);

    unitService.deleteUnit(headers, id);

    verify(unitRepository, times(1)).delete(unitEntity);
    verify(unitCacheUtility, times(1)).safeRemoveFromCache(headers, CACHE_CONTROL_KEY + id);
  }

  @Test
  void testGetUnitCacheMiss() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;

    when(unitCacheUtility.safeGetFromCache(headers, CACHE_CONTROL_KEY + id, id)).thenReturn(null);
    when(unitRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> unitService.getUnit(headers, id));

    verify(unitCacheUtility, times(1)).safeGetFromCache(headers, CACHE_CONTROL_KEY + id, id);
    verify(unitRepository, times(1)).findById(id);
  }

  @Test
  void testUpdateCacheAfterGetUnit() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;
    UnitEntity unitEntity = new UnitEntity(unitSummaryEntity1, unitDetailEntity1);
    Unit unit = new Unit(1, unitSummary1, unitDetail1);

    when(unitCacheUtility.safeGetFromCache(headers, CACHE_CONTROL_KEY + id, id)).thenReturn(null);
    when(unitRepository.findById(id)).thenReturn(Optional.of(unitEntity));
    when(unitMapper.entityToApi(unitEntity)).thenReturn(unit);

    Unit result = unitService.getUnit(headers, id);

    verify(unitRepository, times(1)).findById(id);
    verify(unitMapper, times(1)).entityToApi(unitEntity);
    verify(unitCacheUtility, times(1)).safeAddToCache(headers, CACHE_CONTROL_KEY + id, unitEntity);
    assertThat(unit).isEqualTo(result);
  }

  @Test
  void testCreateUnitDetail() {
    int id = 1;
    UnitDetail unitDetail = unitDetail1;
    UnitEntity unitEntity = new UnitEntity(unitSummaryEntity1, new UnitDetailEntity());

    when(unitRepository.findById(id)).thenReturn(Optional.of(unitEntity));
    when(unitMapper.apiToEntity(unitDetail)).thenReturn(new UnitDetailEntity());
    when(unitRepository.save(unitEntity)).thenReturn(unitEntity);
    when(unitMapper.entityToApi(unitEntity.getDetail())).thenReturn(unitDetail);

    UnitDetail result = unitService.createUnitDetail(id, unitDetail);

    verify(unitRepository, times(1)).save(unitEntity);
    assertThat(unitDetail).isEqualTo(result);
  }

  @Test
  void testGetUnitDetail() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;
    UnitEntity unitEntity = new UnitEntity(unitSummaryEntity1, unitDetailEntity1);
    UnitDetail unitDetail = unitDetail1;

    when(unitCacheUtility.safeGetFromCache(headers, CACHE_CONTROL_KEY + id, id)).thenReturn(unitEntity);
    when(unitMapper.entityToApi(unitEntity.getDetail())).thenReturn(unitDetail);

    UnitDetail result = unitService.getUnitDetail(headers, id);

    verify(unitCacheUtility, times(1)).safeGetFromCache(headers, CACHE_CONTROL_KEY + id, id);
    verify(unitMapper, times(1)).entityToApi(unitEntity.getDetail());
    assertThat(unitDetail).isEqualTo(result);
  }

  @Test
  void testUpdateUnitDetail() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;
    UnitDetail unitDetail = unitDetail1;
    UnitEntity unitEntity = new UnitEntity(unitSummaryEntity1, unitDetailEntity1);

    when(unitCacheUtility.safeGetFromCache(headers, CACHE_CONTROL_KEY + id, id)).thenReturn(unitEntity);
    when(unitMapper.apiToEntity(unitDetail)).thenReturn(unitDetailEntity1);
    when(unitRepository.save(unitEntity)).thenReturn(unitEntity);
    when(unitMapper.entityToApi(unitEntity.getDetail())).thenReturn(unitDetail);

    UnitDetail result = unitService.updateUnitDetail(headers, id, unitDetail);

    verify(unitRepository, times(1)).save(unitEntity);
    verify(unitMapper, times(1)).entityToApi(unitEntity.getDetail());
    assertThat(unitDetail).isEqualTo(result);
  }

  @Test
  void testDeleteUnitDetail() {
    HttpHeaders headers = new HttpHeaders();
    int id = 1;
    UnitEntity unitEntity = new UnitEntity(unitSummaryEntity1, unitDetailEntity1);

    when(unitRepository.save(unitEntity)).thenReturn(unitEntity);
    when(unitCacheUtility.safeGetFromCache(headers, CACHE_CONTROL_KEY + id, id)).thenReturn(unitEntity);

    unitService.deleteUnitDetail(headers, id);

    verify(unitRepository, times(1)).save(unitEntity);
    verify(unitCacheUtility, times(1)).safeAddToCache(headers, CACHE_CONTROL_KEY + id, unitEntity);
  }

  @Test
  void testCreateRoom() {
    int unitId = 1;
    Room room = room1;
    RoomEntity roomEntity = roomEntity1;

    when(roomMapper.apiToEntity(room)).thenReturn(roomEntity);
    when(roomRepository.saveByUnitId(roomEntity, unitId)).thenReturn(roomEntity);
    when(roomMapper.entityToApi(roomEntity)).thenReturn(room);

    Room result = unitService.createRoom(unitId, room);

    verify(roomRepository, times(1)).saveByUnitId(roomEntity, unitId);
    assertThat(room).isEqualTo(result);
  }

  @Test
  void testCreateRooms() {
    int unitId = 1;
    List<Room> rooms = Arrays.asList(room1, room2);
    List<RoomEntity> roomEntities = Arrays.asList(roomEntity1, roomEntity2);

    when(roomMapper.apiToEntity(any(Room.class))).thenReturn(roomEntities.get(0), roomEntities.get(1));

    unitService.createRooms(unitId, rooms);

    verify(roomRepository, times(1)).saveAllByUnitId(roomEntities, unitId);
  }

  @Test
  void testGetRoom() {
    HttpHeaders headers = new HttpHeaders();
    int unitId = 1;
    int roomId = 1;
    RoomEntity roomEntity = roomEntity1;
    Room room = room1;

    when(roomCacheUtility.safeGetFromCache(headers, CACHE_CONTROL_KEY + unitId + ":", roomId)).thenReturn(roomEntity);
    when(roomMapper.entityToApi(roomEntity)).thenReturn(room);

    Room result = unitService.getRoom(headers, unitId, roomId);

    verify(roomCacheUtility, times(1)).safeGetFromCache(headers, CACHE_CONTROL_KEY + unitId + ":", roomId);
    verify(roomMapper, times(1)).entityToApi(roomEntity);
    assertThat(room).isEqualTo(result);
  }

  @Test
  void testGetRooms() {
    HttpHeaders headers = new HttpHeaders();
    int unitId = 1;
    List<RoomEntity> roomEntities = Arrays.asList(roomEntity1, roomEntity2);
    List<Room> rooms = Arrays.asList(room1, room2);

    when(roomRepository.findAllByUnitId(unitId)).thenReturn(roomEntities);
    when(roomMapper.entityToApi(any(RoomEntity.class))).thenReturn(rooms.get(0), rooms.get(1));

    List<Room> result = unitService.getRooms(headers, unitId);

    verify(roomMapper, times(roomEntities.size())).entityToApi(any(RoomEntity.class));
    assertThat(rooms).hasSameElementsAs(result);
  }

  @Test
  void testUpdateRoom() {
    HttpHeaders headers = new HttpHeaders();
    int unitId = 1;
    int roomId = 1;
    Room room = room1;
    RoomEntity roomEntity = roomEntity1;

    when(roomCacheUtility.safeGetFromCache(headers, CACHE_CONTROL_KEY + unitId + ":", roomId)).thenReturn(roomEntity);
    doNothing().when(roomMapper)
      .updateEntityFromApi(room, roomEntity);
    when(roomRepository.save(roomEntity)).thenReturn(roomEntity);
    when(roomMapper.entityToApi(roomEntity)).thenReturn(room);

    Room result = unitService.updateRoom(headers, unitId, roomId, room);

    verify(roomRepository, times(1)).save(roomEntity);
    verify(roomMapper, times(1)).entityToApi(roomEntity);
    assertThat(room).isEqualTo(result);
  }

  @Test
  void testDeleteRoom() {
    HttpHeaders headers = new HttpHeaders();
    int unitId = 1;
    int roomId = 1;
    UnitEntity unitEntity = new UnitEntity(unitSummaryEntity1, unitDetailEntity1);
    RoomEntity roomEntity = roomEntity1;

    when(unitRepository.findById(unitId)).thenReturn(Optional.of(unitEntity));
    when(roomCacheUtility.safeGetFromCache(headers, CACHE_CONTROL_KEY + unitId + ":", roomId)).thenReturn(roomEntity);
    when(roomRepository.findByIdAndUnitId(roomId, unitId)).thenReturn(roomEntity);

    unitService.deleteRoom(headers, unitId, roomId);

    verify(unitRepository, times(1)).save(unitEntity);
    verify(roomCacheUtility, times(1)).safeRemoveFromCache(headers, CACHE_CONTROL_KEY + unitId + ":" + roomId);
  }
}
