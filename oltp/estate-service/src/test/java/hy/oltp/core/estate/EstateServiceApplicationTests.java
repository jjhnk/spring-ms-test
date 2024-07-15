package hy.oltp.core.estate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(
	webEnvironment = WebEnvironment.RANDOM_PORT,
	classes = {TestSecurityConfig.class})
class EstateServiceApplicationTests {

	@Test
	void contextLoads() {}

}
