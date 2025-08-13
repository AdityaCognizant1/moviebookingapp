package com.moviebookingapp.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviebookingapp.backend.entity.User;
import com.moviebookingapp.backend.dto.UserRegistrationRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private com.moviebookingapp.backend.service.UserService userService;
    
    @AfterEach
    void cleanupTestUsers() {
        // Clean up test users after each test (using admin deletion for test cleanup)
        try {
            // Delete any users with "testuser_" prefix (from registration tests)
            userService.findAll().stream()
                .filter(user -> user.getUsername().startsWith("testuser_"))
                .forEach(user -> userService.deleteUserByIdInternal(user.getId()));
                
        } catch (Exception e) {
            // Ignore cleanup errors
            System.out.println("Cleanup warning: " + e.getMessage());
        }
    }

    @Test
    void registerUser_success() throws Exception {
        // Use unique username and email to avoid conflicts
        String uniqueUsername = "testuser_" + System.currentTimeMillis();
        String uniqueEmail = "testuser_" + System.currentTimeMillis() + "@example.com";
        
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername(uniqueUsername);
        request.setPassword("testpassword");
        request.setEmail(uniqueEmail);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(uniqueUsername));
    }

    @Test
    void registerUser_duplicateUsername() throws Exception {
        User user = User.builder()
                .username("testuser")
                .password("testpassword")
                .email("testuser2@example.com")
                .build();

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginUser_invalidCredentials() throws Exception {
        String loginJson = "{ \"username\": \"invalid\", \"password\": \"wrong\" }";
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isUnauthorized());
    }
}
