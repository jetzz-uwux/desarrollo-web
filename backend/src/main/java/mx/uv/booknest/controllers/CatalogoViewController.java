package mx.uv.booknest.controllers;

import mx.uv.booknest.service.BookService;
import mx.uv.booknest.service.LiterarySeriesService;
import mx.uv.booknest.service.AuthorService;
import mx.uv.booknest.service.GenreService;
import mx.uv.booknest.service.RatingService;
import mx.uv.booknest.service.ReviewService;
import mx.uv.booknest.service.BookFileService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.LinkedHashMap;

import mx.uv.booknest.models.Author;
import mx.uv.booknest.models.Book;
import mx.uv.booknest.models.Genre;
import mx.uv.booknest.models.BookAuthor;
import mx.uv.booknest.models.BookGenre;
import mx.uv.booknest.models.BookSeries;
import mx.uv.booknest.models.LiterarySeries;
import mx.uv.booknest.models.Review;

import mx.uv.booknest.repository.BookSeriesRepository;
import mx.uv.booknest.repository.AlternativeTitleRepository;
import mx.uv.booknest.repository.AuthorRepository;
import mx.uv.booknest.repository.GenreRepository;
import mx.uv.booknest.repository.BookAuthorRepository;
import mx.uv.booknest.repository.BookGenreRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CatalogoViewController {

    private final BookService bookService;
    private final LiterarySeriesService seriesService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final RatingService ratingService;
    private final ReviewService reviewService;
    private final BookFileService bookFileService;

    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final BookGenreRepository bookGenreRepository;
    private final BookSeriesRepository bookSeriesRepository;
    private final AlternativeTitleRepository alternativeTitleRepository;


    public CatalogoViewController(
            BookService bookService,
            LiterarySeriesService seriesService,
            AuthorService authorService,
            GenreService genreService,
            AuthorRepository authorRepository,
            GenreRepository genreRepository,
            BookAuthorRepository bookAuthorRepository,
            BookGenreRepository bookGenreRepository,
            BookSeriesRepository bookSeriesRepository,
            AlternativeTitleRepository alternativeTitleRepository,
            RatingService ratingService,
            ReviewService reviewService,
            BookFileService bookFileService) {

        this.bookService = bookService;
        this.seriesService = seriesService;
        this.authorService = authorService;
        this.genreService = genreService;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookAuthorRepository = bookAuthorRepository;
        this.bookGenreRepository = bookGenreRepository;
        this.bookSeriesRepository = bookSeriesRepository;
        this.alternativeTitleRepository = alternativeTitleRepository;
        this.ratingService = ratingService;
        this.reviewService = reviewService;
        this.bookFileService = bookFileService;
    }


    // PÁGINA 1: catálogo con buscador
   
    @GetMapping("/catalogo")
    public String mostrarCatalogo(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer minPages,
            @RequestParam(required = false) Integer maxPages,
            @RequestParam(required = false) Long genreId,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String seriesType,
            @RequestParam(required = false) String language,
            Model model) {

            model.addAttribute("libros",
                    bookService.listar(
                            q, minPages, maxPages,
                            genreId, authorId, sort, seriesType, language
                    ));

        model.addAttribute("autores", authorService.listar());
        model.addAttribute("generos", genreService.listar());

        model.addAttribute("q", q);
        model.addAttribute("minPages", minPages);
        model.addAttribute("maxPages", maxPages);
        model.addAttribute("genreId", genreId);
        model.addAttribute("authorId", authorId);
        model.addAttribute("sort", sort);
        model.addAttribute("seriesType", seriesType);
        model.addAttribute("language", language);

        Map<Long, String> nombresAutores = new HashMap<>();
        Map<Long, String> nombresGeneros = new HashMap<>();

        // Obtener nombres de autores por ID
        Map<Long, String> autoresPorId = new HashMap<>();
        for (Author autor : authorRepository.findAll()) {
            autoresPorId.put(autor.getId(), autor.getName());
        }

        // Obtener nombres de géneros por ID
        Map<Long, String> generosPorId = new HashMap<>();
        for (Genre genero : genreRepository.findAll()) {
            generosPorId.put(genero.getId(), genero.getName());
        }

        // Relacionar libros con sus autores
        for (BookAuthor relacion : bookAuthorRepository.findAll()) {
            String nombre = autoresPorId.get(relacion.getAuthorId());

            if (nombre != null) {
                nombresAutores.merge(
                    relacion.getBookId(),
                    nombre,
                    (anterior, nuevo) -> anterior + ", " + nuevo
                );
            }
        }

        // Relacionar libros con sus géneros
        for (BookGenre relacion : bookGenreRepository.findAll()) {
            String nombre = generosPorId.get(relacion.getGenreId());

            if (nombre != null) {
                nombresGeneros.merge(
                    relacion.getBookId(),
                    nombre,
                    (anterior, nuevo) -> anterior + ", " + nuevo
                );
            }
        }

        // Enviar la información a Thymeleaf
        model.addAttribute("nombresAutores", nombresAutores);
        model.addAttribute("nombresGeneros", nombresGeneros);

        return "catalogo";
    }


    // PÁGINA 2: ficha de un libro
    @GetMapping("/catalogo/libros/{id}")
    public String mostrarLibro(@PathVariable Long id, Model model) {
        model.addAttribute("libro", bookService.obtenerPorId(id));
        model.addAttribute("titulosAlternativos", alternativeTitleRepository.findByBookId(id));

        Map<Long, String> autoresPorId = new HashMap<>();
        for (Author autor : authorRepository.findAll()) {
            autoresPorId.put(autor.getId(), autor.getName());
        }

        Map<Long, String> generosPorId = new HashMap<>();
        for (Genre genero : genreRepository.findAll()) {
            generosPorId.put(genero.getId(), genero.getName());
        }

        StringBuilder nombresAutores = new StringBuilder();
        for (BookAuthor relacion : bookAuthorRepository.findAll()) {
            if (relacion.getBookId().equals(id)) {
                String nombre = autoresPorId.get(relacion.getAuthorId());
                if (nombre != null) {
                    if (nombresAutores.length() > 0) {
                        nombresAutores.append(", ");
                    }
                    nombresAutores.append(nombre);
                }
            }
        }

        StringBuilder nombresGeneros = new StringBuilder();
        for (BookGenre relacion : bookGenreRepository.findAll()) {
            if (relacion.getBookId().equals(id)) {
                String nombre = generosPorId.get(relacion.getGenreId());
                if (nombre != null) {
                    if (nombresGeneros.length() > 0) {
                        nombresGeneros.append(", ");
                    }
                    nombresGeneros.append(nombre);
                }
            }
        }

        model.addAttribute("autoresLibro",
                nombresAutores.length() > 0
                        ? nombresAutores.toString()
                        : "Sin autor registrado");

        model.addAttribute("generosLibro",
                nombresGeneros.length() > 0
                        ? nombresGeneros.toString()
                        : "Sin género registrado");

        
        List<BookSeries> relacionesSeries = bookSeriesRepository.findAll();

        String nombreSerie = "Libro independiente";
        String tipoSerie = "No pertenece a una serie";
        Integer numeroVolumen = null;
        Integer ordenLectura = null;

        for (BookSeries relacion : relacionesSeries) {
            if (id.equals(relacion.getBookId())) {

                LiterarySeries serie =
                        seriesService.obtenerPorId(relacion.getSeriesId());

                nombreSerie = serie.getName();
                tipoSerie = serie.getSeriesType();
                numeroVolumen = relacion.getVolumeNumber();
                ordenLectura = relacion.getReadingOrder();

                break;
            }
        }

        model.addAttribute("nombreSerie", nombreSerie);
        model.addAttribute("tipoSerie", tipoSerie);
        model.addAttribute("numeroVolumen", numeroVolumen);
        model.addAttribute("ordenLectura", ordenLectura);

        // CALIFICACIÓN PROMEDIO DEL LIBRO
        List<Object> estadisticas =
                ratingService.obtenerEstadisticasGlobales(id);

        String calificacion = "Sin calificar";
        Object totalCalificaciones = 0;

        if (!estadisticas.isEmpty()) {
            Object primerResultado = estadisticas.get(0);

            if (primerResultado instanceof Map<?, ?> datos) {
                Object promedio = datos.get("averageScore");
                Object total = datos.get("totalRatings");

                if (promedio != null) {
                    calificacion = promedio.toString() + " / 5";
                }

                if (total != null) {
                    totalCalificaciones = total;
                }
            }
        }

        model.addAttribute("calificacion", calificacion);
        model.addAttribute("totalCalificaciones", totalCalificaciones);


        // RESEÑAS PÚBLICAS DEL LIBRO
        List<Review> resenas = reviewService.obtenerPorLibro(id);
        List<Map<String, Object>> resenasVista = new ArrayList<>();

        for (Review resena : resenas) {
            Map<String, Object> dato = new LinkedHashMap<>();

            dato.put("contenido", resena.getContent());
            dato.put("usuario",
                    resena.getUser() != null
                            ? resena.getUser().getUsername()
                            : "Usuario");
            dato.put("fecha", resena.getCreatedAt());

            resenasVista.add(dato);
        }

        model.addAttribute("resenasLibro", resenasVista);

        model.addAttribute(
            "archivosLibro",
            bookFileService.listarPorLibro(id)
        );

        return "libro-ficha";
    }

    // PÁGINA 3: lista de series
    @GetMapping("/catalogo/series")
    public String mostrarSeries(Model model) {
        model.addAttribute("series", seriesService.listar());
        return "series-lista";
    }

    @GetMapping("/catalogo/series/{id}")
    public String mostrarDetalleSerie(
            @PathVariable Long id,
            Model model) {

        LiterarySeries serie = seriesService.obtenerPorId(id);

        List<BookSeries> relaciones =
                bookSeriesRepository
                        .findBySeriesIdOrderByReadingOrderAsc(id);

        List<Map<String, Object>> librosSerie = new ArrayList<>();

        for (BookSeries relacion : relaciones) {
            Book libro = bookService.obtenerPorId(relacion.getBookId());

            Map<String, Object> dato = new HashMap<>();
            dato.put("libro", libro);
            dato.put("volumen", relacion.getVolumeNumber());
            dato.put("orden", relacion.getReadingOrder());

            librosSerie.add(dato);
        }

        model.addAttribute("serie", serie);
        model.addAttribute("librosSerie", librosSerie);
        model.addAttribute("todosLibros", bookService.listar(null, null, null, null, null, "title", null, null));

        return "serie-detalle";
    }

}