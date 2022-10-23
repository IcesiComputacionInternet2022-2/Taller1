package co.edu.icesi.CaliZoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class CaliZooApplication {

	public static void main(String[] args) {
		SpringApplication.run(CaliZooApplication.class, args);
	}

}
