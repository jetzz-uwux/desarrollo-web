package mx.uv.booknest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import mx.uv.booknest.models.Author;
import mx.uv.booknest.repository.AuthorRepository;

/**
 * 
 * @author Jetzaly
 * 
 */

@Service
public class AuthorService {

    private final AuthorRepository repository;

    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
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

}
