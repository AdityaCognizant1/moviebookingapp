package com.moviebookingapp.backend.repository;

import com.moviebookingapp.backend.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    
    @Query("SELECT s FROM Showtime s WHERE s.movie.id = :movieId ORDER BY s.showTime ASC")
    List<Showtime> findByMovieIdOrderByShowTime(@Param("movieId") Long movieId);
}
