package com.moviebookingapp.backend.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ShowtimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllShowtimes_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/showtimes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getShowtimeById_whenExists_shouldReturnShowtime() throws Exception {
        // This test assumes that showtimes exist from the DataLoader
        mockMvc.perform(get("/api/showtimes/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getShowtimeById_whenNotExists_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/showtimes/999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void getShowtimesByMovie_shouldReturnShowtimesForMovie() throws Exception {
        // Test the new endpoint for getting showtimes by movie ID
        mockMvc.perform(get("/api/showtimes/movie/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.movieId").value(1))
                .andExpect(jsonPath("$.showtimes").exists());
    }
    
    @Test
    void getShowtimesByMovie_whenNoShowtimes_shouldReturnEmptyList() throws Exception {
        // Test with a movie ID that has no showtimes
        mockMvc.perform(get("/api/showtimes/movie/999"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.movieId").value(999))
                .andExpect(jsonPath("$.showtimes").isEmpty())
                .andExpect(jsonPath("$.message").exists());
    }
    
    // ADMIN FUNCTION TESTS
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void createShowtime_asAdmin_shouldReturnOk() throws Exception {
        // Create a test showtime with movie and theater ID reference
        String showtimeJson = "{"
                + "\"movieId\": 1,"
                + "\"theaterId\": 1,"
                + "\"showTime\": \"2024-12-25T15:30:00\""
                + "}";
        
        mockMvc.perform(post("/api/showtimes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(showtimeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.theater.totalSeats").value(144));
    }
    
    @Test
    void createShowtime_withoutAuth_shouldReturnUnauthorized() throws Exception {
        String showtimeJson = "{"
                + "\"movieId\": 1,"
                + "\"theaterId\": 1,"
                + "\"showTime\": \"2024-12-25T15:30:00\""
                + "}";
        
        mockMvc.perform(post("/api/showtimes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(showtimeJson))
                .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(roles = "USER")
    void createShowtime_asUser_shouldReturnForbidden() throws Exception {
        String showtimeJson = "{"
                + "\"movieId\": 1,"
                + "\"theaterId\": 1,"
                + "\"showTime\": \"2024-12-25T15:30:00\""
                + "}";
        
        mockMvc.perform(post("/api/showtimes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(showtimeJson))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateShowtime_asAdmin_shouldReturnOk() throws Exception {
        String showtimeJson = "{"
                + "\"movieId\": 1,"
                + "\"theaterId\": 1,"
                + "\"showTime\": \"2024-12-26T18:00:00\""
                + "}";
        
        mockMvc.perform(put("/api/showtimes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(showtimeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.theater.totalSeats").value(144));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteShowtime_asAdmin_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/showtimes/1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void deleteShowtime_withoutAuth_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/showtimes/1"))
                .andExpect(status().isForbidden());
    }
}
