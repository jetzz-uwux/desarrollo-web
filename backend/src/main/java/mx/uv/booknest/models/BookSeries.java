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
    private Long bookid;
    private Long seriesid;
    private Integer volumenumber;
    private Integer readingorder;

    public BookSeries(){} // Constructor vacío

    // Getters
    public Long getId() {
        return id;
    }
    public Long getBookid() {
        return bookid;
    }
    public Long getSeriesid() {
        return seriesid;
    }
    public Integer getVolumenumber() {
        return volumenumber;
    }
    public Integer getReadingorder() {
        return readingorder;
    }

    // Setters
    public void setBookid(Long bookid) {
        this.bookid = bookid;
    }
    public void setSeriesid(Long seriesid) {
        this.seriesid = seriesid;
    }
    public void setVolumenumber(Integer volumenumber) {
        this.volumenumber = volumenumber;
    }
    public void setReadingorder(Integer readingorder) {
        this.readingorder = readingorder;
    }
    
}
