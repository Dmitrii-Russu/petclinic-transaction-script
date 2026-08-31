package dev.dmitriirussu.petclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class PetclinicApplication {
	public static void main(String[] args) {
		SpringApplication.run(PetclinicApplication.class, args);
	}
}
