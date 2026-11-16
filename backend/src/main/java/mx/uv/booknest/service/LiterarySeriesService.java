package mx.uv.booknest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import mx.uv.booknest.models.LiterarySeries;
import mx.uv.booknest.repository.BookSeriesRepository;
import mx.uv.booknest.repository.LiterarySeriesRepository;

/**
 * 
 * @author Jetzaly
 * 
 */

@Service
public class LiterarySeriesService {
    private final LiterarySeriesRepository repository;
    private final BookSeriesRepository bookSeriesRepository;

    public LiterarySeriesService(LiterarySeriesRepository repository, BookSeriesRepository bookSeriesRepository){
        this.repository=repository;
        this.bookSeriesRepository=bookSeriesRepository;
    }

    public List<LiterarySeries> listar(){
        return repository.findAll();
    }

    public LiterarySeries obtenerPorId(Long id){
        Optional<LiterarySeries> resultado = repository.findById(id);
        if (resultado.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Serie no encontrada");        
        }
        return resultado.get();
    }

    public LiterarySeries crear (LiterarySeries series){
        if (series.getName() == null || series.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
        }
        else if (series.getSeriesType()==null || !(series.getSeriesType().equals("bilogia") || series.getSeriesType().equals("trilogia") || series.getSeriesType().equals("saga"))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de serie no valido, los valores permitidos son: bilogia, trilogia o saga");
        }
        return repository.save(series);
    }

    public LiterarySeries actualizar(Long id, LiterarySeries datos){
        LiterarySeries existente = obtenerPorId(id);

        if (datos.getName() == null || datos.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
        }

        else if (datos.getSeriesType()==null || !(datos.getSeriesType().equals("bilogia") || datos.getSeriesType().equals("trilogia") || datos.getSeriesType().equals("saga"))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de serie no valido, los valores permitidos son: bilogia, trilogia o saga");
        }

        existente.setName(datos.getName());
        existente.setSeriesType(datos.getSeriesType());
        return repository.save(existente);
    }

    public void eliminar(Long id) {
        LiterarySeries existente = obtenerPorId(id);
        
        if (bookSeriesRepository.existsBySeriesId(id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede eliminar esta serie porque está asociado a uno o más libros");
        }

        repository.delete(existente);
    }

}
