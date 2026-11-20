package mx.uv.booknest.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import mx.uv.booknest.models.Review;
import mx.uv.booknest.service.ReviewService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @GetMapping
    public List<Review> listar(
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) Long userId) {
        return service.listar(bookId, userId);
    }

    @GetMapping("/{id}")
    public Review obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @GetMapping("/book/{bookId}")
    public List<Review> obtenerPorLibro(@PathVariable Long bookId) {
        return service.obtenerPorLibro(bookId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Review crear(@RequestBody Review review) {
        return service.crear(review);
    }

    @PutMapping("/{id}")
    public Review actualizar(@PathVariable Long id, @RequestBody Review review) {
        return service.actualizar(id, review);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id, @RequestParam(required = false) Long userId) {
        service.eliminar(id, userId);
    }
}