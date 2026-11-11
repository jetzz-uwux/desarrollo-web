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
    private Long bookId;
    private Long genreId;

    public BookGenre(){} // Constructor vacío

    // Getters
    public Long getId() {
        return id;
    }
    public Long getBookId() {
        return bookId;
    }
    public Long getGenreId() {
        return genreId;
    }

    // Setters
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

}
