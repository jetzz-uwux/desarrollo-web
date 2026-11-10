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
    private Long bookid;
    private Long authorid;

    public BookAuthor() {}

    // Getters
    public Long getId() {
        return id;
    }
    public Long getBookid() {
        return bookid;
    }
    public Long getAuthorid() {
        return authorid;
    }

    // Setters
    public void setBookid(Long bookid) {
        this.bookid = bookid;
    }
    public void setAuthorid(Long authorid) {
        this.authorid = authorid;
    }
}
