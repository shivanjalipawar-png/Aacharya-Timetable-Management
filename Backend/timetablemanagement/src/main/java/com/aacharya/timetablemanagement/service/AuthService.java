package com.aacharya.timetablemanagement.service;


import com.aacharya.timetablemanagement.entity.User;
import com.aacharya.timetablemanagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor Injection → Inject required dependencies
    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    // User Registration -> Handles registration logic
    public User registerUser(User user) {

        // Check User → Find username in database
        User existingUser = userRepository
                .findByUsername(user.getUsername())
                .orElse(null);


        // Duplicate Check → Prevent duplicate username
        if (existingUser != null) {
            throw new RuntimeException(
                    "Username already exists"
            );
        }

        // Encode Password → Secure password using BCrypt
        String encodedPassword =
                passwordEncoder.encode(user.getPassword());

        // Set Password → Replace plain password with hash
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }



}
