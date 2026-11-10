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
    private String maintitle;
    private LocalDate originalpublicationdate;
    private String synopsis;
    private Integer totalpages;
    private String coverurl;
    private Long createduserid; // BigSerial su equivalente en Java es Long

    public Book(){} // Constructor vacío

    // Getters
    public Long getId() {
        return id;
    }
    public String getMaintitle() {
        return maintitle;
    }
    public LocalDate getOriginalpublicationdate() {
        return originalpublicationdate;
    }
    public String getSynopsis() {
        return synopsis;
    }
    public Integer getTotalpages() {
        return totalpages;
    }
    public String getCoverurl() {
        return coverurl;
    }
    public Long getCreateduserid() {
        return createduserid;
    }

    // Setters
    public void setMaintitle(String maintitle) {
        this.maintitle = maintitle;
    }
    public void setOriginalpublicationdate(LocalDate originalpublicationdate) {
        this.originalpublicationdate = originalpublicationdate;
    }
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
    public void setTotalpages(Integer totalpages) {
        this.totalpages = totalpages;
    }
    public void setCoverurl(String coverurl) {
        this.coverurl = coverurl;
    }
    public void setCreateduserid(Long createduserid) {
        this.createduserid = createduserid;
    }

}
