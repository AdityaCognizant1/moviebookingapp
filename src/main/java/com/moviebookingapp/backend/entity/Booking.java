package com.moviebookingapp.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"bookings", "password"})
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "showtime_id", nullable = false)
    @JsonIgnoreProperties({"bookings"})
    private Showtime showtime;

    @Column(nullable = false)
    private Integer numTickets;
    
    @Column(name = "seat_numbers", length = 500)
    private String seatNumbers; // Comma-separated seat numbers like "A1,A2,B5"

    @Column(nullable = false)
    private LocalDateTime bookingTime;
}
