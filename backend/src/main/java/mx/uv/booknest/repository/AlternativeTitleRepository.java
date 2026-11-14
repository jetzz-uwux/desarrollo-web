package mx.uv.booknest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.uv.booknest.models.AlternativeTitle;

/**
 * @author Hans Arturo
 */
public interface AlternativeTitleRepository extends JpaRepository<AlternativeTitle, Long> {
    List<AlternativeTitle> findByBookId(Long bookId);
    Optional<AlternativeTitle> findByBookIdAndTitleIgnoreCaseAndLanguage(Long bookId, String title, String language);
}