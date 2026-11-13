package mx.uv.booknest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mx.uv.booknest.models.Rating;

/**
 * @author Camila
 */
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    // Proyección para calcular promedios globales directamente en la consulta
    public interface GlobalStat {
        Long getBookId();
        Double getAverageScore();
        Integer getTotalRatings();
    }

    @Query("SELECT r.book.id AS bookId, ROUND(AVG(r.score), 2) AS averageScore, COUNT(r.id) AS totalRatings FROM Rating r GROUP BY r.book.id")
    List<GlobalStat> findGlobalStats();
}