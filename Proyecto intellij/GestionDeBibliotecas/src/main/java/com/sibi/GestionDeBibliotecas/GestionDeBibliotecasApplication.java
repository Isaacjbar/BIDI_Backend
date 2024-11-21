package com.sibi.GestionDeBibliotecas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GestionDeBibliotecasApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionDeBibliotecasApplication.class, args);
	}
}