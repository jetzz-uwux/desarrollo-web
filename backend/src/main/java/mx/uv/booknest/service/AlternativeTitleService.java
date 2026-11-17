package mx.uv.booknest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import mx.uv.booknest.models.AlternativeTitle;
import mx.uv.booknest.models.Book;
import mx.uv.booknest.models.Users;
import mx.uv.booknest.repository.AlternativeTitleRepository;
import mx.uv.booknest.repository.BookRepository;
import mx.uv.booknest.repository.UsersRepository;

/**
 * @author Hans Arturo
 */
@Service
public class AlternativeTitleService {

    private final AlternativeTitleRepository repository;
    private final BookRepository bookRepository;
    private final UsersRepository usersRepository;

    public AlternativeTitleService(
            AlternativeTitleRepository repository,
            BookRepository bookRepository,
            UsersRepository usersRepository) {
        this.repository = repository;
        this.bookRepository = bookRepository;
        this.usersRepository = usersRepository;
    }

    public List<AlternativeTitle> listarConFiltros(Long bookId, String language, String q) {
        if (language != null && !language.isBlank()) {
            validarIdioma(language);
        }

        if (bookId != null && language != null && !language.isBlank()) {
            if (!bookRepository.existsById(bookId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El libro especificado no existe");
            }
            return repository.findByBookIdAndLanguage(bookId, language);
        }

        if (bookId != null) {
            if (!bookRepository.existsById(bookId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El libro especificado no existe");
            }
            return repository.findByBookId(bookId);
        }

        if (language != null && !language.isBlank()) {
            return repository.findByLanguage(language);
        }

        if (q != null && !q.isBlank()) {
            return repository.findByTitleContainingIgnoreCase(q.trim());
        }

        return repository.findAll();
    }

    public AlternativeTitle obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Título alternativo no encontrado"));
    }

    public List<AlternativeTitle> obtenerPorLibro(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El libro especificado no existe");
        }
        return repository.findByBookId(bookId);
    }

    public AlternativeTitle crear(AlternativeTitle altTitle) {
        if (altTitle == null || altTitle.getTitle() == null || altTitle.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El título alternativo es obligatorio");
        }
        validarIdioma(altTitle.getLanguage());

        if (altTitle.getBook() == null || altTitle.getBook().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El libro asociado es obligatorio");
        }

        Book book = bookRepository.findById(altTitle.getBook().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El libro especificado no existe"));

        Users contributor = null;
        if (altTitle.getContributedByUser() != null && altTitle.getContributedByUser().getId() != null) {
            contributor = usersRepository.findById(altTitle.getContributedByUser().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario especificado no existe"));
        }

        String tituloLimpio = altTitle.getTitle().trim();
        if (repository.findByBookIdAndTitleIgnoreCaseAndLanguage(
                book.getId(), tituloLimpio, altTitle.getLanguage()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ese título alternativo ya está registrado para este libro e idioma");
        }

        altTitle.setBook(book);
        altTitle.setContributedByUser(contributor);
        altTitle.setTitle(tituloLimpio);
        return repository.save(altTitle);
    }

    public AlternativeTitle actualizar(Long id, AlternativeTitle datos) {
        AlternativeTitle existente = obtenerPorId(id);

        if (datos == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los datos de actualización son obligatorios");
        }

        String nuevoTitulo = datos.getTitle() != null ? datos.getTitle().trim() : existente.getTitle();
        if (nuevoTitulo.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El título alternativo no puede estar vacío");
        }

        String nuevoIdioma = datos.getLanguage() != null ? datos.getLanguage() : existente.getLanguage();
        validarIdioma(nuevoIdioma);

        Optional<AlternativeTitle> duplicado = repository.findByBookIdAndTitleIgnoreCaseAndLanguage(
                existente.getBook().getId(), nuevoTitulo, nuevoIdioma);
        if (duplicado.isPresent() && !duplicado.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ese título alternativo ya está registrado para este libro e idioma");
        }

        existente.setTitle(nuevoTitulo);
        existente.setLanguage(nuevoIdioma);
        return repository.save(existente);
    }

    public void eliminar(Long id) {
        AlternativeTitle existente = obtenerPorId(id);
        repository.delete(existente);
    }

    private void validarIdioma(String language) {
        if (language == null || (!language.equals("es") && !language.equals("en"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El idioma debe ser 'es' o 'en'");
        }
    }
}