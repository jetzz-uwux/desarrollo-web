package mx.uv.booknest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mx.uv.booknest.models.Users;

public interface UsersRepository extends JpaRepository <Users, Long> {}
