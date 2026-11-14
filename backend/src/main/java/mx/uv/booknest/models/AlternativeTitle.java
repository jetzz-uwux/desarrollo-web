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
@Table(name = "alternative_titles")
public class AlternativeTitle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, length = 2)
    private String language;

    @ManyToOne
    @JoinColumn(name = "contributed_by_user_id")
    private Users contributedByUser;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    public AlternativeTitle() {
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public String getTitle() {
        return title;
    }

    public String getLanguage() {
        return language;
    }

    public Users getContributedByUser() {
        return contributedByUser;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setContributedByUser(Users contributedByUser) {
        this.contributedByUser = contributedByUser;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}