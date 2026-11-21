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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import mx.uv.booknest.models.LiterarySeries;
import mx.uv.booknest.service.LiterarySeriesService;

/**
 * 
 * @author Jetzaly
 * 
 */

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/series")
public class LiterarySeriesController {
    private final LiterarySeriesService service;

    public LiterarySeriesController(LiterarySeriesService service){
        this.service=service;
    }

    @GetMapping
    public List<LiterarySeries> listar(){
        return service.listar();
    }

    @GetMapping ("/{id}")
    public LiterarySeries obtener(@PathVariable Long id){
        return service.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LiterarySeries crear(@RequestBody LiterarySeries series){
        return service.crear(series);
    }

    @PutMapping ("/{id}")
    public LiterarySeries actualizar(@PathVariable Long id, @RequestBody LiterarySeries literarySeries){
        return service.actualizar(id, literarySeries);
    }

    @DeleteMapping ("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id){
        service.eliminar(id);
    }

}
