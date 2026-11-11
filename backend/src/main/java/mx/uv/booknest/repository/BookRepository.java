package mx.uv.booknest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mx.uv.booknest.models.Book;

/**
 * 
 * @author Jetzaly
 * 
 */

public interface BookRepository extends JpaRepository <Book, Long>  {

}
