package mx.uv.booknest.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import mx.uv.booknest.models.CollectionBooks;

public interface CollectionBooksRepository extends JpaRepository <CollectionBooks, Long>{}
