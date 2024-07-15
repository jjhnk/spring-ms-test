package hy.oltp.core.estate.unit.interfaces;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import hy.api.core.estate.buildings.Building;
import hy.api.core.estate.lease.Lease;
import hy.api.core.estate.lease.LeaseDetail;
import hy.api.core.estate.lease.LeaseStatus;
import hy.api.core.estate.tenant.Tenant;
import hy.api.core.estate.unit.Room;
import hy.api.core.estate.unit.RoomTypes;
import hy.api.core.estate.unit.Unit;
import hy.api.core.estate.unit.UnitDetail;
import hy.api.core.estate.unit.UnitStatus;
import hy.api.core.estate.unit.UnitSummary;
import hy.oltp.core.estate.TestSecurityConfig;
import hy.oltp.core.estate.building.persistence.BuildingRepository;
import hy.oltp.core.estate.building.service.BuildingService;
import hy.oltp.core.estate.lease.persistence.LeaseRepository;
import hy.oltp.core.estate.lease.service.LeaseService;
import hy.oltp.core.estate.tenant.PostgreSqlTestBase;
import hy.oltp.core.estate.tenant.persistence.TenantRepository;
import hy.oltp.core.estate.tenant.service.TenantService;
import hy.oltp.core.estate.unit.persistence.UnitRepository;
import hy.oltp.core.estate.unit.service.UnitMapper;
import reactor.core.publisher.Mono;

@SpringBootTest(
  webEnvironment = WebEnvironment.RANDOM_PORT,
  classes = {TestSecurityConfig.class},
  properties = {"logging.level.hy=DEBUG", "spring.jpa.hibernate.ddl-auto=update"})
class UnitControllerImplTest extends PostgreSqlTestBase {
  private static final String ROOT_PATH = "/estate/unit";
  private final UnitMapper mapper = UnitMapper.INSTANCE;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private WebTestClient client;

  @Autowired
  private UnitRepository repository;
  @Autowired
  private BuildingRepository buildingRepository;
  @Autowired
  private TenantRepository tenantRepository;
  @Autowired
  private LeaseRepository leaseRepository;

  @Autowired
  private BuildingService buildingService;
  @Autowired
  private TenantService tenantService;
  @Autowired
  private LeaseService leaseService;

  private Building building1 = new Building(1, "name", "address", "city", "state", "zipCode", "description");
  private Building building2 = new Building(2, "name2", "address2", "city2", "state2", "zipCode2", "description2");
  private UnitSummary unitSummary1 = new UnitSummary("b101", 1, UnitStatus.AVAILABLE);
  private UnitSummary unitSummary2 = new UnitSummary("b102", 2, UnitStatus.MAINTENANCE);
  private UnitDetail unitDetail1 = new UnitDetail(1, new BigDecimal(1));
  private UnitDetail unitDetail2 = new UnitDetail(2, new BigDecimal(1));
  private Unit sourceUnit;
  private Unit savedUnit;
  private Tenant tenant1 = new Tenant(1, "firstName", "lastName", "email@email.com", "01011111111");
  private Tenant tenant2 = new Tenant(2, "firstName2", "lastName2", "email2@email.com", "01022222222");
  private Lease lease1 = new Lease(1, new LeaseDetail(Instant.now(), Instant.now(), 10.0, 10.0, LeaseStatus.ACTIVE));
  private Lease lease2 = new Lease(2, new LeaseDetail(Instant.now(), Instant.now(), 11.0, 11.0, LeaseStatus.EXPIRED));

  private Building savedBuilding1;
  private Building savedBuilding2;

  @BeforeEach
  void setUp() {
    buildingRepository.deleteAll();
    tenantRepository.deleteAll();
    leaseRepository.deleteAll();
    repository.deleteAll();

    savedBuilding1 = buildingService.createBuilding(building1);
    savedBuilding2 = buildingService.createBuilding(building2);
    tenant1 = tenantService.createTenant(tenant1);
    tenant2 = tenantService.createTenant(tenant2);

    sourceUnit = new Unit(0, unitSummary1, unitDetail1);
    sourceUnit.setBuilding(savedBuilding1);

    savedUnit = postAndVerify(sourceUnit, HttpStatus.OK);
    assertThat(savedUnit.getSummary()).isEqualTo(sourceUnit.getSummary());
    assertThat(savedUnit.getDetail()).isEqualTo(sourceUnit.getDetail());

    lease1.setTenant(tenant1);
    lease1.setUnit(savedUnit);
    leaseService.createLease(lease1);
  }

