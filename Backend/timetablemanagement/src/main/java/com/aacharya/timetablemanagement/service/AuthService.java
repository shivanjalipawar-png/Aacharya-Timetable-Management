
package com.aacharya.timetablemanagement.service;

import com.aacharya.timetablemanagement.dto.LoginRequestDTO;
import com.aacharya.timetablemanagement.dto.UserRequestDTO;
import com.aacharya.timetablemanagement.dto.UserResponseDTO;
import com.aacharya.timetablemanagement.entity.User;
import com.aacharya.timetablemanagement.entity.Role;
import com.aacharya.timetablemanagement.entity.Teacher;
import com.aacharya.timetablemanagement.entity.Batch;
import com.aacharya.timetablemanagement.exception.ConflictException;
import com.aacharya.timetablemanagement.repository.UserRepository;
import com.aacharya.timetablemanagement.repository.TeacherRepository;
import com.aacharya.timetablemanagement.repository.BatchRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final BatchRepository batchRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // Constructor Injection
    public AuthService(
            UserRepository userRepository,
            TeacherRepository teacherRepository,
            BatchRepository batchRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.batchRepository = batchRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // ADMIN-controlled User Creation
    public UserResponseDTO createUser(UserRequestDTO userRequest) {

        // Check whether username already exists
        User existingUser = userRepository
                .findByUsername(userRequest.getUsername())
                .orElse(null);

        if (existingUser != null) {
            throw new ConflictException("Username already exists");
        }

        User user = new User();

        user.setUsername(userRequest.getUsername());

        // Encode password using BCrypt
        user.setPassword(
                passwordEncoder.encode(userRequest.getPassword())
        );

        // Assign the role selected by ADMIN
        Role role = userRequest.getRole();

        user.setRole(role);

        // Role-specific account linking
        switch (role) {

            case ADMIN:

                if (userRequest.getTeacherId() != null ||
                        userRequest.getBatchId() != null) {

                    throw new IllegalArgumentException(
                            "ADMIN account must not have a teacher or batch ID"
                    );
                }

                break;

            case TEACHER:

                if (userRequest.getTeacherId() == null) {
                    throw new IllegalArgumentException(
                            "Teacher ID is required for TEACHER account"
                    );
                }

                if (userRequest.getBatchId() != null) {
                    throw new IllegalArgumentException(
                            "TEACHER account must not have a batch ID"
                    );
                }

                Teacher teacher = teacherRepository
                        .findById(userRequest.getTeacherId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Teacher not found with ID: "
                                        + userRequest.getTeacherId()
                        ));

                user.setTeacher(teacher);

                break;

            case STUDENT:

                if (userRequest.getBatchId() == null) {
                    throw new IllegalArgumentException(
                            "Batch ID is required for STUDENT account"
                    );
                }

                if (userRequest.getTeacherId() != null) {
                    throw new IllegalArgumentException(
                            "STUDENT account must not have a teacher ID"
                    );
                }

                Batch batch = batchRepository
                        .findById(userRequest.getBatchId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Batch not found with ID: "
                                        + userRequest.getBatchId()
                        ));

                user.setBatch(batch);

                break;
        }

        // Save user with the appropriate relationship
        User savedUser = userRepository.save(user);

        // Return safe response DTO
        return new UserResponseDTO(
                savedUser.getUserId(),
                savedUser.getUsername(),
                savedUser.getRole()
        );
    }

    // User Login → Verify username and password
    public String loginUser(LoginRequestDTO loginRequest) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                );

        // Authenticate user
        authenticationManager.authenticate(authenticationToken);

        // Generate JWT
        return jwtService.generateToken(
                loginRequest.getUsername()
        );
    }
}
