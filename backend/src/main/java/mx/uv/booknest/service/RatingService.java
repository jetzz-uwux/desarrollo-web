package mx.uv.booknest.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import mx.uv.booknest.models.Rating;
import mx.uv.booknest.repository.RatingRepository;

/**
 * @author Camila
 */
@Service
public class RatingService {
    private final RatingRepository repository;

    public RatingService(RatingRepository repository) {
        this.repository = repository;
    }

    public Rating crear(Rating rating) {
        if (rating.getScore() == null || rating.getScore() < 1 || rating.getScore() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La calificación debe estar entre 1 y 5 estrellas");
        }
        return repository.save(rating);
    }

    public List<RatingRepository.GlobalStat> obtenerEstadisticasGlobales() {
        return repository.findGlobalStats();
    }
}