package mx.uv.booknest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import mx.uv.booknest.models.Author;

/**
 * 
 * @author Jetzaly
 * 
 */

public interface AuthorRepository extends JpaRepository <Author, Long> {
    List<Author> findByNameContainingIgnoreCase(String nombre);
}
