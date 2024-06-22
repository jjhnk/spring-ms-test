package hy.springcloud.eurekaserver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {"spring.cloud.config.enabled=false"})
class EurekaServerApplicationTests {

  private TestRestTemplate testRestTemplate;

  @Value("${app.eureka.username}")
  private String username;
  @Value("${app.eureka.password}")
  private String password;

  @Test
	void contextLoads() {
	}

  @Autowired
  void setTestRestTemplate(TestRestTemplate testRestTemplate) {
    this.testRestTemplate = testRestTemplate.withBasicAuth(username, password);
  }

  @Test
  void catalogLoads() {
    String expectedResponseBody = "{\"applications\":{\"versions__delta\":\"1\",\"apps__hashcode\":\"\",\"application\":[]}}";
    ResponseEntity<String> entity = testRestTemplate.getForEntity("/eureka/apps", String.class);
    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(entity.getBody()).isEqualTo(expectedResponseBody);
  }

  @Test
  void healthy() {
    String expectedResponseBody = "{\"status\":\"UP\"}";
    ResponseEntity<String> entity = testRestTemplate.getForEntity("/actuator/health", String.class);
    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(entity.getBody()).isEqualTo(expectedResponseBody);
  }
}
