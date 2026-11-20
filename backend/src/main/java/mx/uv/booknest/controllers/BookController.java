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

import mx.uv.booknest.models.Book;
import mx.uv.booknest.models.BookAuthor;
import mx.uv.booknest.models.BookGenre;
import mx.uv.booknest.service.BookService;

/**
 * 
 * @author Jetzaly
 * 
 */

@CrossOrigin(origins = "*")
@RestController 
@RequestMapping("/api/books")
public class BookController {
    private final BookService service;

    public BookController (BookService service){
        this.service=service;
    }

    @GetMapping ("/{id}")
    public Book obtener(@PathVariable Long id){
        return service.obtenerPorId(id);
    }

    @GetMapping("/series/{seriesId}")
    public List<Book> obtenerLibrosDeSerie(@PathVariable Long seriesId) {
        return service.obtenerLibrosDeSerie(seriesId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book crear(@RequestBody Book book){
        return service.crear(book);
    }

    @PutMapping ("/{id}")
    public Book actualizar(@PathVariable Long id, @RequestBody Book book){
        return service.actualizar(id, book);
    }

    @DeleteMapping ("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id){
        service.eliminar(id);
    }

    @GetMapping
    public List<Book> listar(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer minPages,
            @RequestParam(required = false) Integer maxPages,
            @RequestParam(required = false) Long genreId,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String seriesType) {
        return service.listar(q, minPages, maxPages, genreId, authorId, sort, seriesType);
    }

    @PostMapping("/{id}/authors")
    @ResponseStatus(HttpStatus.CREATED)
    public BookAuthor ligarAutor(@PathVariable Long id, @RequestBody BookAuthor datos) {
        return service.ligarAutor(id, datos.getAuthorId());
    }

    @PostMapping("/{id}/genres")
    @ResponseStatus(HttpStatus.CREATED)
    public BookGenre ligarGenero(@PathVariable Long id, @RequestBody BookGenre datos) {
        return service.ligarGenero(id, datos.getGenreId());
    }

}