package mx.uv.booknest.models;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * @author Hans Arturo
 */

@Entity
@Table(name = "book_files")
public class BookFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "uploader_user_id")
    private Users uploaderUser;

    @Column(name = "file_path", nullable = false, length = 500, unique = true)
    private String filePath;

    @Column(name = "file_size_bytes", nullable = false)
    private Long fileSizeBytes;

    @Column(name = "language", nullable = false, length = 2)
    private String language;

    @Column(name = "edition_isbn", length = 17)
    private String editionIsbn;

    @Column(name = "is_authorized", nullable = false)
    private Boolean isAuthorized = false;

    @Column(name = "original_filename", length = 255)
    private String originalFilename;

    @Column(name = "checksum_sha256", length = 64, columnDefinition = "bpchar(64)")
    private String checksumSha256;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    public BookFile() {
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public Users getUploaderUser() {
        return uploaderUser;
    }

    public String getFilePath() {
        return filePath;
    }

    public Long getFileSizeBytes() {
        return fileSizeBytes;
    }

    public String getLanguage() {
        return language;
    }

    public String getEditionIsbn() {
        return editionIsbn;
    }

    public Boolean getIsAuthorized() {
        return isAuthorized;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getChecksumSha256() {
        return checksumSha256;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setUploaderUser(Users uploaderUser) {
        this.uploaderUser = uploaderUser;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileSizeBytes(Long fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setEditionIsbn(String editionIsbn) {
        this.editionIsbn = editionIsbn;
    }

    public void setIsAuthorized(Boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public void setChecksumSha256(String checksumSha256) {
        this.checksumSha256 = checksumSha256;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}