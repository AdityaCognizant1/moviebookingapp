package com.moviebookingapp.backend.controller;

import com.moviebookingapp.backend.entity.Booking;
import com.moviebookingapp.backend.entity.Seat;
import com.moviebookingapp.backend.entity.Showtime;
import com.moviebookingapp.backend.entity.User;
import com.moviebookingapp.backend.service.BookingService;
import com.moviebookingapp.backend.service.ShowtimeService;
import com.moviebookingapp.backend.service.UserService;
import com.moviebookingapp.backend.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ShowtimeService showtimeService;
    
    @Autowired
    private SeatService seatService;

    // USER-SCOPED OPERATIONS - Users can only see their own bookings
    
    @GetMapping
    public ResponseEntity<?> getMyBookings() {
        try {
            // Get current user from security context
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            
            // Find user
            Optional<User> userOpt = userService.findByUsername(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not found"));
            }
            
            // Get bookings for this user only
            List<Booking> userBookings = bookingService.findByUser(userOpt.get());
            
            return ResponseEntity.ok(Map.of(
                "user", username,
                "bookings", userBookings,
                "count", userBookings.size()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to load bookings: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMyBookingById(@PathVariable Long id) {
        try {
            // Get current user from security context
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            
            // Find user
            Optional<User> userOpt = userService.findByUsername(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not found"));
            }
            
            // Get booking and verify it belongs to this user
            Optional<Booking> bookingOpt = bookingService.findById(id);
            if (bookingOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Booking booking = bookingOpt.get();
            
            // Security check: Only allow users to view their own bookings
            if (!booking.getUser().getId().equals(userOpt.get().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Access denied: You can only view your own bookings"));
            }
            
            return ResponseEntity.ok(booking);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to load booking: " + e.getMessage()));
        }
    }
    
    // ADMIN ONLY - View all bookings (for management purposes)
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Booking> getAllBookingsAdmin() {
        return bookingService.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Map<String, Object> bookingRequest) {
        try {
            // Extract data from request
            Long showtimeId = Long.valueOf(bookingRequest.get("showtimeId").toString());
            Integer numTickets = Integer.valueOf(bookingRequest.get("numTickets").toString());
            
            // Extract selected seat numbers
            @SuppressWarnings("unchecked")
            List<String> selectedSeats = (List<String>) bookingRequest.get("selectedSeats");
            
            // Get current user from security context
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            
            // Find user and showtime
            Optional<User> userOpt = userService.findByUsername(username);
            Optional<Showtime> showtimeOpt = showtimeService.findById(showtimeId);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not found"));
            }
            
            if (showtimeOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Showtime not found"));
            }
            
            Showtime showtime = showtimeOpt.get();
            
            // Validate selected seats
            if (selectedSeats == null || selectedSeats.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "No seats selected"));
            }
            
            if (selectedSeats.size() != numTickets) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Number of selected seats doesn't match ticket count"));
            }
            
            // Check if selected seats are available
            if (!seatService.areSeatsAvailable(selectedSeats, showtime)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "One or more selected seats are not available"));
            }
            
            // Create booking
            Booking booking = Booking.builder()
                    .user(userOpt.get())
                    .showtime(showtime)
                    .numTickets(numTickets)
                    .seatNumbers(String.join(",", selectedSeats))
                    .bookingTime(LocalDateTime.now())
                    .build();
            
            // Save booking first
            Booking savedBooking = bookingService.save(booking);
            
            // Book the specific seats
            seatService.bookSeats(selectedSeats, showtime, savedBooking);
            
            // Update available seats count
            Integer availableSeats = seatService.countAvailableSeats(showtime);
            showtime.setAvailableSeats(availableSeats);
            showtimeService.save(showtime);
            
            return ResponseEntity.ok(savedBooking);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Failed to create booking: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id, Authentication authentication) {
        try {
            String username = authentication.getName();
            Optional<User> userOpt = userService.findByUsername(username);
            
            if (!userOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not found"));
            }
            
            Optional<Booking> bookingOpt = bookingService.findById(id);
            if (!bookingOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Booking not found"));
            }
            
            Booking booking = bookingOpt.get();
            
            // Check if the booking belongs to the current user
            if (!booking.getUser().getId().equals(userOpt.get().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "You can only cancel your own bookings"));
            }
            
            // Unbook the seats first
            List<Seat> bookedSeats = seatService.findByShowtime(booking.getShowtime())
                .stream()
                .filter(seat -> seat.getBooking() != null && seat.getBooking().getId().equals(id))
                .collect(java.util.stream.Collectors.toList());
            
            for (Seat seat : bookedSeats) {
                seat.setIsBooked(false);
                seat.setBooking(null);
                seatService.save(seat);
            }
            
            // Delete the booking
            bookingService.deleteById(id);
            
            return ResponseEntity.ok(Map.of("message", "Booking cancelled successfully"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Failed to cancel booking: " + e.getMessage()));
        }
    }
}
