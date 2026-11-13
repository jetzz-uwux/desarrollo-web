package mx.uv.booknest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import mx.uv.booknest.models.Review;
import mx.uv.booknest.repository.ReviewRepository;

/**
 * @author Camila
 */
@Service
public class ReviewService {
    private final ReviewRepository repository;

    public ReviewService(ReviewRepository repository) {
        this.repository = repository;
    }

    public List<Review> listar() {
        return repository.findAll();
    }

    public Review obtenerPorId(Long id) {
        Optional<Review> resultado = repository.findById(id);
        if (resultado.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reseña no encontrada");
        }
        return resultado.get();
    }

    public List<Review> obtenerPorLibro(Long bookId) {
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
        return repository.save(review);
    }
}