package mx.uv.booknest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import mx.uv.booknest.models.BookAuthor;

/**
 * 
 * @author Jetzaly
 * 
 */

public interface BookAuthorRepository extends JpaRepository <BookAuthor, Long> {
    boolean existsByAuthorId (Long authorId);
    boolean existsByBookIdAndAuthorId(Long bookId, Long authorId);
    
    List<BookAuthor> findByAuthorIdIn(List<Long> authorIds);
    List<BookAuthor> findByAuthorId(Long authorId);
}
