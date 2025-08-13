package com.moviebookingapp.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "theaters")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name; // e.g., "Theater 1", "IMAX Screen", "VIP Hall"

    @Column(nullable = false)
    @Builder.Default
    private Integer totalSeats = 144; // Total seats in this theater (12 rows x 12 seats)

    @Column(length = 100)
    private String description; // e.g., "Standard Theater", "IMAX Experience", "Luxury Recliner Seats"

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true; // Theater operational status

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"theater"})
    private List<Showtime> showtimes;

    // Helper method to get theater capacity
    public String getCapacityInfo() {
        return totalSeats + " seats";
    }

    // Helper method for display
    public String getDisplayName() {
        return name + " (" + getCapacityInfo() + ")";
    }
}
