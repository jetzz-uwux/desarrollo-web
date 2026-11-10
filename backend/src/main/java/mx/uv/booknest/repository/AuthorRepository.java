package mx.uv.booknest.repository;

import org.springframework.data.repository.CrudRepository;
import mx.uv.booknest.models.Author;

/**
 * 
 * @author Jetzaly
 * 
 */

public interface AuthorRepository extends CrudRepository<Author, Integer> {

}
