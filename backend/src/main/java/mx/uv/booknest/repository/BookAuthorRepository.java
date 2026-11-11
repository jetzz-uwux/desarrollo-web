package mx.uv.booknest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mx.uv.booknest.models.BookAuthor;

/**
 * 
 * @author Jetzaly
 * 
 */

public interface BookAuthorRepository extends JpaRepository <BookAuthor, Long> {

}
