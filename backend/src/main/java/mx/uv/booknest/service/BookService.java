package mx.uv.booknest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import mx.uv.booknest.models.Book;
import mx.uv.booknest.repository.BookFileRepository;
import mx.uv.booknest.repository.BookRepository;
import mx.uv.booknest.repository.CollectionBooksRepository;
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

    public BookService(BookRepository repository, BookFileRepository bookFileRepository, 
        CollectionBooksRepository collectionBookRepository, UserReadingProgressRepository userReadingProgressRepository, 
        ReviewRepository reviewRepository, RatingRepository ratingRepository){
        this.repository=repository;
        this.bookFileRepository = bookFileRepository;
        this.collectionBookRepository = collectionBookRepository;
        this.userReadingProgressRepository= userReadingProgressRepository;
        this.reviewRepository = reviewRepository;
        this.ratingRepository = ratingRepository;
    }

    public List<Book> listar(){
        return repository.findAll();
    }

    public Book obtenerPorId(Long id){
        Optional<Book> resultado = repository.findById(id);    
        if (resultado.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado");
        }
        return resultado.get();
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
}
