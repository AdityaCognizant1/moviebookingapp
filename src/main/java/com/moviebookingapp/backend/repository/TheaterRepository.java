package com.moviebookingapp.backend.repository;

import com.moviebookingapp.backend.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
    
    List<Theater> findByIsActiveTrue();
    
    List<Theater> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT t FROM Theater t WHERE t.isActive = true ORDER BY t.name")
    List<Theater> findActiveTheatersOrderByName();
    
    @Query("SELECT COUNT(t) FROM Theater t WHERE t.isActive = true")
    Long countActiveTheaters();
}
