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
@Table(name = "literary_series")
public class LiterarySeries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // BigSerial su equivalente en Java es Long
    private String name;
    private String seriesType;

    public LiterarySeries(){} // Constructor vacío

    // Getters
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getSeriesType() {
        return seriesType;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setSeriesType(String seriesType) {
        this.seriesType = seriesType;
    }

}
