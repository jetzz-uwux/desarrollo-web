package mx.uv.booknest.repository;

import org.springframework.data.repository.CrudRepository;
import mx.uv.booknest.models.BookSeries;

/**
 * 
 * @author Jetzaly
 * 
 */

public interface BookSeriesRepository extends CrudRepository <BookSeries,Long> {

}
