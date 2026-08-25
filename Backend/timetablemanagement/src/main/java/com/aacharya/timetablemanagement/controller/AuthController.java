package com.aacharya.timetablemanagement.controller;

import com.aacharya.timetablemanagement.dto.LoginRequestDTO;
import com.aacharya.timetablemanagement.dto.UserRequestDTO;
import com.aacharya.timetablemanagement.dto.UserResponseDTO;
import com.aacharya.timetablemanagement.service.AuthService;
import jakarta.validation.Valid;
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
         @Valid @RequestBody UserRequestDTO userRequest) {

        UserResponseDTO response =
                authService.registerUser(userRequest);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(
         @Valid   @RequestBody LoginRequestDTO loginRequest) {

        // Login → Authenticate user and generate JWT
        String token = authService.loginUser(loginRequest);

        return new ResponseEntity<>(
                token,
                HttpStatus.OK
        );
    }
}