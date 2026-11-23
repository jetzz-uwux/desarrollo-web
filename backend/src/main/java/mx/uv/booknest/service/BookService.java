package mx.uv.booknest.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import mx.uv.booknest.models.Author;
import mx.uv.booknest.models.AlternativeTitle;
import mx.uv.booknest.models.BookFile;
import mx.uv.booknest.models.Book;
import mx.uv.booknest.models.BookAuthor;
import mx.uv.booknest.models.BookGenre;
import mx.uv.booknest.models.BookSeries;
import mx.uv.booknest.models.LiterarySeries;
import mx.uv.booknest.repository.AuthorRepository;
import mx.uv.booknest.repository.AlternativeTitleRepository;
import mx.uv.booknest.repository.BookAuthorRepository;
import mx.uv.booknest.repository.BookFileRepository;
import mx.uv.booknest.repository.BookGenreRepository;
import mx.uv.booknest.repository.BookRepository;
import mx.uv.booknest.repository.BookSeriesRepository;
import mx.uv.booknest.repository.CollectionBooksRepository;
import mx.uv.booknest.repository.GenreRepository;
import mx.uv.booknest.repository.LiterarySeriesRepository;
import mx.uv.booknest.repository.RatingRepository;
import mx.uv.booknest.repository.ReviewRepository;
import mx.uv.booknest.repository.UserReadingProgressRepository;

/**
 * 
 * @author Jetzaly
 * 
 */

@Service
public class BookService {
    private final BookRepository repository;
    private final AlternativeTitleRepository alternativeTitleRepository;
    private final BookFileRepository bookFileRepository;
    private final CollectionBooksRepository collectionBookRepository;
    private final UserReadingProgressRepository userReadingProgressRepository;
    private final ReviewRepository reviewRepository;
    private final RatingRepository ratingRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final BookGenreRepository bookGenreRepository;
    private final LiterarySeriesRepository literarySeriesRepository;
    private final BookSeriesRepository bookSeriesRepository;

    public BookService(BookRepository repository, BookFileRepository bookFileRepository,
        CollectionBooksRepository collectionBookRepository, UserReadingProgressRepository userReadingProgressRepository,
        ReviewRepository reviewRepository, RatingRepository ratingRepository,
        AuthorRepository authorRepository, GenreRepository genreRepository,
        BookAuthorRepository bookAuthorRepository, BookGenreRepository bookGenreRepository,
        LiterarySeriesRepository literarySeriesRepository,
        BookSeriesRepository bookSeriesRepository, AlternativeTitleRepository alternativeTitleRepository) {
            this.repository = repository;
        this.alternativeTitleRepository = alternativeTitleRepository;
            this.bookFileRepository = bookFileRepository;
            this.collectionBookRepository = collectionBookRepository;
            this.userReadingProgressRepository = userReadingProgressRepository;
            this.reviewRepository = reviewRepository;
            this.ratingRepository = ratingRepository;
            this.authorRepository = authorRepository;
            this.genreRepository = genreRepository;
            this.bookAuthorRepository = bookAuthorRepository;
            this.bookGenreRepository = bookGenreRepository;
            this.literarySeriesRepository = literarySeriesRepository;
            this.bookSeriesRepository = bookSeriesRepository;
    }

