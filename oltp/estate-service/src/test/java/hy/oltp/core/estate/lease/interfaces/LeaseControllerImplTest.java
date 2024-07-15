package hy.oltp.core.estate.lease.interfaces;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import hy.api.core.estate.buildings.Building;
import hy.api.core.estate.lease.Lease;
import hy.api.core.estate.lease.LeaseDetail;
import hy.api.core.estate.lease.LeaseStatus;
import hy.api.core.estate.tenant.Tenant;
import hy.api.core.estate.unit.Unit;
import hy.api.core.estate.unit.UnitDetail;
import hy.api.core.estate.unit.UnitStatus;
import hy.api.core.estate.unit.UnitSummary;
import hy.oltp.core.estate.TestSecurityConfig;
import hy.oltp.core.estate.building.persistence.BuildingRepository;
import hy.oltp.core.estate.building.service.BuildingService;
import hy.oltp.core.estate.lease.persistence.LeaseRepository;
import hy.oltp.core.estate.lease.service.LeaseMapper;
import hy.oltp.core.estate.tenant.PostgreSqlTestBase;
import hy.oltp.core.estate.tenant.persistence.TenantRepository;
import hy.oltp.core.estate.tenant.service.TenantService;
import hy.oltp.core.estate.unit.persistence.UnitRepository;
import hy.oltp.core.estate.unit.service.UnitService;
import reactor.core.publisher.Mono;

@SpringBootTest(
  webEnvironment = WebEnvironment.RANDOM_PORT,
  classes = {TestSecurityConfig.class},
  properties = {"logging.level.hy=DEBUG", "spring.jpa.hibernate.ddl-auto=update"})
class LeaseControllerImplTest extends PostgreSqlTestBase {
  private static final String ROOT_PATH = "/estate/lease";
  private final LeaseMapper mapper = LeaseMapper.INSTANCE;

  @Autowired
  private WebTestClient client;

  @Autowired
  private LeaseRepository repository;
  @Autowired
  private BuildingRepository buildingRepository;
  @Autowired
  private UnitRepository unitRepository;
  @Autowired
  private TenantRepository tenantRepository;

  @Autowired
  private BuildingService buildingService;
  @Autowired
  private UnitService unitService;
  @Autowired
  private TenantService tenantService;

  private LeaseDetail leaseDetail1 = new LeaseDetail(Instant.now(), Instant.now(), 10.0, 10.0, LeaseStatus.ACTIVE);
  private LeaseDetail leaseDetail2 = new LeaseDetail(Instant.now(), Instant.now(), 11.0, 11.0, LeaseStatus.EXPIRED);
  private Lease sourceLease;
  private Lease savedLease;

  private Building building = new Building(1, "name", "address", "city", "state", "zipCode", "description");
  private UnitDetail unitDetail = new UnitDetail(1, new BigDecimal(1));
  private UnitSummary unitSummary = new UnitSummary("b101", 1, UnitStatus.AVAILABLE);
  private Unit unit1 = new Unit(0, unitSummary, unitDetail);
  private Unit unit2 = new Unit(0, unitSummary, unitDetail);
  private Tenant tenant1 = new Tenant(1, "firstName", "lastName", "lease@email.com", "01011111111");
  private Tenant tenant2 = new Tenant(2, "firstName2", "lastName2", "lease2@email.com", "01022222222");

  private Building savedBuilding;
  private Unit savedUnit1;
  private Unit savedUnit2;
  private Tenant savedTenant1;
  private Tenant savedTenant2;

  @BeforeEach
  void setUp() {
    repository.deleteAll();
    buildingRepository.deleteAll();
    unitRepository.deleteAll();
    tenantRepository.deleteAll();

    savedBuilding = buildingService.createBuilding(building);
    unit1.setBuilding(savedBuilding);
    unit2.setBuilding(savedBuilding);
    savedUnit1 = unitService.createUnit(unit1);
    savedUnit2 = unitService.createUnit(unit2);
    savedTenant1 = tenantService.createTenant(tenant1);
    savedTenant2 = tenantService.createTenant(tenant2);

    sourceLease = new Lease(0, leaseDetail1);
    sourceLease.setTenant(savedTenant1);
    sourceLease.setUnit(savedUnit1);
    sourceLease.getUnit()
      .setBuilding(null);

    savedLease = postAndVerify(sourceLease, HttpStatus.OK);
    assertThat(savedLease.getLeaseDetail()).isEqualTo(sourceLease.getLeaseDetail());
  }

