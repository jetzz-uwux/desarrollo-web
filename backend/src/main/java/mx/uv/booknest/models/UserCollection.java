package mx.uv.booknest.models;

import java.time.OffsetDateTime;
import jakarta.persistence.*;

@Entity 
@Table(name = "collections")
public class UserCollection { 
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK con Users
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    private String name;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public UserCollection() {
        // Constructor vacío
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}