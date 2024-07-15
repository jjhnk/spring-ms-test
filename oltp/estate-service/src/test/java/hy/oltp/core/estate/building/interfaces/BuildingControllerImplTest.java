package hy.oltp.core.estate.building.interfaces;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

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
import hy.oltp.core.estate.TestSecurityConfig;
import hy.oltp.core.estate.building.persistence.BuildingRepository;
import hy.oltp.core.estate.tenant.PostgreSqlTestBase;
import reactor.core.publisher.Mono;

@SpringBootTest(
  webEnvironment = WebEnvironment.RANDOM_PORT,
  classes = {TestSecurityConfig.class},
  properties = {"logging.level.hy=DEBUG", "spring.jpa.hibernate.ddl-auto=update"})
class BuildingControllerImplTest extends PostgreSqlTestBase {
  private static final String ROOT_PATH = "/estate/building";

  @Autowired
  private WebTestClient client;

  @Autowired
  private BuildingRepository repository;

  private Building sourceBuilding;
  private Building respondedBuilding;

  @BeforeEach
  void setUp() {
    repository.deleteAll();
    sourceBuilding = new Building(1, "name", "address", "city", "state", "zipCode", "description");
    respondedBuilding = postAndVerify(sourceBuilding, HttpStatus.OK);

    assertThat(respondedBuilding.getName()).isEqualTo(sourceBuilding.getName());
    assertThat(respondedBuilding.getAddress()).isEqualTo(sourceBuilding.getAddress());
    assertThat(respondedBuilding.getCity()).isEqualTo(sourceBuilding.getCity());
    assertThat(respondedBuilding.getState()).isEqualTo(sourceBuilding.getState());
    assertThat(respondedBuilding.getZipCode()).isEqualTo(sourceBuilding.getZipCode());
    assertThat(respondedBuilding.getDescription()).isEqualTo(sourceBuilding.getDescription());
  }

  @Test
  void postBuildingTest() {
    assertThat(repository.findAll()).hasSize(1);

    Building newBuilding = new Building(0, "name", "address", "city", "state", "zipCode", "description");
    var response = postAndVerify(newBuilding, HttpStatus.OK);

    assertThat(repository.findAll()).hasSize(2);
    assertResponseAndRepository(newBuilding, response);
  }

  @Test
  void getBuildingTest() {
    var response = getAndVerify("/" + respondedBuilding.getId(), HttpStatus.OK);

    assertResponseAndRepository(sourceBuilding, response);
  }

  @Test
  void getAllBuildingTest() {
    Building newBuilding = new Building(0, "name", "address", "city", "state", "zipCode", "description");
    postAndVerify(newBuilding, HttpStatus.OK);

    var response = getListAndVerify("/all", HttpStatus.OK);

    assertThat(response).hasSize(2);
    for (var building : response) {
      assertResponseAndRepository(newBuilding, building);
    }
  }

  @Test
  void putBuildingTest() {
    Building newBuilding = new Building(0, "name2", "address2", "city2", "state2", "zipCode2", "description2");
    var response = putAndVerify(respondedBuilding.getId(), newBuilding, HttpStatus.OK);

    assertResponseAndRepository(newBuilding, response);
  }

  @Test
  void deleteBuildingTest() {
    deleteAndVerify(respondedBuilding.getId(), HttpStatus.OK);
    assertThat(repository.findById(respondedBuilding.getId())).isEmpty();
  }

  private void assertResponseAndRepository(Building source, Building response) {
    assertThat(response.getName()).isEqualTo(source.getName());
    assertThat(response.getAddress()).isEqualTo(source.getAddress());
    assertThat(response.getCity()).isEqualTo(source.getCity());
    assertThat(response.getState()).isEqualTo(source.getState());
    assertThat(response.getZipCode()).isEqualTo(source.getZipCode());
    assertThat(response.getDescription()).isEqualTo(source.getDescription());

    var entity = repository.findById(response.getId())
      .get();
    assertThat(entity.getName()).isEqualTo(source.getName());
    assertThat(entity.getAddress()).isEqualTo(source.getAddress());
    assertThat(entity.getCity()).isEqualTo(source.getCity());
    assertThat(entity.getState()).isEqualTo(source.getState());
    assertThat(entity.getZipCode()).isEqualTo(source.getZipCode());
    assertThat(entity.getDescription()).isEqualTo(source.getDescription());
  }

  private Building postAndVerify(Building building, HttpStatus expectedStatus) {
    // @formatter:off
    return client.post()
      .uri(ROOT_PATH)
      .accept(APPLICATION_JSON)
      .body(Mono.just(building), Building.class)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus)
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody(Building.class)
      .returnResult()
      .getResponseBody();
    // @formatter:on
  }

  private Building getAndVerify(String query, HttpStatus expectedStatus) {
    // @formatter:off
		return client.get()
			.uri(ROOT_PATH + query)
			.header("Cache-Control", "no-store")
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isEqualTo(expectedStatus)
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(Building.class)
      .returnResult()
      .getResponseBody();
    // @formatter:on
  }

  private List<Building> getListAndVerify(String query, HttpStatus expectedStatus) {
    // @formatter:off
		return client.get()
			.uri(ROOT_PATH + query)
			.header("Cache-Control", "no-store")
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isEqualTo(expectedStatus)
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(new ParameterizedTypeReference<List<Building>>() {})
      .returnResult()
      .getResponseBody();
    // @formatter:on
  }

  private Building putAndVerify(int id, Building building, HttpStatus expectedStatus) {
    // @formatter:off
    return client.put()
      .uri(ROOT_PATH + "/" + id)
      .accept(APPLICATION_JSON)
      .body(Mono.just(building), Building.class)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus)
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody(Building.class)
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
