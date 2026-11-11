package mx.uv.booknest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import mx.uv.booknest.models.Book;
import mx.uv.booknest.repository.BookRepository;

/**
 * 
 * @author Jetzaly
 * 
 */

@Service
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository){
        this.repository=repository;
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
}
