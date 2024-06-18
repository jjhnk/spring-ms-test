package hy.microservices.core.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@ComponentScan(basePackages = "hy")
@Slf4j
public class ProductServiceApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(ProductServiceApplication.class, args);

		String mongoDbHost = ctx.getEnvironment().getProperty("spring.data.mongodb.host");
		String mongoDbPort = ctx.getEnvironment().getProperty("spring.data.mongodb.port");

		log.info("MongoDb connection: {}", mongoDbHost + mongoDbPort);
	}
}
