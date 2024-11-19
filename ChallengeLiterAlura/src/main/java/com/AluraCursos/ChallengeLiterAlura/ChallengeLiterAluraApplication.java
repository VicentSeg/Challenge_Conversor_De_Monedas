package com.AluraCursos.ChallengeLiterAlura;
import com.AluraCursos.ChallengeLiterAlura.Principal.Principal;
import com.AluraCursos.ChallengeLiterAlura.Repository.IAutorDatosRepository;
import com.AluraCursos.ChallengeLiterAlura.Repository.ILibroDatosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChallengeLiterAluraApplication implements CommandLineRunner {

	@Autowired
	private ILibroDatosRepository libroDatosRepository;

	@Autowired
	private IAutorDatosRepository autorDatosRepository;

	@Autowired
	private Principal principal;  // Inyecta la instancia de Principal

	public static void main(String[] args) {
		SpringApplication.run(ChallengeLiterAluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		principal.muestraElMenu();
	}
}

