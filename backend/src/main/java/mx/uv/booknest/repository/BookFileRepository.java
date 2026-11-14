package mx.uv.booknest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.uv.booknest.models.BookFile;

/**
 * @author Hans Arturo
 */
public interface BookFileRepository extends JpaRepository<BookFile, Long> {
    List<BookFile> findByBookId(Long bookId);
    List<BookFile> findByBookIdAndLanguage(Long bookId, String language);
    Optional<BookFile> findByChecksumSha256(String checksumSha256);
    Optional<BookFile> findByEditionIsbn(String editionIsbn);
}