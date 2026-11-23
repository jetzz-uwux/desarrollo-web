package mx.uv.booknest.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import mx.uv.booknest.models.BookSeries;
import mx.uv.booknest.service.BookSeriesService;

/** Endpoints de asociación de libros a series. */
@RestController
@RequestMapping("/api/series/{seriesId}/books")
public class BookSeriesController {
    private final BookSeriesService service;

    public BookSeriesController(BookSeriesService service) {
        this.service = service;
    }

    @GetMapping
    public List<BookSeries> listar(@PathVariable Long seriesId) {
        return service.listar(seriesId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookSeries asociar(@PathVariable Long seriesId, @RequestBody BookSeries datos) {
        return service.asociar(seriesId, datos);
    }

    @DeleteMapping("/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void quitar(@PathVariable Long seriesId, @PathVariable Long bookId) {
        service.quitar(seriesId, bookId);
    }
}
