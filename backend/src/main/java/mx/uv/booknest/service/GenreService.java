package mx.uv.booknest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import mx.uv.booknest.models.Genre;
import mx.uv.booknest.repository.GenreRepository;

/**
 * 
 * @author Jetzaly
 * 
 */

@Service
public class GenreService {

    private final GenreRepository repository;

    public GenreService(GenreRepository repository) {
        this.repository = repository;
    }

    public List<Genre> listar() {
        return repository.findAll();
    }
    
    public Genre obtenerPorId(Long id) {
        Optional<Genre> resultado = repository.findById(id);
        if (resultado.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Género no encontrado");
        }
        return resultado.get();
    }

    public Genre crear(Genre genre) {
        if (genre.getName() == null || genre.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
        }
        return repository.save(genre);
    }
}
