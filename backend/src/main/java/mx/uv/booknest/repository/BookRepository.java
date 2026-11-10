package mx.uv.booknest.repository;

import org.springframework.data.repository.CrudRepository;
import mx.uv.booknest.models.Book;

/**
 * 
 * @author Jetzaly
 * 
 */

public interface BookRepository extends CrudRepository <Book, Long>  {

}
