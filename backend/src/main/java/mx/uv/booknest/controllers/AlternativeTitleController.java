package mx.uv.booknest.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.CrossOrigin;

import mx.uv.booknest.models.AlternativeTitle;
import mx.uv.booknest.service.AlternativeTitleService;

/**
 * @author Hans Arturo
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/alternative-titles")
public class AlternativeTitleController {

    private final AlternativeTitleService service;

    public AlternativeTitleController(AlternativeTitleService service) {
        this.service = service;
    }

    @GetMapping
    public List<AlternativeTitle> listar(
            @RequestParam(value = "bookId", required = false) Long bookId,
            @RequestParam(value = "language", required = false) String language,
            @RequestParam(value = "q", required = false) String q) {
        return service.listarConFiltros(bookId, language, q);
    }

    @GetMapping("/{id}")
    public AlternativeTitle obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
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

    @PutMapping("/{id}")
    public AlternativeTitle actualizar(@PathVariable Long id, @RequestBody AlternativeTitle altTitle) {
        return service.actualizar(id, altTitle);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}