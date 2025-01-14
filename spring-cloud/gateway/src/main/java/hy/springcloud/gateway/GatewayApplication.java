package hy.springcloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.core.publisher.Hooks;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		Hooks.enableAutomaticContextPropagation();
		SpringApplication.run(GatewayApplication.class, args);
	}

}
