package com.moviebookingapp.backend.controller;

import com.moviebookingapp.backend.entity.User;
import com.moviebookingapp.backend.security.JwtUtil;
import com.moviebookingapp.backend.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestBody AdminLoginRequest request) {
        try {
            // Find user by username
            Optional<User> userOpt = userService.findByUsername(request.getUsername());
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid admin credentials"));
            }
            
            User user = userOpt.get();
            
            // Check if user is admin
            if (!"ADMIN".equals(user.getRole())) {
                return ResponseEntity.status(403).body(Map.of("error", "Access denied: Admin privileges required"));
            }
            
            // Verify password
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid admin credentials"));
            }
            
            // Generate JWT token
            String token = jwtUtil.generateToken(user.getUsername());
            
            return ResponseEntity.ok(Map.of(
                "token", token,
                "username", user.getUsername(),
                "role", user.getRole(),
                "message", "Admin login successful"
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Admin login failed: " + e.getMessage()));
        }
    }
    
    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdminUser(@RequestBody CreateAdminRequest request) {
        try {
            // Validate admin creation key (in production, this should be a secure secret)
            if (!"ADMIN_CREATION_SECRET_2024".equals(request.getAdminKey())) {
                return ResponseEntity.status(403).body(Map.of("error", "Invalid admin creation key"));
            }
            
            // Check if username already exists
            if (userService.findByUsername(request.getUsername()).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
            }
            
            // Check if email already exists
            if (userService.findByEmail(request.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
            }
            
            // Create admin user
            User adminUser = userService.createAdminUser(
                request.getUsername(),
                request.getPassword(),
                request.getEmail()
            );
            
            return ResponseEntity.ok(Map.of(
                "message", "Admin user created successfully",
                "username", adminUser.getUsername(),
                "role", adminUser.getRole()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Admin creation failed: " + e.getMessage()));
        }
    }
    
    @Data
    static class AdminLoginRequest {
        private String username;
        private String password;
    }
    
    @Data
    static class CreateAdminRequest {
        private String username;
        private String password;
        private String email;
        private String adminKey;  // Secret key for admin creation
    }
}
