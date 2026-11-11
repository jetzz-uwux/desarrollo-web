package mx.uv.booknest.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import mx.uv.booknest.models.Genre;
import mx.uv.booknest.service.GenreService;

/**
 * 
 * @author Jetzaly
 * 
 */

@RestController 
@RequestMapping("/api/genres")
public class GenreController {
    private final GenreService service;

    public GenreController(GenreService service) {
        this.service = service;
    }

    @GetMapping
    public List<Genre> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Genre obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Genre crear(@RequestBody Genre genre) {
        return service.crear(genre);
    }
}
