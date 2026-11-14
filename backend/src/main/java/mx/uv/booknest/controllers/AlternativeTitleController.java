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

import mx.uv.booknest.models.AlternativeTitle;
import mx.uv.booknest.service.AlternativeTitleService;

/**
 * @author Hans Arturo
 */
@RestController
@RequestMapping("/api/alternative-titles")
public class AlternativeTitleController {

    private final AlternativeTitleService service;

    public AlternativeTitleController(AlternativeTitleService service) {
        this.service = service;
    }

    @GetMapping
    public List<AlternativeTitle> listar() {
        return service.listar();
    }

    @GetMapping("/book/{bookId}")
    public List<AlternativeTitle> obtenerPorLibro(@PathVariable Long bookId) {
        return service.obtenerPorLibro(bookId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlternativeTitle crear(@RequestBody AlternativeTitle altTitle) {
        return service.crear(altTitle);
    }
}