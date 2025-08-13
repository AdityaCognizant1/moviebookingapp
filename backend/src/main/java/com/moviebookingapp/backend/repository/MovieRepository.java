package com.moviebookingapp.backend.repository;

import com.moviebookingapp.backend.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