    public List<Book> listar(String q, Integer minPages, Integer maxPages, Long genreId, Long authorId, String sort, String seriesType, String language, String category){
        List<Book> libros;

        if (q == null || q.isBlank()) {
            libros = repository.findAll();
        } else {
            // Buscar libros por título
            libros = new ArrayList<>(
                repository.findByMainTitleContainingIgnoreCase(q)
            );

            // También buscar por títulos alternativos registrados en español o inglés.
            for (AlternativeTitle alternativo : alternativeTitleRepository.findByTitleContainingIgnoreCase(q)) {
                Book libro = alternativo.getBook();
                if (libro != null && libros.stream().noneMatch(b -> b.getId().equals(libro.getId()))) {
                    libros.add(libro);
                }
            }

            // Buscar autores cuyo nombre contenga el texto
            List<Author> autores =
                authorRepository.findByNameContainingIgnoreCase(q);

            // Obtener los IDs de los autores encontrados
            List<Long> authorIds = new ArrayList<>();

            for (Author autor : autores) {
                authorIds.add(autor.getId());
            }

            // Buscar los libros asociados a esos autores
            if (!authorIds.isEmpty()) {
                List<BookAuthor> relaciones =
                    bookAuthorRepository.findByAuthorIdIn(authorIds);

                for (BookAuthor relacion : relaciones) {
                    Book libro = repository.findById(relacion.getBookId())
                        .orElse(null);

                    if (libro != null && !libros.contains(libro)) {
                        libros.add(libro);
                    }
                }
            }
        }

        if (category != null && !category.isBlank()) {
            String filtroCategoria = category.trim();
            libros.removeIf(libro -> libro.getCategory() == null || !libro.getCategory().equalsIgnoreCase(filtroCategoria));
        }

        if (minPages != null && minPages < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El mínimo de páginas debe ser positivo");
        }
        if (maxPages != null && maxPages < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El máximo de páginas debe ser positivo");
        }
        if (minPages != null && maxPages != null && minPages > maxPages) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El mínimo de páginas supera el máximo");
        }

        if (minPages != null) {
            List<Book> filtrados = new ArrayList<>();
            for (Book libro : libros) {
                if (libro.getTotalPages() != null && libro.getTotalPages() >= minPages) {
                    filtrados.add(libro);
                }
            }
            libros = filtrados;
        }

        if (maxPages != null) {
            List<Book> filtrados = new ArrayList<>();
            for (Book libro : libros) {
                if (libro.getTotalPages() != null && libro.getTotalPages() <= maxPages) {
                    filtrados.add(libro);
                }
            }
            libros = filtrados;
        }

        if (genreId != null) {
            List<BookGenre> relaciones = bookGenreRepository.findByGenreId(genreId);
            List<Book> filtrados = new ArrayList<>();
            for (Book libro : libros) {
                for (BookGenre relacion : relaciones) {
                    if (libro.getId().equals(relacion.getBookId())) {
                        filtrados.add(libro);
                        break;
                    }
                }
            }
            libros = filtrados;
        }

        if (authorId != null) {
            List<BookAuthor> relaciones = bookAuthorRepository.findByAuthorId(authorId);
            List<Book> filtrados = new ArrayList<>();
            for (Book libro : libros) {
                for (BookAuthor relacion : relaciones) {
                    if (libro.getId().equals(relacion.getBookId())) {
                        filtrados.add(libro);
                        break;
                    }
                }
            }
            libros = filtrados;
        }


        if (seriesType != null && !seriesType.isBlank()) {
            String tipo = seriesType.trim().toLowerCase(Locale.ROOT);
            if (tipo.equals("bilogía")) tipo = "bilogia";
            if (tipo.equals("trilogía")) tipo = "trilogia";
            if (!Set.of("bilogia", "trilogia", "saga").contains(tipo)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de serie no válido");
            }
            List<LiterarySeries> series =
                literarySeriesRepository.findBySeriesType(tipo);

            List<Long> seriesIds = new ArrayList<>();

            for (LiterarySeries serie : series) {
                seriesIds.add(serie.getId());
            }

            List<Book> filtrados = new ArrayList<>();

            if (!seriesIds.isEmpty()) {
                List<BookSeries> relaciones =
                    bookSeriesRepository.findBySeriesIdIn(seriesIds);

                for (Book libro : libros) {
                    for (BookSeries relacion : relaciones) {
                        if (libro.getId().equals(relacion.getBookId())) {
                            filtrados.add(libro);
                            break;
                        }
                    }
                }
            }

            libros = filtrados;
        }

        // El idioma corresponde a los PDF disponibles, no al idioma de la ficha.
        if (language != null && !language.isBlank()) {
            if (!language.equals("es") && !language.equals("en")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Idioma no válido (es/en)");
            }
            Set<Long> ids = new HashSet<>();
            for (BookFile archivo : bookFileRepository.findByLanguage(language)) {
                if (archivo.getBook() != null) ids.add(archivo.getBook().getId());
            }
            libros.removeIf(libro -> !ids.contains(libro.getId()));
        }

