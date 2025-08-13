package com.moviebookingapp.backend.service;

import com.moviebookingapp.backend.entity.Seat;
import com.moviebookingapp.backend.entity.Showtime;
import com.moviebookingapp.backend.entity.Booking;
import com.moviebookingapp.backend.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeatService {
    
    @Autowired
    private SeatRepository seatRepository;

    public List<Seat> findAll() {
        return seatRepository.findAll();
    }

    public Optional<Seat> findById(Long id) {
        return seatRepository.findById(id);
    }

    public Seat save(Seat seat) {
        return seatRepository.save(seat);
    }
    
    public List<Seat> findByShowtimeId(Long showtimeId) {
        // First get the showtime, then find seats
        // This method will be used by the controller
        return seatRepository.findAll().stream()
            .filter(seat -> seat.getShowtime().getId().equals(showtimeId))
            .collect(java.util.stream.Collectors.toList());
    }
    
    public void deleteById(Long id) {
        seatRepository.deleteById(id);
    }

    public List<Seat> findByShowtime(Showtime showtime) {
        return seatRepository.findByShowtimeOrderBySeatNumber(showtime);
    }

    public List<Seat> findAvailableSeats(Showtime showtime) {
        return seatRepository.findByShowtimeAndIsBookedFalseOrderBySeatNumber(showtime);
    }

    public List<Seat> findBookedSeats(Showtime showtime) {
        return seatRepository.findByShowtimeAndIsBookedTrueOrderBySeatNumber(showtime);
    }

    public Integer countAvailableSeats(Showtime showtime) {
        return seatRepository.countAvailableSeats(showtime);
    }

    public List<Seat> findSeatsByNumbers(List<String> seatNumbers, Showtime showtime) {
        return seatRepository.findBySeatNumberInAndShowtime(seatNumbers, showtime);
    }

    public boolean areSeatsAvailable(List<String> seatNumbers, Showtime showtime) {
        List<Seat> seats = findSeatsByNumbers(seatNumbers, showtime);
        return seats.size() == seatNumbers.size() && 
               seats.stream().allMatch(seat -> !seat.getIsBooked());
    }

    public void bookSeats(List<String> seatNumbers, Showtime showtime, Booking booking) {
        List<Seat> seats = findSeatsByNumbers(seatNumbers, showtime);
        for (Seat seat : seats) {
            seat.setIsBooked(true);
            seat.setBooking(booking);
            save(seat);
        }
    }

    public void generateSeatsForShowtime(Showtime showtime) {
        // Generate seats for a standard cinema layout
        // 12 rows (A-L) × 12 seats per row = 144 total seats
        for (char row = 'A'; row <= 'L'; row++) {
            for (int seatNum = 1; seatNum <= 12; seatNum++) {
                String seatNumber = row + String.valueOf(seatNum);
                Seat seat = Seat.builder()
                        .showtime(showtime)
                        .seatNumber(seatNumber)
                        .isBooked(false)
                        .build();
                save(seat);
            }
        }
    }
}
