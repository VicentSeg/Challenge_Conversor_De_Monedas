package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = System.getenv("API_KEY");
    //API_KEY ="colocar tu Api_key";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosSerie> datosSerie = new ArrayList<>();
    private SerieRepository repositorio;
    private List<Serie> series;
    private Optional<Serie> serieBuscada;

    public Principal(SerieRepository repository) {
        this.repositorio = repository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series 
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                    4 - Buscar Series por titulo
                    5 - Top 5 mejores series
                    6 - Buscar series por genero
                    7 - filtrar series
                    8 - Buscar episodios por nombre
                    9 - Top 5 Episodios por serie
                                  
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrasSeriesBuscadas();
                    break;
                case 4:
                    bucarSeriesPorTitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarSeriesPorGenero();
                    break;
                case 7:
                    filtrarSeriesPorTemporadaYEvaluacion();
                    break;
                case 8:
                    bucarEpisodiosPorTitulo();
                    break;
                case 9:
                    buscarTop5Episodios();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }
    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }
    private void buscarEpisodioPorSerie() {
//        DatosSerie datosSerie = getDatosSerie();
//        busca los episodios directo de la api omdb, se comenta porque ya lo vamos hacer directo de nuestra base de datos
        mostrasSeriesBuscadas();
        System.out.println("Escribe el nombre de la serie de la cual quieres ver los episodios: ");
        var nombreSerie = teclado.nextLine();
        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();
        if(serie.isPresent()){
            var serieEncontrada = serie.get();
            List<DatosTemporadas> temporadas = new ArrayList<>();
            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d ->d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        }
    }
    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        Serie serie = new Serie(datos);
        repositorio.save(serie);// se pasan los datos que buscamos de la lista
        //datosSerie.add(datos);  se comenta porque ya se esta guardando en la base de datos.
        System.out.println(datos);
    }
    private void mostrasSeriesBuscadas() {
//        List<Serie> series = new ArrayList<>();
//        series = datosSerie.stream()
//                .map (d -> new Serie(d))
//                .collect(Collectors.toList());
        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
    private void bucarSeriesPorTitulo(){
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
         serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);

        if(serieBuscada.isPresent()){
            System.out.println("La serie buscada es: " + serieBuscada.get());
        }else {
            System.out.println("Serie no encontrada");
        }
    }
    private void buscarTop5Series() {
        List<Serie> topSeries = repositorio.findTop5ByOrderByEvaluacionDesc();
        topSeries.forEach(s ->
                System.out.println("Serie: " + s.getTitulo() + " Evalucion: " + s.getEvaluacion()));
    }
    private void buscarSeriesPorGenero () {
        System.out.println("Escribe el genero/categoria de la serie que deseas buscar");
        var genero = teclado.nextLine();
        var categoria = Categoria.fromEspanol(genero);
        List<Serie> seriesPorGenero = repositorio.findByGenero(categoria);
        System.out.println("Las serie del genero: " + genero);
        seriesPorGenero.forEach(System.out::println);

    }
    public void filtrarSeriesPorTemporadaYEvaluacion(){
        System.out.println("¿Filtrar séries con cuántas temporadas? ");
        var totalTemporadas = teclado.nextInt();
        teclado.nextLine();
        System.out.println("¿Com evaluación apartir de cuál valor? ");
        var evaluacion = teclado.nextDouble();
        teclado.nextLine();
        List<Serie> filtroSeries = repositorio.seriesPorTemporadaYEvaluacion(totalTemporadas,evaluacion);
        System.out.println("*** Series filtradas ***");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + "  - evaluacion: " + s.getEvaluacion()));
    }
    private void bucarEpisodiosPorTitulo(){
        System.out.println("Escribe el nombre del episodio que deas buscar: ");
        var nombreEpisodio = teclado.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorNombre((nombreEpisodio));
        episodiosEncontrados.forEach(e -> System.out.printf("Serie: %s  Temporada %s Episodio %s Evaluacion %s\n",
                e.getSerie(), e.getTemporada(), e.getNumeroEpisodio(), e.getEvaluacion()));
    }
    private void buscarTop5Episodios(){
         bucarSeriesPorTitulo();
         if(serieBuscada.isPresent()){
             Serie serie = serieBuscada.get();
             List<Episodio> topEpisodios = repositorio.top5Episodios(serie);
             topEpisodios.forEach(e -> System.out.printf("Serie: %s  Temporada %s Episodio %s Evaluacion %s\n",
                     e.getSerie(), e.getTemporada(), e.getTitulo(), e.getEvaluacion()));
         }
    }
}

