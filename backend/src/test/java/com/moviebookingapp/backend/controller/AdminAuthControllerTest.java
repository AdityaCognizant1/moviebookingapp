package com.moviebookingapp.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviebookingapp.backend.entity.User;
import com.moviebookingapp.backend.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private UserService userService;
    
    @AfterEach
    void cleanupTestUsers() {
        // Clean up test users after each test
        try {
            // Delete any users with "testadmin_" prefix
            userService.findAll().stream()
                .filter(user -> user.getUsername().startsWith("testadmin_"))
                .forEach(user -> userService.deleteUserByIdInternal(user.getId()));
                
        } catch (Exception e) {
            // Ignore cleanup errors
            System.out.println("Cleanup warning: " + e.getMessage());
        }
    }

    @Test
    void adminLogin_withValidAdminCredentials_shouldReturnToken() throws Exception {
        // Create a test admin user
        String adminUsername = "testadmin_" + System.currentTimeMillis();
        String adminEmail = "testadmin_" + System.currentTimeMillis() + "@example.com";
        
        User adminUser = User.builder()
                .username(adminUsername)
                .password("adminpassword")
                .email(adminEmail)
                .role("ADMIN")
                .build();
        
        userService.save(adminUser);
        
        // Test admin login
        Map<String, String> loginRequest = Map.of(
            "username", adminUsername,
            "password", "adminpassword"
        );
        
        mockMvc.perform(post("/api/admin/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value(adminUsername))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.message").value("Admin login successful"));
    }

    @Test
    void adminLogin_withRegularUserCredentials_shouldReturnForbidden() throws Exception {
        // Create a test regular user
        String username = "testadmin_" + System.currentTimeMillis();
        String email = "testadmin_" + System.currentTimeMillis() + "@example.com";
        
        User regularUser = User.builder()
                .username(username)
                .password("userpassword")
                .email(email)
                .role("USER")  // Regular user, not admin
                .build();
        
        userService.save(regularUser);
        
        // Test admin login with regular user credentials
        Map<String, String> loginRequest = Map.of(
            "username", username,
            "password", "userpassword"
        );
        
        mockMvc.perform(post("/api/admin/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Access denied: Admin privileges required"));
    }

    @Test
    void adminLogin_withInvalidCredentials_shouldReturnUnauthorized() throws Exception {
        Map<String, String> loginRequest = Map.of(
            "username", "nonexistentadmin",
            "password", "wrongpassword"
        );
        
        mockMvc.perform(post("/api/admin/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid admin credentials"));
    }

    @Test
    void createAdminUser_withValidSecretKey_shouldCreateAdmin() throws Exception {
        String adminUsername = "testadmin_" + System.currentTimeMillis();
        String adminEmail = "testadmin_" + System.currentTimeMillis() + "@example.com";
        
        Map<String, String> createRequest = Map.of(
            "username", adminUsername,
            "password", "newadminpassword",
            "email", adminEmail,
            "adminKey", "ADMIN_CREATION_SECRET_2024"
        );
        
        mockMvc.perform(post("/api/admin/auth/create-admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Admin user created successfully"))
                .andExpect(jsonPath("$.username").value(adminUsername))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void createAdminUser_withInvalidSecretKey_shouldReturnForbidden() throws Exception {
        String adminUsername = "testadmin_" + System.currentTimeMillis();
        String adminEmail = "testadmin_" + System.currentTimeMillis() + "@example.com";
        
        Map<String, String> createRequest = Map.of(
            "username", adminUsername,
            "password", "newadminpassword",
            "email", adminEmail,
            "adminKey", "WRONG_SECRET_KEY"
        );
        
        mockMvc.perform(post("/api/admin/auth/create-admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Invalid admin creation key"));
    }

    @Test
    void createAdminUser_withExistingUsername_shouldReturnBadRequest() throws Exception {
        // Create an existing user first
        String existingUsername = "testadmin_" + System.currentTimeMillis();
        String existingEmail = "testadmin_" + System.currentTimeMillis() + "@example.com";
        
        User existingUser = User.builder()
                .username(existingUsername)
                .password("existingpassword")
                .email(existingEmail)
                .role("USER")
                .build();
        
        userService.save(existingUser);
        
        // Try to create admin with same username
        Map<String, String> createRequest = Map.of(
            "username", existingUsername,  // Same username
            "password", "newadminpassword",
            "email", "different@example.com",
            "adminKey", "ADMIN_CREATION_SECRET_2024"
        );
        
        mockMvc.perform(post("/api/admin/auth/create-admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Username already exists"));
    }
}
