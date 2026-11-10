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
@Table(name = "book_genres")
public class BookGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long bookid;
    private Long genreid;

    public BookGenre(){} // Constructor vacío

    // Getters
    public Long getId() {
        return id;
    }
    public Long getBookid() {
        return bookid;
    }
    public Long getGenreid() {
        return genreid;
    }

    // Setters
    public void setBookid(Long bookid) {
        this.bookid = bookid;
    }
    public void setGenreid(Long genreid) {
        this.genreid = genreid;
    }

}
