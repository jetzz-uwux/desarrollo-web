package mx.uv.booknest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.uv.booknest.models.UserCollection;

public interface UserCollectionRepository extends JpaRepository <UserCollection, Long> {
    
}
