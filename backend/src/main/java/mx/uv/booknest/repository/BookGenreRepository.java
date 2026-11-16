package mx.uv.booknest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import mx.uv.booknest.models.BookGenre;

/**
 * 
 * @author Jetzaly
 * 
 */

public interface BookGenreRepository extends JpaRepository<BookGenre, Long>{
    boolean existsByGenreId(Long genreId);
    boolean existsByBookIdAndGenreId(Long bookId, Long genreId);

    List<BookGenre> findByGenreId(Long genreId);
}
