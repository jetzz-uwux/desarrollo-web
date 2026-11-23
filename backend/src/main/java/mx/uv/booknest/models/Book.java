package mx.uv.booknest.models;

import java.time.LocalDate;

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
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // BigSerial su equivalente en Java es Long
    private String mainTitle;
    private LocalDate originalPublicationDate;
    private String synopsis;
    private Integer totalPages;
    private String coverUrl;
    private String category; // Clasificación independiente del género (p. ej., ficción, no ficción)
    private Long createdByUserId; // BigInt su equivalente en Java es Long

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Book(){} // Constructor vacío

    // Getters
    public Long getId() {
        return id;
    }
    public String getMainTitle() {
        return mainTitle;
    }
    public LocalDate getOriginalPublicationDate() {
        return originalPublicationDate;
    }
    public String getSynopsis() {
        return synopsis;
    }
    public Integer getTotalPages() {
        return totalPages;
    }
    public String getCoverUrl() {
        return coverUrl;
    }
public Long getCreatedByUserId() {
    return createdByUserId;
}

    // Setters
    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }
    public void setOriginalPublicationDate(LocalDate originalPublicationDate) {
        this.originalPublicationDate = originalPublicationDate;
    }
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
    public void setCreatedByUserId (Long createdByUserId ) {
        this.createdByUserId = createdByUserId;
    }

}