  @Test
  void postLeaseTest() {
    assertThat(repository.findAll()).hasSize(1);

    Lease newLease = new Lease(0, leaseDetail1);
    newLease.setTenant(savedTenant1);
    newLease.setUnit(savedUnit1);
    var response = postAndVerify(newLease, HttpStatus.OK);

    assertThat(repository.findAll()).hasSize(2);
    assertResponseAndRepository(newLease, response);
  }

  @Test
  void getLeaseTest() {
    var response = getAndVerify("/" + savedLease.getId(), HttpStatus.OK);
    assertResponseAndRepository(sourceLease, response);
  }

  @Test
  void getAllLeasesTest() {
    Lease newLease = new Lease(0, leaseDetail2);
    newLease.setTenant(savedTenant2);
    newLease.setUnit(savedUnit2);
    postAndVerify(newLease, HttpStatus.OK);

    var response = getListAndVerify("/all", HttpStatus.OK);

    assertThat(response).hasSize(2);
    for (var lease : response) {
      if (lease.getId() == savedLease.getId()) {
        assertResponseAndRepository(sourceLease, lease);
      } else {
        assertResponseAndRepository(newLease, lease);
      }
    }

    response = getListAndVerify("/all?tenantId=" + savedTenant1.getId(), HttpStatus.OK);
    assertThat(response).hasSize(1);
    assertResponseAndRepository(sourceLease, response.get(0));

    response = getListAndVerify("/all?unitId=" + savedUnit2.getId(), HttpStatus.OK);
    assertThat(response).hasSize(1);
    assertResponseAndRepository(newLease, response.get(0));
  }

  @Test
  void putLeaseTest() {
    Lease newLease = new Lease(0, leaseDetail2);
    var response = putAndVerify(savedLease.getId(), newLease, HttpStatus.OK);

    assertResponseAndRepository(newLease, response);
  }

  @Test
  void deleteLeaseTest() {
    deleteAndVerify(savedLease.getId(), HttpStatus.OK);
    assertThat(repository.findById(savedLease.getId())).isEmpty();
  }

  private void assertResponseAndRepository(Lease source, Lease response) {
    assertThat(source.getLeaseDetail()).isEqualTo(source.getLeaseDetail());

    var entity = repository.findById(response.getId())
      .get();
    assertThat(mapper.entityToApi(entity.getLeaseDetail())).isEqualTo(source.getLeaseDetail());
  }

  private Lease postAndVerify(Lease lease, HttpStatus expectedStatus) {
    // @formatter:off
    return client.post()
      .uri(ROOT_PATH)
      .accept(APPLICATION_JSON)
      .body(Mono.just(lease), Lease.class)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus)
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody(Lease.class)
      .returnResult()
      .getResponseBody();
    // @formatter:on
  }

  private Lease getAndVerify(String query, HttpStatus expectedStatus) {
    // @formatter:off
    return client.get()
      .uri(ROOT_PATH + query)
      .header("Cache-Control", "no-store")
      .accept(APPLICATION_JSON)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus)
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody(Lease.class)
      .returnResult()
      .getResponseBody();
    // @formatter:on
  }

  private List<Lease> getListAndVerify(String query, HttpStatus expectedStatus) {
    // @formatter:off
    return client.get()
      .uri(ROOT_PATH + query)
      .header("Cache-Control", "no-store")
      .accept(APPLICATION_JSON)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus)
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody(new ParameterizedTypeReference<List<Lease>>() {})
      .returnResult()
      .getResponseBody();
    // @formatter:on
  }

  private Lease putAndVerify(int id, Lease lease, HttpStatus expectedStatus) {
    // @formatter:off
    return client.put()
      .uri(ROOT_PATH + "/" + id)
      .accept(APPLICATION_JSON)
      .body(Mono.just(lease), Lease.class)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus)
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody(Lease.class)
      .returnResult()
      .getResponseBody();
    // @formatter:on
  }

  private WebTestClient.ResponseSpec deleteAndVerify(int id, HttpStatus expectedStatus) {
    // @formatter:off
    return client.delete()
      .uri(ROOT_PATH + "/" + id)
      .accept(APPLICATION_JSON)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus);
    // @formatter:on
  }
}
