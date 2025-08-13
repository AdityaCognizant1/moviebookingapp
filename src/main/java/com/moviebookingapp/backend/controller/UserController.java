package com.moviebookingapp.backend.controller;

import com.moviebookingapp.backend.entity.User;
import com.moviebookingapp.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    // USER-SCOPED - Get current user's profile
    @GetMapping("/profile")
    public ResponseEntity<?> getCurrentUserProfile() {
        try {
            // Get current user from security context
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            
            Optional<User> userOpt = userService.findByUsername(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not found"));
            }
            
            // Return user profile (password already excluded by @JsonIgnore)
            return ResponseEntity.ok(userOpt.get());
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to load profile: " + e.getMessage()));
        }
    }
    
    // ADMIN ONLY - Get user by username (for admin purposes)
    @GetMapping("/by-username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getByUsername(@PathVariable String username) {
        try {
            Optional<User> user = userService.findByUsername(username);
            if (user.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            // Return user info (password already excluded by @JsonIgnore)
            return ResponseEntity.ok(user.get());
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to load user: " + e.getMessage()));
        }
    }
    
    // ADMIN ONLY - Get all users
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to load users: " + e.getMessage()));
        }
    }
    
    // USER-SCOPED - Delete own account
    @DeleteMapping("/account")
    public ResponseEntity<?> deleteMyAccount() {
        try {
            // Get current user from security context
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            
            // Delete current user's account
            userService.deleteCurrentUserAccount(username);
            
            return ResponseEntity.ok(Map.of(
                "message", "Account deleted successfully",
                "username", username
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to delete account: " + e.getMessage()));
        }
    }
    
    // ADMIN ONLY - Delete any user by ID
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok(Map.of(
                "message", "User deleted successfully",
                "userId", id
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to delete user: " + e.getMessage()));
        }
    }
}
