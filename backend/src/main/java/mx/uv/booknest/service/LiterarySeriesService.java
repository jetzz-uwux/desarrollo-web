package mx.uv.booknest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import mx.uv.booknest.models.LiterarySeries;
import mx.uv.booknest.repository.LiterarySeriesRepository;

/**
 * 
 * @author Jetzaly
 * 
 */

@Service
public class LiterarySeriesService {
    private final LiterarySeriesRepository repository;

    public LiterarySeriesService(LiterarySeriesRepository repository){
        this.repository=repository;
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

}
