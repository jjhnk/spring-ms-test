package hy.oltp.core.estate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Hooks;

@SpringBootApplication
@ComponentScan(basePackages = "hy")
@EnableJpaRepositories
@EnableCaching
@Slf4j
public class EstateServiceApplication {
	public static void main(String[] args) {
		Hooks.enableAutomaticContextPropagation();
		ConfigurableApplicationContext ctx = SpringApplication.run(EstateServiceApplication.class, args);

		String datasource = ctx.getEnvironment().getProperty("spring.datasource.url");
		log.info("Estate service connected to DATASOURCE: {}", datasource);
	}
}
