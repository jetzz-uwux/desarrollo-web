package mx.uv.booknest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.uv.booknest.models.Review;

/**
 * @author Camila
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBookIdOrderByIdDesc(Long bookId);
}