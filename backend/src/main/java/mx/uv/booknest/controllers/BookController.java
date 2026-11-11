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

import mx.uv.booknest.models.Book;
import mx.uv.booknest.service.BookService;

/**
 * 
 * @author Jetzaly
 * 
 */

@RestController 
@RequestMapping("/api/books")
public class BookController {
    private final BookService service;

    public BookController (BookService service){
        this.service=service;
    }
    
    @GetMapping
    public List<Book> listar(){
        return service.listar();
    }

    @GetMapping ("/{id}")
    public Book obtener(@PathVariable Long id){
        return service.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book crear(@RequestBody Book book){
        return service.crear(book);
    }

}
