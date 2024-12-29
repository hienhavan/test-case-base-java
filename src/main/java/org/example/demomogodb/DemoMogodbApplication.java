package org.example.demomogodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DemoMogodbApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoMogodbApplication.class, args);
	}

}
