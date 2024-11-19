package com.AluraCursos.ActividadSpring;

import com.AluraCursos.ActividadSpring.Principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ActividadSpringApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ActividadSpringApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

			Principal principal = new Principal();
			principal.muestraElMenu();
	}
}
