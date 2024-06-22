package hy.springcloud.authorizationserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AuthorizationServerApplication {
	private static final Logger LOG = LoggerFactory.getLogger(AuthorizationServerApplication.class);

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(AuthorizationServerApplication.class, args);
		LOG.info("Config Server URI: {}", ctx.getEnvironment().getProperty("spring.cloud.config.uri"));
	}

}
