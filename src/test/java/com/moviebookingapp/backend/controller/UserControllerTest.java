package com.moviebookingapp.backend.controller;

import com.moviebookingapp.backend.entity.User;
import com.moviebookingapp.backend.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;
    
    @AfterEach
    void cleanupTestUsers() {
        // Clean up test users after each test (using admin deletion for test cleanup)
        try {
            // Delete testuser if it exists
            Optional<User> testUser = userService.findByUsername("testuser");
            if (testUser.isPresent()) {
                userService.deleteUserByIdInternal(testUser.get().getId());
            }
            
            // Delete any users with "admintest_" prefix
            userService.findAll().stream()
                .filter(user -> user.getUsername().startsWith("admintest_"))
                .forEach(user -> userService.deleteUserByIdInternal(user.getId()));
                
        } catch (Exception e) {
            // Ignore cleanup errors
            System.out.println("Cleanup warning: " + e.getMessage());
        }
    }

    @Test
    @WithMockUser(username = "testuser")
    @DirtiesContext
    void getCurrentUserProfile_shouldReturnUserProfile() throws Exception {
        // Create testuser with unique email to avoid constraint violations
        String uniqueEmail = "testuser_" + System.currentTimeMillis() + "@example.com";
        
        try {
            User user = User.builder()
                    .username("testuser")  // Must match @WithMockUser username
                    .password("testpassword")
                    .email(uniqueEmail)
                    .role("USER")  // Set role explicitly
                    .build();
            
            userService.save(user);
        } catch (Exception e) {
            // User might already exist, that's okay for this test
        }

        // Test the new profile endpoint
        mockMvc.perform(get("/api/users/profile"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.password").doesNotExist()); // Password should not be exposed
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getByUsername_asAdmin_shouldReturnUser() throws Exception {
        // Create a user
        String uniqueUsername = "admintest_" + System.currentTimeMillis();
        String uniqueEmail = "admintest_" + System.currentTimeMillis() + "@example.com";
        
        User user = User.builder()
                .username(uniqueUsername)
                .password("testpassword")
                .email(uniqueEmail)
                .role("USER")  // Set role explicitly
                .build();
        
        userService.save(user);

        // Admin should be able to access user by username
        mockMvc.perform(get("/api/users/by-username/" + uniqueUsername))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(uniqueUsername))
                .andExpect(jsonPath("$.email").value(uniqueEmail))
                .andExpect(jsonPath("$.password").doesNotExist()); // Password should not be exposed
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getByUsername_whenUserNotExists_shouldReturnNotFound() throws Exception {
        // Test with non-existent user
        mockMvc.perform(get("/api/users/by-username/nonexistentuser"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_shouldReturnAllUsers() throws Exception {
        // Test getting all users
        mockMvc.perform(get("/api/users/admin/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}

