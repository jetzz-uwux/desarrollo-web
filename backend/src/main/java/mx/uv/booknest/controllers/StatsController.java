package mx.uv.booknest.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import mx.uv.booknest.models.Rating;
import mx.uv.booknest.repository.RatingRepository;
import mx.uv.booknest.service.RatingService;

/**
 * @author Camila
 */
@RestController
@RequestMapping("/api/stats")
public class StatsController {
    private final RatingService service;

    public StatsController(RatingService service) {
        this.service = service;
    }

    @PostMapping("/rating")
    @ResponseStatus(HttpStatus.CREATED)
    public Rating crearRating(@RequestBody Rating rating) {
        return service.crear(rating);
    }

    @GetMapping("/global")
    public List<RatingRepository.GlobalStat> obtenerGlobales() {
        return service.obtenerEstadisticasGlobales();
    }
}