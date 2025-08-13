package com.moviebookingapp.backend.controller;

import com.moviebookingapp.backend.entity.Seat;
import com.moviebookingapp.backend.entity.Showtime;
import com.moviebookingapp.backend.service.SeatService;
import com.moviebookingapp.backend.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/seats")
public class SeatController {
    
    @Autowired
    private SeatService seatService;
    
    @Autowired
    private ShowtimeService showtimeService;

    @GetMapping("/showtime/{showtimeId}")
    public ResponseEntity<?> getSeatsByShowtime(@PathVariable Long showtimeId) {
        try {
            Optional<Showtime> showtimeOpt = showtimeService.findById(showtimeId);
            if (showtimeOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Showtime not found"));
            }
            
            Showtime showtime = showtimeOpt.get();
            List<Seat> seats = seatService.findByShowtime(showtime);
            
            // If no seats exist, generate them
            if (seats.isEmpty()) {
                seatService.generateSeatsForShowtime(showtime);
                seats = seatService.findByShowtime(showtime);
            }
            
            return ResponseEntity.ok(Map.of(
                "showtime", showtime,
                "seats", seats,
                "availableCount", seatService.countAvailableSeats(showtime)
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to load seats: " + e.getMessage()));
        }
    }

    @GetMapping("/available/showtime/{showtimeId}")
    public ResponseEntity<?> getAvailableSeats(@PathVariable Long showtimeId) {
        try {
            Optional<Showtime> showtimeOpt = showtimeService.findById(showtimeId);
            if (showtimeOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Showtime not found"));
            }
            
            List<Seat> availableSeats = seatService.findAvailableSeats(showtimeOpt.get());
            return ResponseEntity.ok(availableSeats);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to load available seats: " + e.getMessage()));
        }
    }
}
