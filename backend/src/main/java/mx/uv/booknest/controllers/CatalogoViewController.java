package mx.uv.booknest.controllers;

import mx.uv.booknest.service.BookService;
import mx.uv.booknest.service.LiterarySeriesService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CatalogoViewController {

    private final BookService bookService;
    private final LiterarySeriesService seriesService;

    public CatalogoViewController(BookService bookService, LiterarySeriesService seriesService) {
        this.bookService = bookService;
        this.seriesService = seriesService;
    }

    // PÁGINA 1: catálogo con buscador
    @GetMapping("/catalogo")
    public String mostrarCatalogo(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer minPages,
            @RequestParam(required = false) Integer maxPages,
            Model model) {

        model.addAttribute("libros",
                bookService.listar(q, minPages, maxPages, null, null, null, null));
        model.addAttribute("q", q);
        model.addAttribute("minPages", minPages);
        model.addAttribute("maxPages", maxPages);

        return "catalogo";
    }

    // PÁGINA 2: ficha de un libro
    @GetMapping("/catalogo/libros/{id}")
    public String mostrarLibro(@PathVariable Long id, Model model) {
        model.addAttribute("libro", bookService.obtenerPorId(id));
        return "libro-ficha";
    }

    // PÁGINA 3: lista de series
    @GetMapping("/catalogo/series")
    public String mostrarSeries(Model model) {
        model.addAttribute("series", seriesService.listar());
        return "series-lista";
    }
}