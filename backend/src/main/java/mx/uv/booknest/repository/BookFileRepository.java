package mx.uv.booknest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.uv.booknest.models.BookFile;

/**
 * @author Hans Arturo
 */
public interface BookFileRepository extends JpaRepository<BookFile, Long> {
    boolean existsByBookId(Long bookId);
    List<BookFile> findByBookId(Long bookId);
    List<BookFile> findByLanguage(String language);
    List<BookFile> findByBookIdAndLanguage(Long bookId, String language);
    List<BookFile> findByOriginalFilenameContainingIgnoreCase(String query);
    Optional<BookFile> findByChecksumSha256(String checksumSha256);
    Optional<BookFile> findByEditionIsbn(String editionIsbn);
}