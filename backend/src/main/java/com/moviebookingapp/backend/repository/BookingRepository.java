package com.moviebookingapp.backend.repository;

import com.moviebookingapp.backend.entity.Booking;
import com.moviebookingapp.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByUserOrderByBookingTimeDesc(User user);
}
