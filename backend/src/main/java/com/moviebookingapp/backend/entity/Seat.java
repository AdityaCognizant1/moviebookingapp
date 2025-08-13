package com.moviebookingapp.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "showtime_id", nullable = false)
    @JsonIgnoreProperties({"seats"})
    private Showtime showtime;

    @Column(nullable = false, length = 10)
    private String seatNumber; // e.g., "A1", "B5", "C12"

    @Column(nullable = false)
    @Builder.Default
    private Boolean isBooked = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id")
    @JsonIgnoreProperties({"seats", "user", "showtime"})
    private Booking booking;

    // Helper methods
    public String getRowLetter() {
        return seatNumber.replaceAll("\\d", "");
    }

    public Integer getSeatNumberInRow() {
        return Integer.parseInt(seatNumber.replaceAll("\\D", ""));
    }
}
