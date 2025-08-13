package com.moviebookingapp.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ShowtimeDTO {
    private Long id;
    private Long movieId;
    private LocalDateTime showTime;
    private Integer availableSeats;
}
