package com.moviebookingapp.backend.service;

import com.moviebookingapp.backend.entity.Booking;
import com.moviebookingapp.backend.entity.User;
import com.moviebookingapp.backend.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }
    
    public List<Booking> findByUser(User user) {
        return bookingRepository.findByUserOrderByBookingTimeDesc(user);
    }
    
    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }
}
