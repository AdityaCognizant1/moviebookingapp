package com.moviebookingapp.backend.controller;

import com.moviebookingapp.backend.entity.Showtime;
import com.moviebookingapp.backend.entity.Movie;
import com.moviebookingapp.backend.entity.Theater;
import com.moviebookingapp.backend.service.ShowtimeService;
import com.moviebookingapp.backend.service.MovieService;
import com.moviebookingapp.backend.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/showtimes")
public class ShowtimeController {
    @Autowired
    private ShowtimeService showtimeService;
    
    @Autowired
    private MovieService movieService;
    
    @Autowired
    private TheaterService theaterService;

    @GetMapping
    public List<Showtime> getAllShowtimes() {
        return showtimeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Showtime> getShowtimeById(@PathVariable Long id) {
        Optional<Showtime> showtime = showtimeService.findById(id);
        return showtime.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // USER-FRIENDLY ENDPOINT - Get showtimes for a specific movie
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<?> getShowtimesByMovie(@PathVariable Long movieId) {
        try {
            List<Showtime> showtimes = showtimeService.findByMovieId(movieId);
            if (showtimes.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                    "message", "No showtimes found for this movie",
                    "movieId", movieId,
                    "showtimes", showtimes
                ));
            }
            return ResponseEntity.ok(Map.of(
                "movieId", movieId,
                "showtimes", showtimes,
                "count", showtimes.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to load showtimes for movie: " + e.getMessage()));
        }
    }

    // ADMIN ONLY OPERATIONS - Require ADMIN role
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createShowtime(@RequestBody Map<String, Object> showtimeData) {
        try {
            // Extract data from JSON
            Long movieId = Long.valueOf(showtimeData.get("movieId").toString());
            Long theaterId = Long.valueOf(showtimeData.get("theaterId").toString());
            String showTimeStr = showtimeData.get("showTime").toString();
            
            // Find the movie and theater
            Movie movie = movieService.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found with ID: " + movieId));
            Theater theater = theaterService.findById(theaterId)
                .orElseThrow(() -> new RuntimeException("Theater not found with ID: " + theaterId));
            
            // Create showtime with proper movie and theater reference
            Showtime showtime = Showtime.builder()
                .movie(movie)
                .theater(theater)
                .showTime(LocalDateTime.parse(showTimeStr))
                .build();
            
            Showtime savedShowtime = showtimeService.save(showtime);
            return ResponseEntity.ok(savedShowtime);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create showtime: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateShowtime(@PathVariable Long id, @RequestBody Map<String, Object> showtimeData) {
        try {
            // Check if showtime exists
            Optional<Showtime> existingShowtime = showtimeService.findById(id);
            if (existingShowtime.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            // Extract data from JSON
            Long movieId = Long.valueOf(showtimeData.get("movieId").toString());
            Long theaterId = Long.valueOf(showtimeData.get("theaterId").toString());
            String showTimeStr = showtimeData.get("showTime").toString();
            
            // Find the movie and theater
            Movie movie = movieService.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found with ID: " + movieId));
            Theater theater = theaterService.findById(theaterId)
                .orElseThrow(() -> new RuntimeException("Theater not found with ID: " + theaterId));
            
            // Update showtime with proper movie and theater reference
            Showtime showtime = Showtime.builder()
                .id(id)
                .movie(movie)
                .theater(theater)
                .showTime(LocalDateTime.parse(showTimeStr))
                .build();
            
            Showtime updatedShowtime = showtimeService.save(showtime);
            return ResponseEntity.ok(updatedShowtime);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update showtime: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
        try {
            showtimeService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
