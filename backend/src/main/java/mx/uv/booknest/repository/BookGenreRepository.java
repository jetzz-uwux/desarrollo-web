package mx.uv.booknest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mx.uv.booknest.models.BookGenre;


/**
 * 
 * @author Jetzaly
 * 
 */

public interface BookGenreRepository extends JpaRepository<BookGenre, Long>{

}
