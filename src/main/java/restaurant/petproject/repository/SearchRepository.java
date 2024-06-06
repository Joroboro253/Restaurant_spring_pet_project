package restaurant.petproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import restaurant.petproject.entity.SearchResult;

public interface SearchRepository extends JpaRepository<SearchResult, Long> {

    @Query("SELECT s FROM SearchResult s WHERE s.title LIKE %:query% OR s.description LIKE %:query%")
    List<SearchResult> findByQuery(String query);
}

