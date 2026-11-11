package mx.uv.booknest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mx.uv.booknest.models.Author;

/**
 * 
 * @author Jetzaly
 * 
 */

public interface AuthorRepository extends JpaRepository <Author, Long> {

}
