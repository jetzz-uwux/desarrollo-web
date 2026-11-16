package mx.uv.booknest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.uv.booknest.models.Review;

/**
 * @author Camila
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByBookId(Long bookId);
    List<Review> findByBookIdOrderByIdDesc(Long bookId);
}