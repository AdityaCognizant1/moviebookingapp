package com.moviebookingapp.backend.dto;

import lombok.Data;

@Data
public class MovieDTO {
    private Long id;
    private String title;
    private String description;
    private Integer durationMinutes;
}
