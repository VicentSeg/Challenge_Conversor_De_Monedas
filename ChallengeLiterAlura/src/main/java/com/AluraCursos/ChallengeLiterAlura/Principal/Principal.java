package com.AluraCursos.ChallengeLiterAlura.Principal;
import com.AluraCursos.ChallengeLiterAlura.Dto.AutoresDto;
import com.AluraCursos.ChallengeLiterAlura.Dto.LibroDto;
import com.AluraCursos.ChallengeLiterAlura.Model.*;
import com.AluraCursos.ChallengeLiterAlura.Repository.IAutorDatosRepository;
import com.AluraCursos.ChallengeLiterAlura.Repository.ILibroDatosRepository;
import com.AluraCursos.ChallengeLiterAlura.Service.ConsumoApi;
import com.AluraCursos.ChallengeLiterAlura.Service.ConvierteDatos;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
@Service
public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private static final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvierteDatos conversor = new ConvierteDatos();
    private ILibroDatosRepository libroDatosRepository;
    private IAutorDatosRepository autorDatosRepository;
    private List<LibroDatos> libroDatos;
    private List<AutorDatos> autorDatos;

    public Principal(ILibroDatosRepository libroDatosRepository, IAutorDatosRepository autorDatosRepository) {
        this.libroDatosRepository = libroDatosRepository;
        this.autorDatosRepository = autorDatosRepository;
    }
    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar Libro en API por título
                    2 - Mostrar libros registrados
                    3 - Buscar libro registrado Por titulo 
                    4 - Mostrar Autores registrados
                    5 - Buscar libro registrado Por Autor
                    6 - Buscar autores vivos en un determinado año
                    7 - Buscar libros por idioma
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion) {
                case 1:
                    buscarLibrosAPI();
                    break;
                case 2:
                    mostrarTodosLosLibros();
                    break;
                case 3:
                    buscarLibroPorTitulo();
                    break;
                case 4:
                    mostrarAutoresRegistrados();
                    break;
                case 5:
                    buscarLibroPorAutor();
                    break;
                case 6:
                    buscarAutoresPorDeterminadaFecha();
                    break;
                case 7:
                    bucarLibrosPorIdioma();
                    break;
            }
        }
    }
    private DatosLibros getDatoslibro() {
        System.out.println("Escribe el nombre del libro que desea buscar");
        var nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "+"));
        Datos datos = conversor.obtenerDatos(json, Datos.class);
        return datos.resultados().stream()
                .filter(datosLibros -> datosLibros.titulo().toUpperCase().contains(nombreLibro.toUpperCase()))
                .findFirst()
                .orElse(null);
    }
    private void buscarLibrosAPI() {
        DatosLibros datosLibros = getDatoslibro();
        if (datosLibros == null) {
            System.out.println("-----------------------------------");
            System.out.println("Libro no Encontrado.");
            System.out.println("-----------------------------------");
            return;
        }
        Optional<LibroDatos> libroYaGuardado = libroDatosRepository.findByTituloLibro(datosLibros.titulo());
        if (libroYaGuardado.isPresent()) {
            System.out.println("-----------------------------------");
            System.out.println("El libro ya esta registrado en el sistema.");
            System.out.println("-----------------------------------");
            // Crear y mostrar DTO del libro guardado
            LibroDto libroDto = new LibroDto(
                    libroYaGuardado.get().getID(),
                    libroYaGuardado.get().getTituloLibro(),
                    libroYaGuardado.get().getAutorDatos().stream()
                            .map(autores -> new AutoresDto(autores.getID(), autores
                                    .getNombreAutor(), autores.getFechaDeNacimiento(), autores
                                    .getFechaDeFallecimiento()))
                            .collect(Collectors.toList()),
                    String.join(", ", libroYaGuardado.get().getIdiomas()),
                    libroYaGuardado.get().getNumeroDeDescargas());
            System.out.println("-----------------------------------");
            System.out.println("Título: " + libroDto.titulo());
            System.out.println("Autor: " + libroDto.autor().stream()
                    .map(AutoresDto::nombreAutor)
                    .collect(Collectors.joining(", ")));
            System.out.println("Idioma: " + libroDto.idiomas());
            System.out.println("Número de descargas: " + libroDto.numeroDeDescargas());
            System.out.println("-----------------------------------");
            return;
        }
        // Si el libro existe procesamos los autores
        List<AutorDatos> autores = datosLibros.autor().stream()
                .map(datosAutor -> {
                    List<AutorDatos> autorList = autorDatosRepository.findByNombreAutor(datosAutor.nombreAutor());
                    if (autorList.isEmpty()) {
                        // Si no se encontró el autor, creamos uno nuevo y lo guardamos
                        AutorDatos nuevoAutor = new AutorDatos();
                        nuevoAutor.setNombreAutor(datosAutor.nombreAutor());
                        nuevoAutor.setFechaDeNacimiento(datosAutor.fechaDeNacimiento());
                        nuevoAutor.setFechaDeFallecimiento(datosAutor.fechaDeFallecimiento());
                        autorDatosRepository.save(nuevoAutor);
                        return nuevoAutor;
                    } else {
                        // Si ya existe el autor, devolvemos el primero de la lista
                        return autorList.get(0);
                    }
                })
                .collect(Collectors.toList());
        // Crear el libro con los datos de datosLibros y añadir los autores
        LibroDatos nuevoLibro = new LibroDatos(datosLibros);
        nuevoLibro.setAutorDatos(autores);
        // Guardar el libro junto con sus autores en la base de datos
        libroDatosRepository.save(nuevoLibro);
        // Crear y mostrar DTO del libro guardado
        LibroDto libroDTO = new LibroDto(
                nuevoLibro.getID(),
                nuevoLibro.getTituloLibro(),
                nuevoLibro.getAutorDatos().stream().map(autor -> new AutoresDto(autor.getID(),
                                autor.getNombreAutor(), autor.getFechaDeNacimiento(),
                                autor.getFechaDeFallecimiento()))
                        .collect(Collectors.toList()),
                String.join(", ", nuevoLibro.getIdiomas()),
                nuevoLibro.getNumeroDeDescargas());
        System.out.println("-----------------------------------");
        System.out.println("Título: " + libroDTO.titulo());
        System.out.println("Autor: " + libroDTO.autor().stream()
                .map(AutoresDto::nombreAutor)
                .collect(Collectors.joining(", ")));
        System.out.println("Idioma: " + libroDTO.idiomas());
        System.out.println("Número de descargas: " + libroDTO.numeroDeDescargas());
        System.out.println("-----------------------------------");
    }
    private void mostrarTodosLosLibros() {
        List<LibroDatos> libros = libroDatosRepository.findAllWithAutores();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos.");
        } else {
            System.out.println("Libros registrados:");
            for (LibroDatos libro : libros) {
                System.out.println("-----------------------------------");
                System.out.println("ID: " + libro.getID());
                System.out.println("Título: " + libro.getTituloLibro());
                System.out.println("Idiomas: " + libro.getIdiomas());
                System.out.println("Número de descargas: " + libro.getNumeroDeDescargas());
                // Mostrar información de los autores
                if (libro.getAutorDatos() != null && !libro.getAutorDatos().isEmpty()) {
                    for (AutorDatos autor : libro.getAutorDatos()) {
                        System.out.println("Autor: " + autor.getNombreAutor());
                        System.out.println("Fecha de nacimiento: " + autor.getFechaDeNacimiento());
                        if (autor.getFechaDeFallecimiento() != null) {
                            System.out.println("Fecha de fallecimiento: " + autor.getFechaDeFallecimiento());
                        } else {
                            System.out.println("Fecha de fallecimiento: Aún con vida");
                        }
                    }
                } else {
                    System.out.println("No se encontró información sobre el autor.");
                }

                System.out.println("-----------------------------------");
            }
        }
    }
    private void buscarLibroPorTitulo() {
        System.out.print("Ingrese el título del libro que desea buscar: ");
        String titulo = teclado.nextLine();
        // Buscar el libro en la base de datos
        List<LibroDatos> librosEncontrados = libroDatosRepository.findByTituloLibroContainingIgnoreCase(titulo);

        if (!librosEncontrados.isEmpty()) {
            System.out.println("Libros encontrados:");
            for (LibroDatos libro : librosEncontrados) {
                System.out.println("-----------------------------------");
                System.out.println("Título: " + libro.getTituloLibro());
                System.out.println("Idiomas: " + libro.getIdiomas());
                System.out.println("Número de descargas: " + libro.getNumeroDeDescargas());
                System.out.println("-----------------------------------");
            }
        } else {
            System.out.println("El libro '" + titulo + "' no fue encontrado en la base de datos.");
        }
    }
    public void mostrarAutoresRegistrados() {
        List<AutorDatos> autores = autorDatosRepository.findAllWithLibros();

        if (autores.isEmpty()) {
            System.out.println("-----------------------------------");
            System.out.println("No hay autores registrados en la base de datos.");
            System.out.println("-----------------------------------");
        } else {
            System.out.println("Autores registrados:");
            for (AutorDatos autor : autores) {
                System.out.println("-----------------------------------");
                System.out.println("Autor: " + autor.getNombreAutor());
                System.out.println("Libros:");
                for (LibroDatos libro : autor.getLibrosDeAutores()) {
                    System.out.println(" - " + libro.getTituloLibro());
                    System.out.println("-----------------------------------");
                }
            }
        }

    }
    private void buscarLibroPorAutor() {
        System.out.print("Ingrese el nombre o apellido del autor para mostrar sus libros: ");
        String nombreAutor = teclado.nextLine().trim();
        Optional<AutorDatos> autorOptional = autorDatosRepository.findByNombreWithLibros(nombreAutor);
        if (autorOptional.isPresent()) {
            AutorDatos autor = autorOptional.get();
            System.out.println("-----------------------------------");
            System.out.println("Autor encontrado: " + autor.getNombreAutor());
            System.out.println("Libros del autor:");
            for (LibroDatos libro : autor.getLibrosDeAutores()) {
                System.out.println(" - " + libro.getTituloLibro());
                System.out.println("-----------------------------------");
            }
        } else {
            System.out.println("No se encontró un autor con el nombre: " + nombreAutor);
        }
    }
    private void buscarAutoresPorDeterminadaFecha(){
        System.out.print("Ingrese el año para listar autores vivos: ");
        int anio = Integer.parseInt(teclado.nextLine());

        List<AutorDatos> autoresVivos = autorDatosRepository.findAutoresVivosEnAnio(anio);

        if (autoresVivos.isEmpty()) {
            System.out.println("-----------------------------------");
            System.out.println("No se encontraron autores vivos en el año " + anio + ".");
            System.out.println("-----------------------------------");
        } else {
            System.out.println("Autores vivos en el año " + anio + ":");
            for (AutorDatos autor : autoresVivos) {
                System.out.println("-----------------------------------");
                System.out.println(" - " + autor.getNombreAutor() + " (Nacido: " + autor.getFechaDeNacimiento() + ")");
                System.out.println("-----------------------------------");
            }
        }
    }
    private void bucarLibrosPorIdioma(){

        Map<String, String> idiomas = Map.of(
                "es", "Español",
                "it", "Italiano",
                "en", "Inglés",
                "ja", "Japonés",
                "fr", "Francés",
                "pt", "Portugués",
                "ru", "Ruso",
                "zh", "Chino Mandarín",
                "de", "Alemán",
                "ar", "Árabe"
        );

        System.out.println("Idiomas disponibles:");
        idiomas.forEach((codigo, nombre) -> System.out.println(codigo + " - " + nombre));
        System.out.print("Ingrese el código de idioma (ejemplo: es, en, fr): ");
        String codigoIdioma = teclado.nextLine();
        // Verificar si el idioma es válido
        if (idiomas.containsKey(codigoIdioma)) {
            List<LibroDatos> libros = libroDatosRepository.findByIdioma(codigoIdioma);

            if (libros.isEmpty()) {
                System.out.println("No se encontraron libros en el idioma " + idiomas.get(codigoIdioma));
            } else {
                System.out.println("Libros en " + idiomas.get(codigoIdioma) + ":");
                for (LibroDatos libro : libros) {
                    System.out.println("-----------------------------------");
                    System.out.println("ID: " + libro.getID());
                    System.out.println("Título: " + libro.getTituloLibro());
                    System.out.println("Idiomas: " + libro.getIdiomas());
                    System.out.println("Número de descargas: " + libro.getNumeroDeDescargas());
                    System.out.println("-----------------------------------");
                }
            }
        } else {
            System.out.println("El código de idioma ingresado no es válido.");
        }
    }
}
