package com.moviebookingapp.backend.service;

import com.moviebookingapp.backend.entity.Theater;
import com.moviebookingapp.backend.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TheaterService {
    
    @Autowired
    private TheaterRepository theaterRepository;

    public List<Theater> findAll() {
        return theaterRepository.findAll();
    }

    public List<Theater> findActiveTheaters() {
        return theaterRepository.findActiveTheatersOrderByName();
    }

    public Optional<Theater> findById(Long id) {
        return theaterRepository.findById(id);
    }

    public Theater save(Theater theater) {
        return theaterRepository.save(theater);
    }
    
    public void deleteById(Long id) {
        theaterRepository.deleteById(id);
    }

    public List<Theater> searchByName(String name) {
        return theaterRepository.findByNameContainingIgnoreCase(name);
    }

    public Long countActiveTheaters() {
        return theaterRepository.countActiveTheaters();
    }

    // Helper method to create default theaters
    public void createDefaultTheaters() {
        if (theaterRepository.count() == 0) {
            // Create default theaters
            Theater theater1 = Theater.builder()
                    .name("Theater 1")
                    .description("Standard Theater with Digital Sound")
                    .totalSeats(144)
                    .isActive(true)
                    .build();

            Theater theater2 = Theater.builder()
                    .name("Theater 2")
                    .description("Premium Theater with Dolby Atmos")
                    .totalSeats(144)
                    .isActive(true)
                    .build();

            Theater imaxTheater = Theater.builder()
                    .name("IMAX Screen")
                    .description("IMAX Experience with 4K Projection")
                    .totalSeats(180) // Larger IMAX theater
                    .isActive(true)
                    .build();

            Theater vipTheater = Theater.builder()
                    .name("VIP Hall")
                    .description("Luxury Recliner Seats with Premium Service")
                    .totalSeats(96) // Smaller VIP theater
                    .isActive(true)
                    .build();

            save(theater1);
            save(theater2);
            save(imaxTheater);
            save(vipTheater);
        }
    }
}
