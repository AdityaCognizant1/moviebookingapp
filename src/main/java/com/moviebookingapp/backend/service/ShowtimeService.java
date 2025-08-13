package com.moviebookingapp.backend.service;

import com.moviebookingapp.backend.entity.Showtime;
import com.moviebookingapp.backend.entity.Seat;
import com.moviebookingapp.backend.repository.ShowtimeRepository;
import com.moviebookingapp.backend.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ShowtimeService {
    @Autowired
    private ShowtimeRepository showtimeRepository;
    
    @Autowired
    private SeatRepository seatRepository;

    public List<Showtime> findAll() {
        List<Showtime> showtimes = showtimeRepository.findAll();
        // Sort by show time for better user experience
        showtimes.sort((s1, s2) -> s1.getShowTime().compareTo(s2.getShowTime()));
        return calculateAvailableSeats(showtimes);
    }

    public Optional<Showtime> findById(Long id) {
        Optional<Showtime> showtimeOpt = showtimeRepository.findById(id);
        if (showtimeOpt.isPresent()) {
            Showtime showtime = showtimeOpt.get();
            calculateAvailableSeats(showtime);
            return Optional.of(showtime);
        }
        return Optional.empty();
    }

    public Showtime save(Showtime showtime) {
        return showtimeRepository.save(showtime);
    }
    
    public void deleteById(Long id) {
        showtimeRepository.deleteById(id);
    }
    
    public List<Showtime> findByMovieId(Long movieId) {
        List<Showtime> showtimes = showtimeRepository.findByMovieIdOrderByShowTime(movieId);
        return calculateAvailableSeats(showtimes);
    }
    
    // Calculate available seats for a list of showtimes
    private List<Showtime> calculateAvailableSeats(List<Showtime> showtimes) {
        for (Showtime showtime : showtimes) {
            calculateAvailableSeats(showtime);
        }
        return showtimes;
    }
    
    // Calculate available seats for a single showtime
    private void calculateAvailableSeats(Showtime showtime) {
        Integer availableSeatsFromRepo = seatRepository.countAvailableSeats(showtime);
        if (availableSeatsFromRepo == null || availableSeatsFromRepo == 0) {
            // If no seat data exists, check if any seats exist at all
            List<Seat> existingSeats = seatRepository.findByShowtimeOrderBySeatNumber(showtime);
            if (existingSeats.isEmpty()) {
                // No seats generated yet, show total capacity as available
                showtime.setAvailableSeats(showtime.getTotalSeats());
            } else {
                // Seats exist but all are booked, show 0
                showtime.setAvailableSeats(0);
            }
        } else {
            // Use the count from repository (this is already available seats count)
            showtime.setAvailableSeats(availableSeatsFromRepo);
        }
    }
}
