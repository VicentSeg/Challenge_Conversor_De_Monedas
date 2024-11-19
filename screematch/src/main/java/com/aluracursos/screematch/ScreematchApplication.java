package com.aluracursos.screematch;

import com.aluracursos.screematch.Principal.EjemploStreams;
import com.aluracursos.screematch.Principal.Principal;
import com.aluracursos.screematch.model.DatosEpisodio;
import com.aluracursos.screematch.model.DatosSerie;
import com.aluracursos.screematch.model.DatosTemporadas;
import com.aluracursos.screematch.service.ConsumoApi;
import com.aluracursos.screematch.service.ConvierteDatos;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreematchApplication implements CommandLineRunner {

	public static void main(String[] args) {

		SpringApplication.run(ScreematchApplication.class, args);

	}

		@Override
		public void run(String... args) throws Exception {
			Principal principal = new Principal();
			 principal.muestraElMenu();
			//EjemploStreams ejemploStreams = new EjemploStreams();
			//ejemploStreams.muestraEjemplo();


		}
}
