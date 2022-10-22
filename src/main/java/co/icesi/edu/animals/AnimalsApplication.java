package co.icesi.edu.animals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class AnimalsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnimalsApplication.class, args);
	}

}