  @Test
  void postUnitTest() {
    assertThat(repository.findAll()).hasSize(1);

    Unit newUnit = new Unit(0, unitSummary2, unitDetail2);
    newUnit.setBuilding(savedBuilding2);
    var response = postAndVerify(newUnit, HttpStatus.OK);

    assertThat(repository.findAll()).hasSize(2);
    assertResponseAndRepository(newUnit, response);
  }

  @Test
  void getUnitTest() {
    var response = getAndVerify("/" + savedUnit.getId(), HttpStatus.OK);
    assertResponseAndRepository(sourceUnit, response);
  }

  @Test
  void getAllUnitsTest() {
    Unit newUnit = new Unit(0, unitSummary2, unitDetail2);
    newUnit.setBuilding(savedBuilding2);

    newUnit = postAndVerify(newUnit, HttpStatus.OK);
    lease2.setTenant(tenant2);
    lease2.setUnit(newUnit);
    leaseService.createLease(lease2);

    var response = getListAndVerify("/all", HttpStatus.OK);

    assertThat(response).hasSize(2);
    for (var unit : response) {
      if (unit.getId() == savedUnit.getId()) {
        assertResponseAndRepository(sourceUnit, unit);
      } else {
        assertResponseAndRepository(newUnit, unit);
      }
    }

    response = getListAndVerify("/all?buildingId=" + savedBuilding1.getId(), HttpStatus.OK);
    assertThat(response).hasSize(1);
    assertThat(response.get(0)).isEqualTo(savedUnit);

    response = getListAndVerify("/all?tenantId=" + tenant2.getId(), HttpStatus.OK);
    assertThat(response).hasSize(1);
    assertThat(response.get(0)).isEqualTo(newUnit);
  }

  @Test
  void putUnitTest() {
    Unit newUnit = new Unit(0, unitSummary2, unitDetail2);
    var response = putAndVerify(savedUnit.getId(), newUnit, HttpStatus.OK);

    assertResponseAndRepository(newUnit, response);
  }

  @Test
  void deleteUnitTest() {
    deleteAndVerify(savedUnit.getId(), HttpStatus.OK);
    assertThat(repository.findById(savedUnit.getId())).isEmpty();
  }

  private void assertResponseAndRepository(Unit source, Unit response) {
    assertThat(response.getSummary()).isEqualTo(source.getSummary());
    assertThat(response.getDetail()).isEqualTo(source.getDetail());

    var entity = repository.findById(response.getId())
      .get();
    assertThat(mapper.entityToApi(entity.getSummary())).isEqualTo(source.getSummary());
    assertThat(mapper.entityToApi(entity.getDetail())).isEqualTo(source.getDetail());
  }

  @Test
  void createUnitDetailTest() {
    assertThat(repository.findAll()).hasSize(1);

    postAndVerify("/" + savedUnit.getId() + "/details", savedUnit, Unit.class, HttpStatus.UNPROCESSABLE_ENTITY);

    Unit newUnit = new Unit(0, unitSummary2, null);
    newUnit.setBuilding(savedBuilding2);
    newUnit = postAndVerify(newUnit, HttpStatus.OK);

    postAndVerify("/" + newUnit.getId() + "/details", new UnitDetail(300, new BigDecimal(300)), UnitDetail.class,
      HttpStatus.OK);
  }

  @Test
  void getUnitDetailTest() {
    var found = getAndVerify("/" + savedUnit.getId() + "/details", UnitDetail.class, HttpStatus.OK);

    assertThat(found).isEqualTo(savedUnit.getDetail());
  }

  @Test
  void updateUnitDetailTest() {
    var newDetail = new UnitDetail(300, new BigDecimal(300));
    var found = putAndVerify("/" + savedUnit.getId() + "/details", newDetail, UnitDetail.class, HttpStatus.OK);

    assertThat(found).isEqualTo(newDetail);

    var entity = repository.findById(savedUnit.getId())
      .get();
    assertThat(mapper.entityToApi(entity.getDetail())).isEqualTo(newDetail);
  }

