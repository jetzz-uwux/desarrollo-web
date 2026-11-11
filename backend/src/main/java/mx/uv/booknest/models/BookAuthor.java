package mx.uv.booknest.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 
 * @author Jetzaly
 * 
 */

@Entity
@Table(name = "book_authors")
public class BookAuthor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long bookId;
    private Long authorId;

    public BookAuthor() {}

    // Getters
    public Long getId() {
        return id;
    }
    public Long getBookId() {
        return bookId;
    }
    public Long getAuthorId() {
        return authorId;
    }

    // Setters
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
