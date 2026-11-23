package mx.uv.booknest.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import mx.uv.booknest.models.BookSeries;
import mx.uv.booknest.repository.BookSeriesRepository;

/** Relaciones y orden de lectura; pertenece al módulo Catálogo y Series. */
@Service
public class BookSeriesService {
    private final BookSeriesRepository repository;
    private final BookService bookService;
    private final LiterarySeriesService seriesService;

    public BookSeriesService(BookSeriesRepository repository, BookService bookService,
                             LiterarySeriesService seriesService) {
        this.repository = repository;
        this.bookService = bookService;
        this.seriesService = seriesService;
    }

    public List<BookSeries> listar(Long seriesId) {
        seriesService.obtenerPorId(seriesId);
        return repository.findBySeriesIdOrderByReadingOrderAsc(seriesId);
    }

    public BookSeries asociar(Long seriesId, BookSeries datos) {
        seriesService.obtenerPorId(seriesId);
        if (datos == null || datos.getBookId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selecciona un libro");
        }
        bookService.obtenerPorId(datos.getBookId());
        if (datos.getVolumeNumber() == null || datos.getVolumeNumber() < 1
                || datos.getReadingOrder() == null || datos.getReadingOrder() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Volumen y orden de lectura deben ser positivos");
        }
        if (repository.existsByBookIdAndSeriesId(datos.getBookId(), seriesId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El libro ya pertenece a esta serie");
        }
        if (repository.existsBySeriesIdAndVolumeNumber(seriesId, datos.getVolumeNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe ese volumen en la serie");
        }
        if (repository.existsBySeriesIdAndReadingOrder(seriesId, datos.getReadingOrder())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El orden de lectura ya está ocupado");
        }
        BookSeries relacion = new BookSeries();
        relacion.setSeriesId(seriesId);
        relacion.setBookId(datos.getBookId());
        relacion.setVolumeNumber(datos.getVolumeNumber());
        relacion.setReadingOrder(datos.getReadingOrder());
        return repository.save(relacion);
    }

    public void quitar(Long seriesId, Long bookId) {
        seriesService.obtenerPorId(seriesId);
        BookSeries relacion = repository.findByBookId(bookId).stream()
            .filter(r -> seriesId.equals(r.getSeriesId())).findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "El libro no pertenece a esta serie"));
        repository.delete(relacion);
    }
}
