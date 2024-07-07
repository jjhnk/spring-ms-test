package hy.oltp.core.estate;

import org.springframework.boot.SpringApplication;

public class TestEstateApplication {

	public static void main(String[] args) {
		SpringApplication.from(EstateServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
