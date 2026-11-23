package mx.uv.booknest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import mx.uv.booknest.models.BookSeries;

/**
 * 
 * @author Jetzaly
 * 
 */

public interface BookSeriesRepository extends JpaRepository <BookSeries,Long> {
    boolean existsBySeriesId (Long seriesId);
    boolean existsByBookIdAndSeriesId(Long bookId, Long seriesId);
    boolean existsBySeriesIdAndVolumeNumber(Long seriesId, Integer volumeNumber);
    boolean existsBySeriesIdAndReadingOrder(Long seriesId, Integer readingOrder);
    List<BookSeries> findByBookId(Long bookId);
    List<BookSeries> findBySeriesIdIn(List<Long> seriesIds);
    List<BookSeries> findBySeriesIdOrderByReadingOrderAsc(Long seriesId);
}