  @Test
  void deleteUnitDetailTest() {
    deleteAndVerify("/" + savedUnit.getId() + "/details", HttpStatus.OK);
  }

  @Test
  void createRoomTest() {
    Room room = new Room(0, "name", RoomTypes.DINING, 1, 1);

    var found = postAndVerify("/" + savedUnit.getId() + "/details/room", room, Room.class, HttpStatus.OK);
    var unit = getAndVerify("/" + found.getUnit()
      .getId(), HttpStatus.OK);

    assertThat(found).isEqualTo(unit.getDetail()
      .getRoomById(found.getId()));
  }

  @Test
  void createRoomsTest() {
    var rooms =
      Arrays.asList(new Room(0, "name", RoomTypes.DINING, 1, 1), new Room(1, "name2", RoomTypes.BATHROOMS, 2, 2));

    var found = postAndVerify("/" + savedUnit.getId() + "/details/rooms", rooms,
      new ParameterizedTypeReference<List<Room>>() {}, HttpStatus.OK);
    var unit = getAndVerify("/" + savedUnit.getId(), HttpStatus.OK);

    found.forEach(e -> assertThat(e).isEqualTo(unit.getDetail()
      .getRoomById(e.getId())));
  }

  @Test
  void getRoomTest() {
    var rooms =
      Arrays.asList(new Room(0, "name", RoomTypes.DINING, 1, 1), new Room(1, "name2", RoomTypes.BATHROOMS, 2, 2));
    var created = postAndVerify("/" + savedUnit.getId() + "/details/rooms", rooms,
      new ParameterizedTypeReference<List<Room>>() {}, HttpStatus.OK);
    var toTest = created.get(0);

    var foundRoom =
      getAndVerify("/" + savedUnit.getId() + "/details/room/" + toTest.getId(), Room.class, HttpStatus.OK);
    var foundUnit = getAndVerify("/" + savedUnit.getId(), HttpStatus.OK);

    assertThat(foundRoom).isEqualTo(foundUnit.getDetail()
      .getRoomById(toTest.getId()));
  }

  @Test
  void getRoomsTest() {
    var rooms =
      Arrays.asList(new Room(0, "name", RoomTypes.DINING, 1, 1), new Room(1, "name2", RoomTypes.BATHROOMS, 2, 2));
    postAndVerify("/" + savedUnit.getId() + "/details/rooms", rooms, new ParameterizedTypeReference<List<Room>>() {},
      HttpStatus.OK);

    var foundRooms = getAndVerify("/" + savedUnit.getId() + "/details/room/all",
      new ParameterizedTypeReference<List<Room>>() {}, HttpStatus.OK);
    var foundUnit = getAndVerify("/" + savedUnit.getId(), HttpStatus.OK);

    foundRooms.forEach(e -> assertThat(e).isEqualTo(foundUnit.getDetail()
      .getRoomById(e.getId())));
  }

  @Test
  void updateRoomTest() {
    var rooms =
      Arrays.asList(new Room(0, "name", RoomTypes.DINING, 1, 1), new Room(1, "name2", RoomTypes.BATHROOMS, 2, 2));
    var created = postAndVerify("/" + savedUnit.getId() + "/details/rooms", rooms,
      new ParameterizedTypeReference<List<Room>>() {}, HttpStatus.OK);
    var toTest = new Room(0, "name", RoomTypes.DINING, 2, 2);

    var updatedRoom = putAndVerify("/" + savedUnit.getId() + "/details/room/" + created.get(0)
      .getId(), toTest, Room.class, HttpStatus.OK);
    var updatedUnit = getAndVerify("/" + savedUnit.getId(), HttpStatus.OK);

    assertThat(updatedRoom).isEqualTo(toTest);
    assertThat(updatedUnit.getDetail()
      .getRoomById(updatedRoom.getId())).isEqualTo(updatedRoom);
  }

