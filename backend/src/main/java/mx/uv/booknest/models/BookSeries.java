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
@Table(name = "book_series")
public class BookSeries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long bookId;
    private Long seriesId;
    private Integer volumeNumber;
    private Integer readingOrder;

    public BookSeries(){} // Constructor vacío

    // Getters
    public Long getId() {
        return id;
    }
    public Long getBookId() {
        return bookId;
    }
    public Long getSeriesId() {
        return seriesId;
    }
    public Integer getVolumeNumber() {
        return volumeNumber;
    }
    public Integer getReadingOrder() {
        return readingOrder;
    }

    // Setters
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    public void setSeriesId(Long seriesId) {
        this.seriesId = seriesId;
    }
    public void setVolumeNumber(Integer volumeNumber) {
        this.volumeNumber = volumeNumber;
    }
    public void setReadingOrder(Integer readingOrder) {
        this.readingOrder = readingOrder;
    }
    
}
