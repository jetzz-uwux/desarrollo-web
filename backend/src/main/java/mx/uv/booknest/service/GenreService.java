package mx.uv.booknest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import mx.uv.booknest.models.Genre;
import mx.uv.booknest.repository.BookGenreRepository;
import mx.uv.booknest.repository.GenreRepository;

/**
 * 
 * @author Jetzaly
 * 
 */

@Service
public class GenreService {

    private final GenreRepository repository;
    private final BookGenreRepository bookGenreRepository;

    public GenreService(GenreRepository repository, BookGenreRepository bookGenreRepository) {
        this.repository = repository;
        this.bookGenreRepository = bookGenreRepository;
    }

    // Lista todas los géneros
    public List<Genre> listar() {
        return repository.findAll();
    }
    
    // Busca una id específica
    public Genre obtenerPorId(Long id) {
        Optional<Genre> resultado = repository.findById(id);
        if (resultado.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Género no encontrado");
        }
        return resultado.get();
    }

    // Crea un nuevo género 
    public Genre crear(Genre genre) {
        if (genre.getName() == null || genre.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
        }
        return repository.save(genre);
    }

    // Actualiza el nombre de un género
    public Genre actualizar(Long id, Genre datos) {
        Genre existente = obtenerPorId(id);

        if (datos.getName() == null || datos.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
        }

        existente.setName(datos.getName());

        return repository.save(existente);
    }

    // Elimina un género en específico
    public void eliminar(Long id) {
        Genre existente = obtenerPorId(id);
        if (bookGenreRepository.existsByGenreId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede eliminar el género porque está asociado a uno o más libros");
        }
        repository.delete(existente);
    }

}
