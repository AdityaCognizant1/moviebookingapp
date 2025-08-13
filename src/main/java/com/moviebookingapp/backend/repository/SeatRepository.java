package com.moviebookingapp.backend.repository;

import com.moviebookingapp.backend.entity.Seat;
import com.moviebookingapp.backend.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    
    List<Seat> findByShowtimeOrderBySeatNumber(Showtime showtime);
    
    List<Seat> findByShowtimeAndIsBookedFalseOrderBySeatNumber(Showtime showtime);
    
    List<Seat> findByShowtimeAndIsBookedTrueOrderBySeatNumber(Showtime showtime);
    
    @Query("SELECT COUNT(s) FROM Seat s WHERE s.showtime = :showtime AND s.isBooked = false")
    Integer countAvailableSeats(@Param("showtime") Showtime showtime);
    
    List<Seat> findBySeatNumberInAndShowtime(List<String> seatNumbers, Showtime showtime);
}
