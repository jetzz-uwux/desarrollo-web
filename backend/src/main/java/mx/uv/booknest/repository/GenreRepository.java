package mx.uv.booknest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mx.uv.booknest.models.Genre;

/**
 * 
 * @author Jetzaly
 * 
 */

public interface GenreRepository extends JpaRepository <Genre, Long>{

}
