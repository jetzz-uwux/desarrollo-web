package mx.uv.booknest.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import mx.uv.booknest.models.AlternativeTitle;
import mx.uv.booknest.repository.AlternativeTitleRepository;

/**
 * @author Hans Arturo
 */
@Service
public class AlternativeTitleService {

    private final AlternativeTitleRepository repository;

    public AlternativeTitleService(AlternativeTitleRepository repository) {
        this.repository = repository;
    }

    public List<AlternativeTitle> listar() {
        return repository.findAll();
    }

    public List<AlternativeTitle> obtenerPorLibro(Long bookId) {
        return repository.findByBookId(bookId);
    }

    public AlternativeTitle crear(AlternativeTitle altTitle) {
        if (altTitle.getTitle() == null || altTitle.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El título alternativo es obligatorio");
        }
        if (altTitle.getLanguage() == null || (!altTitle.getLanguage().equals("es") && !altTitle.getLanguage().equals("en"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El idioma debe ser 'es' o 'en'");
        }
        if (altTitle.getBook() == null || altTitle.getBook().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El libro asociado es obligatorio");
        }
        if (repository.findByBookIdAndTitleIgnoreCaseAndLanguage(
                altTitle.getBook().getId(), altTitle.getTitle().trim(), altTitle.getLanguage()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ese título alternativo ya está registrado para este libro e idioma");
        }
        altTitle.setTitle(altTitle.getTitle().trim());
        return repository.save(altTitle);
    }
}