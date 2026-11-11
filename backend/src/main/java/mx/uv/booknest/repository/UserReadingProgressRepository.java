package mx.uv.booknest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mx.uv.booknest.models.UserReadingProgress;

public interface UserReadingProgressRepository extends JpaRepository <UserReadingProgress, Long>{}