        if (sort != null) {
        switch (sort) {
            case "best-rated":
                Map<Long, Double> promedios = new HashMap<>();
                ratingRepository.findBookRatingStats(null).forEach(r -> {
                    if (r.getAverageScore() != null) promedios.put(r.getBookId(), r.getAverageScore());
                });
                libros.sort(Comparator.comparingDouble((Book b) -> promedios.getOrDefault(b.getId(), -1.0)).reversed()
                    .thenComparing(Book::getMainTitle, String.CASE_INSENSITIVE_ORDER));
                break;
            case "most-read":
                Map<Long, Long> lectores = new HashMap<>();
                ratingRepository.findMostReadBooks().forEach(r -> lectores.put(r.getBookId(), r.getFinishedReadersCount()));
                libros.sort(Comparator.comparingLong((Book b) -> lectores.getOrDefault(b.getId(), 0L)).reversed()
                    .thenComparing(Book::getMainTitle, String.CASE_INSENSITIVE_ORDER));
                break;
            case "title":
                libros.sort(Comparator.comparing(
                    Book::getMainTitle, String.CASE_INSENSITIVE_ORDER));
                break;

            case "pages-asc":
                libros.sort(Comparator.comparing(Book::getTotalPages, Comparator.nullsLast(Integer::compareTo)));
                break;

            case "pages-desc":
                libros.sort(Comparator.comparing(Book::getTotalPages, Comparator.nullsLast(Comparator.reverseOrder())));
                break;
            }
        }

