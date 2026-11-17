package mx.uv.booknest.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import mx.uv.booknest.models.Review;
import mx.uv.booknest.repository.BookRepository;
import mx.uv.booknest.repository.ReviewRepository;
import mx.uv.booknest.repository.UsersRepository;

@Service
public class ReviewService {

    private final ReviewRepository repository;
    private final UsersRepository usersRepository;
    private final BookRepository bookRepository;

    public ReviewService(ReviewRepository repository, UsersRepository usersRepository, BookRepository bookRepository) {
        this.repository = repository;
        this.usersRepository = usersRepository;
        this.bookRepository = bookRepository;
    }

    // Soporta filtros opcionales ?bookId=...&userId=...
    public List<Review> listar(Long bookId, Long userId) {
        if (bookId != null && userId != null) {
            return repository.findByBookIdAndUserIdOrderByIdDesc(bookId, userId);
        }
        if (bookId != null) {
            return repository.findByBookIdOrderByIdDesc(bookId);
        }
        if (userId != null) {
            return repository.findByUserIdOrderByIdDesc(userId);
        }
        return repository.findAll();
    }

    public Review obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reseña no encontrada"));
    }

    public List<Review> obtenerPorLibro(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado");
        }
        return repository.findByBookIdOrderByIdDesc(bookId);
    }

    public Review crear(Review review) {
        if (review.getContent() == null || review.getContent().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El contenido de la reseña es obligatorio");
        }
        if (review.getUser() == null || review.getUser().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario es obligatorio");
        }
        if (review.getBook() == null || review.getBook().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El libro es obligatorio");
        }
        if (!usersRepository.existsById(review.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario especificado no existe");
        }
        if (!bookRepository.existsById(review.getBook().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El libro especificado no existe");
        }
        return repository.save(review);
    }

    // RF-31: Editar reseña propia
    public Review actualizar(Long id, Review datos) {
        Review existente = obtenerPorId(id);
        if (datos.getContent() == null || datos.getContent().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El contenido de la reseña es obligatorio");
        }
        // Validación opcional de propietario si envían user.id
        if (datos.getUser() != null && datos.getUser().getId() != null
                && !existente.getUser().getId().equals(datos.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Solo el autor de la reseña puede modificarla");
        }
        existente.setContent(datos.getContent());
        return repository.save(existente);
    }

    // RF-31: Eliminar reseña propia
    public void eliminar(Long id, Long userId) {
        Review existente = obtenerPorId(id);
        if (userId != null && !existente.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Solo el autor de la reseña puede eliminarla");
        }
        repository.delete(existente);
    }
}