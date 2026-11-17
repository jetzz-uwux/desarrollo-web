package mx.uv.booknest.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import mx.uv.booknest.models.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    boolean existsByBookId(Long bookId);
    Optional<Rating> findByUserIdAndBookId(Long userId, Long bookId);
    List<Rating> findByBookId(Long bookId);

    public interface GlobalStat {
        Long getBookId();
        String getMainTitle();
        Double getAverageScore();
        Integer getTotalRatings();
    }

    public interface PopularBookStat {
        Long getBookId();
        String getMainTitle();
        Long getFinishedReadersCount();
    }

    public interface UserPersonalStat {
        String getStatus();
        Long getTotal();
    }

    // RF-16, RF-18, RF-22: Promedio y total de valoraciones por libro (o todos)
    @Query(value = """
        SELECT b.id AS bookId,
               b.main_title AS mainTitle,
               ROUND(AVG(r.score)::NUMERIC, 2) AS averageScore,
               COUNT(r.id) AS totalRatings
        FROM books b
        LEFT JOIN ratings r ON r.book_id = b.id
        WHERE (:bookId IS NULL OR b.id = :bookId)
        GROUP BY b.id, b.main_title
        ORDER BY averageScore DESC NULLS LAST, totalRatings DESC
        """, nativeQuery = true)
    List<GlobalStat> findBookRatingStats(@Param("bookId") Long bookId);

    // RF-20, RF-22: Ranking de más leídos (usuarios distintos que marcaron 'terminado')
    @Query(value = """
        SELECT b.id AS bookId,
               b.main_title AS mainTitle,
               COUNT(DISTINCT p.user_id) AS finishedReadersCount
        FROM books b
        LEFT JOIN user_reading_progress p ON p.book_id = b.id AND p.status = 'terminado'
        GROUP BY b.id, b.main_title
        ORDER BY finishedReadersCount DESC, b.id ASC
        """, nativeQuery = true)
    List<PopularBookStat> findMostReadBooks();

    // RF-13: Estadísticas personales por estado de lectura
    @Query(value = """
        SELECT status AS status, COUNT(*) AS total
        FROM user_reading_progress
        WHERE user_id = :userId
        GROUP BY status
        """, nativeQuery = true)
    List<UserPersonalStat> findUserProgressStats(@Param("userId") Long userId);
}