package com.moviebookingapp.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @JsonIgnore  // NEVER expose password in JSON responses - SECURITY CRITICAL
    private String password;

    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    @Builder.Default
    private String role = "USER";  // Default role is USER, can be ADMIN
}
