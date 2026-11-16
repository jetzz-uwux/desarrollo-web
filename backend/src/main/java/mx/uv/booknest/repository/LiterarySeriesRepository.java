package mx.uv.booknest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.uv.booknest.models.LiterarySeries;

/**
 * 
 * @author Jetzaly
 * 
 */

public interface LiterarySeriesRepository extends JpaRepository <LiterarySeries, Long> {
    List<LiterarySeries> findBySeriesType(String seriesType);

}