        return libros;
    }

    /** Coincidencias sugeridas: no fusiona ediciones ni obras automáticamente. */
    public List<Book> posiblesDuplicados(String titulo, Long authorId, String isbn) {
        if ((titulo == null || titulo.isBlank()) && authorId == null && (isbn == null || isbn.isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Indica título, autor o ISBN");
        }
        Set<Long> ids = new HashSet<>();
        String consulta = titulo == null ? "" : titulo.trim();
        if (!consulta.isEmpty()) {
            repository.findByMainTitleContainingIgnoreCase(consulta).forEach(b -> ids.add(b.getId()));
            alternativeTitleRepository.findByTitleContainingIgnoreCase(consulta).forEach(a -> ids.add(a.getBook().getId()));
        }
        if (authorId != null) {
            // Mostrar obras del mismo autor aunque el título no coincida.
            bookAuthorRepository.findByAuthorId(authorId).forEach(a -> ids.add(a.getBookId()));
        }
        if (isbn != null && !isbn.isBlank()) {
            bookFileRepository.findByEditionIsbn(isbn.trim()).ifPresent(f -> ids.add(f.getBook().getId()));
        }
        List<Book> coincidencias = new ArrayList<>();
        for (Long id : ids) repository.findById(id).ifPresent(coincidencias::add);
        coincidencias.sort(Comparator.comparing(Book::getMainTitle, String.CASE_INSENSITIVE_ORDER));
        return coincidencias;
    }

    public Book obtenerPorId(Long id){
        Optional<Book> resultado = repository.findById(id);    
        if (resultado.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado");
        }
        return resultado.get();
    }

    public List<Book> obtenerLibrosDeSerie(Long seriesId) {

        List<BookSeries> relaciones =
            bookSeriesRepository.findBySeriesIdOrderByReadingOrderAsc(seriesId);

        List<Book> libros = new ArrayList<>();

        for (BookSeries relacion : relaciones) {
            Book libro = repository.findById(relacion.getBookId())
                .orElse(null);

            if (libro != null) {
                libros.add(libro);
            }
        }

        return libros;
    }

    @Transactional
    public Book crear(Book book, boolean confirmarCoincidencia){

        if (book == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los datos del libro son obligatorios");
        }
        if (book.getTotalPages()== null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El número de páginas es obligatorio");
        }
        else if (book.getTotalPages() <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El número de páginas no puede ser igual o menor a 0");
        }
        else if (book.getMainTitle() == null || book.getMainTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El título es obligatorio");
        }
        
        book.setMainTitle(book.getMainTitle().trim());
        if (book.getMainTitle().length() > 500) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El título supera los 500 caracteres");
        }
        if (book.getOriginalPublicationDate() != null
                && book.getOriginalPublicationDate().isAfter(java.time.LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de publicación no puede ser futura");
        }
        // La verificación se repite en el servidor; el usuario debe confirmar
        // expresamente si se trata de una edición/obra diferente.
        if (!confirmarCoincidencia && !posiblesDuplicados(book.getMainTitle(), null, null).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya hay fichas con ese título o uno parecido. Revisa las coincidencias y confirma si es una obra distinta");
        }
        book.setCategory(normalizarCategoria(book.getCategory()));
        return repository.save(book);
    }

    public Book actualizar(Long id, Book datos) {
        Book existente = obtenerPorId(id);

        if (datos == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los datos del libro son obligatorios");
        }
        if (datos.getTotalPages()== null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El número de páginas es obligatorio");
        }
        else if (datos.getTotalPages() <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El número de páginas no puede ser igual o menor a 0");
        }
        else if (datos.getMainTitle() == null || datos.getMainTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El título es obligatorio");
        }

        if (datos.getMainTitle().trim().length() > 500) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El título supera los 500 caracteres");
        }
        if (datos.getOriginalPublicationDate() != null
                && datos.getOriginalPublicationDate().isAfter(java.time.LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de publicación no puede ser futura");
        }
        existente.setCategory(normalizarCategoria(datos.getCategory()));
        existente.setMainTitle(datos.getMainTitle().trim());
        existente.setOriginalPublicationDate(datos.getOriginalPublicationDate());
        existente.setSynopsis(datos.getSynopsis());
        existente.setTotalPages(datos.getTotalPages());
        existente.setCoverUrl(datos.getCoverUrl());

        return repository.save(existente);
    }

    @Transactional
    private String normalizarCategoria(String categoria) {
        if (categoria == null || categoria.isBlank()) return null;
        String limpia = categoria.trim();
        if (limpia.length() > 80) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La categoría no puede superar 80 caracteres");
        if (!Set.of("Ficción", "No ficción", "Académico", "Infantil", "Otro").contains(limpia)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoría no válida");
        }
        return limpia;
    }

    public void eliminar(Long id) {
        Book existente = obtenerPorId(id);
        if (!bookSeriesRepository.findByBookId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El libro está asociado a una serie");
        }
        if (!alternativeTitleRepository.findByBookId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El libro tiene títulos alternativos asociados");
        }
        if(bookFileRepository.existsByBookId(id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede eliminar el libro porque está asociado a un archivo");
        }

        if (collectionBookRepository.existsByBookId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede eliminar el libro porque está guardado en una o más colecciones");
        }

        if (userReadingProgressRepository.existsByBookId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede eliminar el libro porque existen procesos de lectura");
        }

        if (reviewRepository.existsByBookId(id)||ratingRepository.existsByBookId(id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede eliminar el libro porque cuentan con reseñas");
        }

        // Las relaciones propias del catálogo se retiran con el libro.
        // Nunca se borran datos pertenecientes a módulos ajenos.
        bookAuthorRepository.deleteAll(bookAuthorRepository.findByBookId(id));
        bookGenreRepository.deleteAll(bookGenreRepository.findByBookId(id));
        repository.delete(existente);
    }

    public BookAuthor ligarAutor(Long bookId, Long authorId) {
        obtenerPorId(bookId);

            if (authorId == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El authorId es obligatorio");
            }
            if (!authorRepository.existsById(authorId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor no encontrado");
            }
            if (bookAuthorRepository.existsByBookIdAndAuthorId(bookId, authorId)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El libro ya tiene asignado ese autor");
            }

            BookAuthor relacion = new BookAuthor();
            relacion.setBookId(bookId);
            relacion.setAuthorId(authorId);
            return bookAuthorRepository.save(relacion);
    }

    public BookGenre ligarGenero(Long bookId, Long genreId) {
        obtenerPorId(bookId);

        if (genreId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El genreId es obligatorio");
        }
        if (!genreRepository.existsById(genreId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Género no encontrado");
        }
        if (bookGenreRepository.existsByBookIdAndGenreId(bookId, genreId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El libro ya tiene asignado ese género");
        }

        BookGenre relacion = new BookGenre();
        relacion.setBookId(bookId);
        relacion.setGenreId(genreId);
        return bookGenreRepository.save(relacion);
}
}
