
package com.aacharya.timetablemanagement.controller;

import com.aacharya.timetablemanagement.dto.UserRequestDTO;
import com.aacharya.timetablemanagement.dto.UserResponseDTO;
import com.aacharya.timetablemanagement.entity.Role;
import com.aacharya.timetablemanagement.repository.UserRepository;
import com.aacharya.timetablemanagement.service.AuthService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final AuthService authService;

    public UserController(
            UserRepository userRepository,
            AuthService authService) {

        this.userRepository = userRepository;
        this.authService = authService;
    }

    // Get User By ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {

        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(
                        new UserResponseDTO(
                                user.getUserId(),
                                user.getUsername(),
                                user.getRole()
                        )
                ))
                .orElse(ResponseEntity.notFound().build());
    }

    // ADMIN creates a new user
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserRequestDTO userRequest) {

        UserResponseDTO response =
                authService.createUser(userRequest);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    // Get total number of students
    @GetMapping("/students/count")
    public Long getStudentCount() {

        return userRepository.countByRole(Role.STUDENT);
    }
}
