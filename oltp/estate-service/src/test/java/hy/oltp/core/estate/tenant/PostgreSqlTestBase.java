package hy.oltp.core.estate.tenant;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class PostgreSqlTestBase {
  @SuppressWarnings({"resource", "rawtypes"})
  private static final JdbcDatabaseContainer database =
    new PostgreSQLContainer<>("postgres:latest").withStartupTimeoutSeconds(300);

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
