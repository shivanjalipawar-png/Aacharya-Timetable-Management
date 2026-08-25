package com.aacharya.timetablemanagement.service;


import com.aacharya.timetablemanagement.dto.LoginRequestDTO;
import com.aacharya.timetablemanagement.dto.UserRequestDTO;
import com.aacharya.timetablemanagement.dto.UserResponseDTO;
import com.aacharya.timetablemanagement.entity.Role;
import com.aacharya.timetablemanagement.entity.User;
import com.aacharya.timetablemanagement.exception.ConflictException;
import com.aacharya.timetablemanagement.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // Constructor Injection → Inject required dependencies
    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;

    }


    // User Registration -> Handles registration logic
    public UserResponseDTO registerUser(UserRequestDTO userRequest) {


        User existingUser = userRepository
                .findByUsername(userRequest.getUsername())
                .orElse(null);

        if (existingUser != null) {
            throw new ConflictException(
                    "Username already exists"
            );
        }


        User user = new User();

        user.setUsername(userRequest.getUsername());

        // Encode password using BCrypt
        user.setPassword(
                passwordEncoder.encode(userRequest.getPassword())
        );

        // Registration through public API creates STUDENT
        user.setRole(Role.STUDENT);


        User savedUser = userRepository.save(user);

        // Return safe response DTO
        return new UserResponseDTO(
                savedUser.getUserId(),
                savedUser.getUsername(),
                savedUser.getRole()
        );
    }

    // User Login → Verify username and password
    public String loginUser(LoginRequestDTO loginRequest){

        // Authentication Token → Store login credentials
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                );

        // Authenticate User → Verify credentials
        authenticationManager.authenticate(authenticationToken);

        return jwtService.generateToken(
                loginRequest.getUsername()
        );
    }

}
