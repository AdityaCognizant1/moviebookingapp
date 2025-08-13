package com.moviebookingapp.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviebookingapp.backend.entity.Movie;
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
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllMovies_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk());
    }
    
    @Test
    void getMovieById_whenExists_shouldReturnMovie() throws Exception {
        // Assuming movie with ID 1 exists from DataLoader
        mockMvc.perform(get("/api/movies/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }
    
    @Test
    void getMovieById_whenNotExists_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/movies/999"))
                .andExpect(status().isNotFound());
    }
    
    // ADMIN FUNCTION TESTS
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void createMovie_asAdmin_shouldReturnOk() throws Exception {
        Movie newMovie = Movie.builder()
                .title("Test Movie")
                .description("A test movie for admin creation")
                .durationMinutes(120)
                .build();
        
        mockMvc.perform(post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newMovie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Movie"))
                .andExpect(jsonPath("$.description").value("A test movie for admin creation"))
                .andExpect(jsonPath("$.durationMinutes").value(120));
    }
    
    @Test
    void createMovie_withoutAuth_shouldReturnUnauthorized() throws Exception {
        Movie newMovie = Movie.builder()
                .title("Unauthorized Movie")
                .description("Should not be created")
                .durationMinutes(90)
                .build();
        
        mockMvc.perform(post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newMovie)))
                .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(roles = "USER")
    void createMovie_asUser_shouldReturnForbidden() throws Exception {
        Movie newMovie = Movie.builder()
                .title("User Movie")
                .description("Should not be created by user")
                .durationMinutes(90)
                .build();
        
        mockMvc.perform(post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newMovie)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateMovie_asAdmin_shouldReturnOk() throws Exception {
        Movie updatedMovie = Movie.builder()
                .title("Updated Movie Title")
                .description("Updated description")
                .durationMinutes(150)
                .build();
        
        mockMvc.perform(put("/api/movies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedMovie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Movie Title"));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteMovie_asAdmin_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/movies/1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void deleteMovie_withoutAuth_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/movies/1"))
                .andExpect(status().isForbidden());
    }
}
