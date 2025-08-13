package com.moviebookingapp.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    private Integer durationMinutes;
    
    private String genre;
    
    private String rating;
    
    @Column(length = 500)
    private String posterUrl;
}
