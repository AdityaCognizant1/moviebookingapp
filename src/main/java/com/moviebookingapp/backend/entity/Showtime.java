package com.moviebookingapp.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "showtimes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Showtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_id", nullable = false)
    @JsonIgnoreProperties({"showtimes"})
    private Movie movie;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "theater_id", nullable = false)
    @JsonIgnoreProperties({"showtimes"})
    private Theater theater;

    @Column(nullable = false)
    private LocalDateTime showTime;

    @Transient
    private Integer availableSeats; // This will be calculated dynamically
    
    // Get total seats from theater
    public Integer getTotalSeats() {
        return theater != null ? theater.getTotalSeats() : 144;
    }
    
    // Calculate available seats dynamically
    public Integer getAvailableSeats() {
        if (availableSeats != null) {
            return availableSeats;
        }
        return getTotalSeats(); // Default to total seats if not calculated
    }
    
    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }
}
