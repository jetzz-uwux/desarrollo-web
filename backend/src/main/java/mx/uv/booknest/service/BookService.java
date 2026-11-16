package mx.uv.booknest.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import mx.uv.booknest.models.Author;
import mx.uv.booknest.models.Book;
import mx.uv.booknest.models.BookAuthor;
import mx.uv.booknest.models.BookGenre;
import mx.uv.booknest.models.BookSeries;
import mx.uv.booknest.models.LiterarySeries;
import mx.uv.booknest.repository.AuthorRepository;
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
        BookSeriesRepository bookSeriesRepository) {
            this.repository = repository;
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

    public List<Book> listar(String q, Integer minPages, Integer maxPages, Long genreId, Long authorId, String sort, String seriesType){
        List<Book> libros;

        if (q == null || q.isBlank()) {
            libros = repository.findAll();
        } else {
            // Buscar libros por título
            libros = new ArrayList<>(
                repository.findByMainTitleContainingIgnoreCase(q)
            );

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

        if (minPages != null) {
            List<Book> filtrados = new ArrayList<>();
            for (Book libro : libros) {
                if (libro.getTotalPages() >= minPages) {
                    filtrados.add(libro);
                }
            }
            libros = filtrados;
        }

        if (maxPages != null) {
            List<Book> filtrados = new ArrayList<>();
            for (Book libro : libros) {
                if (libro.getTotalPages() <= maxPages) {
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

            List<LiterarySeries> series =
                literarySeriesRepository.findBySeriesType(seriesType);

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

        if (sort != null) {
        switch (sort) {
            case "title":
                libros.sort(Comparator.comparing(
                    Book::getMainTitle, String.CASE_INSENSITIVE_ORDER));
                break;

            case "pages-asc":
                libros.sort(Comparator.comparing(Book::getTotalPages));
                break;

            case "pages-desc":
                libros.sort(Comparator.comparing(
                    Book::getTotalPages).reversed());
                break;
            }
        }

        return libros;
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

    public Book crear(Book book){

        if (book.getTotalPages()== null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El número de páginas es obligatorio");
        }
        else if (book.getTotalPages() <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El número de páginas no puede ser igual o menor a 0");
        }
        else if (book.getMainTitle() == null || book.getMainTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El título es obligatorio");
        }
        
        return repository.save(book);
    }

    public Book actualizar(Long id, Book datos) {
        Book existente = obtenerPorId(id);

        if (datos.getTotalPages()== null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El número de páginas es obligatorio");
        }
        else if (datos.getTotalPages() <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El número de páginas no puede ser igual o menor a 0");
        }
        else if (datos.getMainTitle() == null || datos.getMainTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El título es obligatorio");
        }

        existente.setMainTitle(datos.getMainTitle());
        existente.setOriginalPublicationDate(datos.getOriginalPublicationDate());
        existente.setSynopsis(datos.getSynopsis());
        existente.setTotalPages(datos.getTotalPages());
        existente.setCoverUrl(datos.getCoverUrl());

        return repository.save(existente);
    }

    public void eliminar(Long id) {
        Book existente = obtenerPorId(id);
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
