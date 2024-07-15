package hy.oltp.core.estate.tenant.interfaces;

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

import hy.api.core.estate.tenant.Tenant;
import hy.oltp.core.estate.TestSecurityConfig;
import hy.oltp.core.estate.tenant.PostgreSqlTestBase;
import hy.oltp.core.estate.tenant.persistence.TenantRepository;
import reactor.core.publisher.Mono;

@SpringBootTest(
  webEnvironment = WebEnvironment.RANDOM_PORT,
  classes = {TestSecurityConfig.class},
  properties = {"logging.level.hy=DEBUG", "spring.jpa.hibernate.ddl-auto=update"})
class TenantControllerImplTest extends PostgreSqlTestBase {
  private static final String ROOT_PATH = "/estate/tenant";

  @Autowired
  private WebTestClient client;

  @Autowired
  private TenantRepository repository;

  private Tenant sourceTenant;
  private Tenant respondedTenant;

  @BeforeEach
  void setUp() {
    repository.deleteAll();
    sourceTenant = new Tenant(1, "firstName", "lastName", "email@email.com", "01011111111");
    respondedTenant = postAndVerify(sourceTenant, HttpStatus.OK);

    assertThat(respondedTenant.getFirstName()).isEqualTo(sourceTenant.getFirstName());
    assertThat(respondedTenant.getLastName()).isEqualTo(sourceTenant.getLastName());
    assertThat(respondedTenant.getEmail()).isEqualTo(sourceTenant.getEmail());
    assertThat(respondedTenant.getPhoneNumber()).isEqualTo(sourceTenant.getPhoneNumber());
  }

  @Test
  void postTenantTest() {
    assertThat(repository.findAll()).hasSize(1);

    Tenant newTenant = new Tenant(0, "newFirstName", "newLastName", "newEmail@email.com", "01022222222");
    var response = postAndVerify(newTenant, HttpStatus.OK);

    assertThat(repository.findAll()).hasSize(2);
    assertResponseAndRepository(newTenant, response);
  }

  @Test
  void getTenantTest() {
    var response = getAndVerify("/" + respondedTenant.getId(), HttpStatus.OK);

    assertResponseAndRepository(sourceTenant, response);
  }

  @Test
  void getAllTenantsTest() {
    Tenant newTenant = new Tenant(0, "newFirstName", "newLastName", "newEmail@email.com", "01022222222");
    postAndVerify(newTenant, HttpStatus.OK);

    var response = getListAndVerify("/all", HttpStatus.OK);

    assertThat(response).hasSize(2);
    for (var tenant : response) {
      if (tenant.getId() == respondedTenant.getId()) {
        assertResponseAndRepository(respondedTenant, tenant);
      } else {
        assertResponseAndRepository(newTenant, tenant);
      }

    }
  }

  @Test
  void putTenantTest() {
    Tenant newTenant = new Tenant(0, "newFirstName", "newLastName", "newEmail@email.com", "01022222222");
    var response = putAndVerify(respondedTenant.getId(), newTenant, HttpStatus.OK);

    assertResponseAndRepository(newTenant, response);
  }

  @Test
  void deleteTenantTest() {
    deleteAndVerify(respondedTenant.getId(), HttpStatus.OK);
    assertThat(repository.findById(respondedTenant.getId())).isEmpty();
  }

  private void assertResponseAndRepository(Tenant source, Tenant response) {
    assertThat(response.getFirstName()).isEqualTo(source.getFirstName());
    assertThat(response.getLastName()).isEqualTo(source.getLastName());
    assertThat(response.getEmail()).isEqualTo(source.getEmail());
    assertThat(response.getPhoneNumber()).isEqualTo(source.getPhoneNumber());

    var entity = repository.findById(response.getId())
      .get();
    assertThat(entity.getFirstName()).isEqualTo(source.getFirstName());
    assertThat(entity.getLastName()).isEqualTo(source.getLastName());
    assertThat(entity.getEmail()).isEqualTo(source.getEmail());
    assertThat(entity.getPhoneNumber()).isEqualTo(source.getPhoneNumber());
  }

  private Tenant postAndVerify(Tenant tenant, HttpStatus expectedStatus) {
    // @formatter:off
    return client.post()
      .uri(ROOT_PATH)
      .accept(APPLICATION_JSON)
      .body(Mono.just(tenant), Tenant.class)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus)
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody(Tenant.class)
      .returnResult()
      .getResponseBody();
    // @formatter:on
  }

  private Tenant getAndVerify(String query, HttpStatus expectedStatus) {
    // @formatter:off
    return client.get()
      .uri(ROOT_PATH + query)
      .header("Cache-Control", "no-store")
      .accept(APPLICATION_JSON)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus)
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody(Tenant.class)
      .returnResult()
      .getResponseBody();
    // @formatter:on
  }

  private List<Tenant> getListAndVerify(String query, HttpStatus expectedStatus) {
    // @formatter:off
    return client.get()
      .uri(ROOT_PATH + query)
      .header("Cache-Control", "no-store")
      .accept(APPLICATION_JSON)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus)
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody(new ParameterizedTypeReference<List<Tenant>>() {})
      .returnResult()
      .getResponseBody();
    // @formatter:on
  }

  private Tenant putAndVerify(int id, Tenant tenant, HttpStatus expectedStatus) {
    // @formatter:off
    return client.put()
      .uri(ROOT_PATH + "/" + id)
      .accept(APPLICATION_JSON)
      .body(Mono.just(tenant), Tenant.class)
      .exchange()
      .expectStatus().isEqualTo(expectedStatus)
      .expectHeader().contentType(APPLICATION_JSON)
      .expectBody(Tenant.class)
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
