package mx.uv.booknest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mx.uv.booknest.models.BookSeries;

/**
 * 
 * @author Jetzaly
 * 
 */

public interface BookSeriesRepository extends JpaRepository <BookSeries,Long> {

}
