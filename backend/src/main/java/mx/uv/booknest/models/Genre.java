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
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // BigSerial su equivalente en Java es Long
    private String name;
    
    public Genre(){} // Constructor vacío

    // Getters
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
}
