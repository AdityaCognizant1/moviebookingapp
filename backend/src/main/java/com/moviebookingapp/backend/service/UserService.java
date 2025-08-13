package com.moviebookingapp.backend.service;

import com.moviebookingapp.backend.entity.User;
import com.moviebookingapp.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    // ADMIN ONLY - Delete any user by ID (for admin user management)
    // Security enforced at controller level
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
    
    // USER-SCOPED - Delete current user's own account
    public void deleteCurrentUserAccount(String currentUsername) {
        Optional<User> user = findByUsername(currentUsername);
        if (user.isPresent()) {
            userRepository.deleteById(user.get().getId());
        } else {
            throw new RuntimeException("User not found: " + currentUsername);
        }
    }
    
    // INTERNAL - For test cleanup only (bypasses security)
    public void deleteUserByIdInternal(Long id) {
        userRepository.deleteById(id);
    }
    
    // Create admin user
    public User createAdminUser(String username, String password, String email) {
        User adminUser = User.builder()
                .username(username)
                .password(password)  // Will be encoded by save method
                .email(email)
                .role("ADMIN")
                .build();
        return save(adminUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole())  // Role is guaranteed to be non-null
                .build();
    }
}
