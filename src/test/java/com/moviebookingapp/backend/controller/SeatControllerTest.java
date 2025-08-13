package com.moviebookingapp.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getSeatsByShowtime_shouldReturnSeatsForShowtime() throws Exception {
        // Test getting seats for a showtime
        mockMvc.perform(get("/api/seats/showtime/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.showtime").exists())
                .andExpect(jsonPath("$.seats").exists())
                .andExpect(jsonPath("$.availableCount").exists());
    }

    @Test
    void getSeatsByShowtime_whenShowtimeNotExists_shouldReturnBadRequest() throws Exception {
        // Test with non-existent showtime
        mockMvc.perform(get("/api/seats/showtime/999"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void getAvailableSeats_shouldReturnAvailableSeatsOnly() throws Exception {
        // Test getting only available seats
        mockMvc.perform(get("/api/seats/available/showtime/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAvailableSeats_whenShowtimeNotExists_shouldReturnBadRequest() throws Exception {
        // Test with non-existent showtime
        mockMvc.perform(get("/api/seats/available/showtime/999"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").exists());
    }
}
