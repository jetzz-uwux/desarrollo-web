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

import mx.uv.booknest.models.Author;
import mx.uv.booknest.service.AuthorService;

/**
 * 
 * @author Jetzaly
 * 
 */

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    private final AuthorService service;

    public AuthorController(AuthorService service) {
        this.service = service;
    }

        @GetMapping
    public List<Author> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Author obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Author crear(@RequestBody Author author) {
        return service.crear(author);
    }

}
