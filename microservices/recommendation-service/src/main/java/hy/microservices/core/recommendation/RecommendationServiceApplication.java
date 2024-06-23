package hy.microservices.core.recommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Hooks;

@SpringBootApplication
@ComponentScan("hy")
@Slf4j
public class RecommendationServiceApplication {

	public static void main(String[] args) {
		Hooks.enableAutomaticContextPropagation();
		ConfigurableApplicationContext ctx = SpringApplication.run(RecommendationServiceApplication.class, args);

		String mongoDbHost = ctx.getEnvironment().getProperty("spring.data.mongodb.host");
		String mongoDbPort = ctx.getEnvironment().getProperty("spring.data.mongodb.port");

		log.info("MongoDb connection: {}", mongoDbHost + mongoDbPort);
	}

}
