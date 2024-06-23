package hy.microservices.core.review;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Hooks;

@SpringBootApplication
@ComponentScan("hy")
@Slf4j
public class ReviewServiceApplication {

	public static void main(String[] args) {
		Hooks.enableAutomaticContextPropagation();
		ConfigurableApplicationContext ctx = SpringApplication.run(ReviewServiceApplication.class, args);

		String mysqlUri = ctx.getEnvironment().getProperty("spring.datasource.url");
		log.info("MYSQL connection: {}", mysqlUri);
	}
}
