package com.moviebookingapp.backend.controller;

import com.moviebookingapp.backend.entity.Theater;
import com.moviebookingapp.backend.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/theaters")
public class TheaterController {
    
    @Autowired
    private TheaterService theaterService;

    @GetMapping
    public ResponseEntity<List<Theater>> getAllTheaters() {
        List<Theater> theaters = theaterService.findAll();
        return ResponseEntity.ok(theaters);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Theater>> getActiveTheaters() {
        List<Theater> theaters = theaterService.findActiveTheaters();
        return ResponseEntity.ok(theaters);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTheaterById(@PathVariable Long id) {
        Optional<Theater> theater = theaterService.findById(id);
        if (theater.isPresent()) {
            return ResponseEntity.ok(theater.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Theater> createTheater(@RequestBody Theater theater) {
        Theater savedTheater = theaterService.save(theater);
        return ResponseEntity.ok(savedTheater);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateTheater(@PathVariable Long id, @RequestBody Theater theater) {
        Optional<Theater> existingTheater = theaterService.findById(id);
        if (existingTheater.isPresent()) {
            theater.setId(id);
            Theater updatedTheater = theaterService.save(theater);
            return ResponseEntity.ok(updatedTheater);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTheater(@PathVariable Long id) {
        try {
            theaterService.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Theater deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to delete theater: " + e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Theater>> searchTheaters(@RequestParam String name) {
        List<Theater> theaters = theaterService.searchByName(name);
        return ResponseEntity.ok(theaters);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getTheaterCount() {
        Long count = theaterService.countActiveTheaters();
        return ResponseEntity.ok(Map.of("count", count));
    }
}
