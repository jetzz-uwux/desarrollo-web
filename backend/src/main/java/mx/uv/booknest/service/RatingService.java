package mx.uv.booknest.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import mx.uv.booknest.models.Rating;
import mx.uv.booknest.repository.BookRepository;
import mx.uv.booknest.repository.RatingRepository;
import mx.uv.booknest.repository.UsersRepository;

@Service
public class RatingService {

    private final RatingRepository repository;
    private final UsersRepository usersRepository;
    private final BookRepository bookRepository;

    public RatingService(RatingRepository repository, UsersRepository usersRepository, BookRepository bookRepository) {
        this.repository = repository;
        this.usersRepository = usersRepository;
        this.bookRepository = bookRepository;
    }

    public Rating obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Calificación no encontrada"));
    }

    public Rating crear(Rating rating) {
        validarDatos(rating);
        Long userId = rating.getUser().getId();
        Long bookId = rating.getBook().getId();

        if (repository.findByUserIdAndBookId(userId, bookId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El usuario ya calificó este libro. Use PUT para modificar su calificación");
        }
        return repository.save(rating);
    }

    public Rating actualizar(Long id, Rating datos) {
        Rating existente = obtenerPorId(id);
        if (datos.getScore() == null || datos.getScore() < 1 || datos.getScore() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La calificación debe estar entre 1 y 5 estrellas");
        }
        existente.setScore(datos.getScore());
        return repository.save(existente);
    }

    public void eliminar(Long id) {
        Rating existente = obtenerPorId(id);
        repository.delete(existente);
    }

    public List<Object> obtenerEstadisticasGlobales(Long bookId) {
        if (bookId != null && !bookRepository.existsById(bookId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado");
        }
        List<Object> resultado = new ArrayList<>();
        for (RatingRepository.GlobalStat stat : repository.findBookRatingStats(bookId)) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("bookId", stat.getBookId());
            item.put("mainTitle", stat.getMainTitle());
            item.put("totalRatings", stat.getTotalRatings());
            if (stat.getTotalRatings() == null || stat.getTotalRatings() == 0 || stat.getAverageScore() == null) {
                item.put("averageScore", null);
                item.put("displayRating", "Sin calificar");
            } else {
                item.put("averageScore", stat.getAverageScore());
                item.put("displayRating", stat.getAverageScore() + " estrellas");
            }
            resultado.add(item);
        }
        return resultado;
    }

    public List<RatingRepository.PopularBookStat> obtenerMasLeidos() {
        return repository.findMostReadBooks();
    }

    public List<RatingRepository.UserPersonalStat> obtenerEstadisticasPersonales(Long userId) {
        if (!usersRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        return repository.findUserProgressStats(userId);
    }

    private void validarDatos(Rating rating) {
        if (rating.getScore() == null || rating.getScore() < 1 || rating.getScore() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La calificación debe estar entre 1 y 5 estrellas");
        }
        if (rating.getUser() == null || rating.getUser().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario es obligatorio");
        }
        if (rating.getBook() == null || rating.getBook().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El libro es obligatorio");
        }
        if (!usersRepository.existsById(rating.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario especificado no existe");
        }
        if (!bookRepository.existsById(rating.getBook().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El libro especificado no existe");
        }
    }
}