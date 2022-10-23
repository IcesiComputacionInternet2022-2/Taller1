package com.co.edu.icesi.zooWeb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class ZooWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZooWebApplication.class, args);
	}

}
