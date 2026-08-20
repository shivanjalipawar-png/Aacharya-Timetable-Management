package com.aacharya.timetablemanagement.controller;

import com.aacharya.timetablemanagement.dto.LoginRequestDTO;
import com.aacharya.timetablemanagement.dto.UserResponseDTO;
import com.aacharya.timetablemanagement.entity.User;
import com.aacharya.timetablemanagement.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // Constructor Injection → Inject AuthService
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Register User → Create new user
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(
            @RequestBody User user) {

        User registeredUser =
                authService.registerUser(user);

        UserResponseDTO response =
                new UserResponseDTO(
                        registeredUser.getUserId(),
                        registeredUser.getUsername(),
                        registeredUser.getRole()
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(
            @RequestBody LoginRequestDTO loginRequest) {

        // Login → Authenticate user and generate JWT
        String token = authService.loginUser(loginRequest);

        return new ResponseEntity<>(
                token,
                HttpStatus.OK
        );
    }
}