  @Test
  void deleteRoomTest() {
    var rooms =
      Arrays.asList(new Room(0, "name", RoomTypes.DINING, 1, 1), new Room(1, "name2", RoomTypes.BATHROOMS, 2, 2));
    var created = postAndVerify("/" + savedUnit.getId() + "/details/rooms", rooms,
      new ParameterizedTypeReference<List<Room>>() {}, HttpStatus.OK);

    deleteAndVerify("/" + savedUnit.getId() + "/details/room/" + created.get(0)
      .getId(), HttpStatus.OK);
    var updatedUnit = getAndVerify("/" + savedUnit.getId(), HttpStatus.OK);

    assertThat(updatedUnit.getDetail()
      .getRoomById(created.get(0)
        .getId())).isNull();
    getAndVerify("/" + savedUnit.getId() + "/details/room/" + created.get(0)
      .getId(), Object.class, HttpStatus.NOT_FOUND);
  }

  private Unit postAndVerify(Unit unit, HttpStatus expectedStatus) {
    return postAndVerify("", unit, Unit.class, expectedStatus);
  }

  private <T> T postAndVerify(String query, T target, Class<T> clazz, HttpStatus expectedStatus) {
    // @formatter:off
    return (T)client.post()
      .uri(ROOT_PATH + query)
      .accept(APPLICATION_JSON)
      .body(Mono.just(target), clazz)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus)
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody(clazz)
      .returnResult()
      .getResponseBody();
    // @formatter:on
  }

  private <T> T postAndVerify(String query, T target, ParameterizedTypeReference<T> clazz, HttpStatus expectedStatus) {
    // @formatter:off
    return (T)client.post()
      .uri(ROOT_PATH + query)
      .accept(APPLICATION_JSON)
      .body(Mono.just(target), clazz)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus)
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody(clazz)
      .returnResult()
      .getResponseBody();
    // @formatter:on
  }

  private Unit getAndVerify(String query, HttpStatus expectedStatus) {
    return getAndVerify(query, Unit.class, expectedStatus);
  }

  private <T> T getAndVerify(String query, Class<T> clazz, HttpStatus expectedStatus) {
    // @formatter:off
    return client.get()
      .uri(ROOT_PATH + query)
      .header("Cache-Control", "no-store")
      .accept(APPLICATION_JSON)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus)
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody(clazz)
      .returnResult()
      .getResponseBody();
    // @formatter:on
  }

  private <T> T getAndVerify(String query, ParameterizedTypeReference<T> clazz, HttpStatus expectedStatus) {
    // @formatter:off
    return client.get()
      .uri(ROOT_PATH + query)
      .header("Cache-Control", "no-store")
      .accept(APPLICATION_JSON)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus)
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody(clazz)
      .returnResult()
      .getResponseBody();
    // @formatter:on
  }

  private List<Unit> getListAndVerify(String query, HttpStatus expectedStatus) {
    // @formatter:off
    objectMapper.registerModule(new JavaTimeModule());
    return client.get()
      .uri(ROOT_PATH + query)
      .header("Cache-Control", "no-store")
      .accept(APPLICATION_JSON)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus)
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody(new ParameterizedTypeReference<List<Object>>() {})
      .returnResult()
      .getResponseBody()
      .stream()
      .map(o -> {
        try {
          return objectMapper.readValue(objectMapper.writeValueAsString(o), Unit.class);
        } catch (Exception e) {
          e.printStackTrace();
        }
        return null;
      })
      .collect(Collectors.toList());
    // @formatter:on
  }

  private Unit putAndVerify(int id, Unit unit, HttpStatus expectedStatus) {
    return putAndVerify("/" + id, unit, Unit.class, expectedStatus);
  }

  private <T> T putAndVerify(String query, T target, Class<T> clazz, HttpStatus expectedStatus) {
    // @formatter:off
    return client.put()
      .uri(ROOT_PATH + query)
      .accept(APPLICATION_JSON)
      .body(Mono.just(target), clazz)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus)
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody(clazz)
      .returnResult()
      .getResponseBody();
    // @formatter:on
  }

  private WebTestClient.ResponseSpec deleteAndVerify(int id, HttpStatus expectedStatus) {
    return deleteAndVerify("/" + id, expectedStatus);
  }

  private WebTestClient.ResponseSpec deleteAndVerify(String query, HttpStatus expectedStatus) {
    // @formatter:off
    return client.delete()
      .uri(ROOT_PATH + query)
      .accept(APPLICATION_JSON)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus);
    // @formatter:on
  }
}
