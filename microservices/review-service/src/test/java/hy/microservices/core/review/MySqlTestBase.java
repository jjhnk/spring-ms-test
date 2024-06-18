package hy.microservices.core.review;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;

public abstract class MySqlTestBase {
  @SuppressWarnings({"resource", "rawtypes"})
  private static final JdbcDatabaseContainer database =
      new MySQLContainer<>("mysql:8.0.37").withStartupTimeoutSeconds(300);

  static {
    database.start();
  }

  @DynamicPropertySource
  static void databaseProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", database::getJdbcUrl);
    registry.add("spring.datasource.username", database::getUsername);
    registry.add("spring.datasource.password", database::getPassword);
  }
}
