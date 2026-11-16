package mx.uv.booknest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import mx.uv.booknest.models.Author;
import mx.uv.booknest.repository.AuthorRepository;
import mx.uv.booknest.repository.BookAuthorRepository;

/**
 * 
 * @author Jetzaly
 * 
 */

@Service
public class AuthorService {

    private final AuthorRepository repository;
    private final BookAuthorRepository bookAuthorRepository;

    public AuthorService(AuthorRepository repository, BookAuthorRepository bookAuthorRepository) {
        this.repository = repository;
        this.bookAuthorRepository = bookAuthorRepository;
    }

    public List<Author> listar() {
        return repository.findAll();
    }
    
    public Author obtenerPorId(Long id) {
        Optional<Author> resultado = repository.findById(id);
        if (resultado.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor no encontrado");
        }
        return resultado.get();
    }

    public Author crear(Author author) {
        if (author.getName() == null || author.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
        }
        return repository.save(author);
    }

    /* Actualiza un autor en específico */
    public Author actualizar(Long id, Author datos){
        Author existente = obtenerPorId(id);

        if (datos.getName() == null || datos.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
        }

        existente.setName(datos.getName());

        return repository.save(existente);
    }

    /* Elimina un autor en específico */
    public void eliminar(Long id) {
        Author existente = obtenerPorId(id);
        if (bookAuthorRepository.existsByAuthorId(id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede eliminar el autor porque está asociado a uno o más libros");
        }
        repository.delete(existente);
    }

}
