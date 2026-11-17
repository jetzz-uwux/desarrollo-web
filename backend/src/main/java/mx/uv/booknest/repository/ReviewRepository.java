package mx.uv.booknest.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import mx.uv.booknest.models.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByBookId(Long bookId);
    List<Review> findByBookIdOrderByIdDesc(Long bookId);
    List<Review> findByUserIdOrderByIdDesc(Long userId);
    List<Review> findByBookIdAndUserIdOrderByIdDesc(Long bookId, Long userId);
}