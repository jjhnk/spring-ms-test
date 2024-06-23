package hy.microservices.composite.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import reactor.core.publisher.Hooks;

@SpringBootApplication
@ComponentScan(basePackages = "hy")
public class ProductCompositeApplication {

	public static void main(String[] args) {
		Hooks.enableAutomaticContextPropagation();
		SpringApplication.run(ProductCompositeApplication.class, args);
	}

}
