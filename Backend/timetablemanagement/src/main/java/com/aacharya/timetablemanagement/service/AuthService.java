package com.aacharya.timetablemanagement.service;


import com.aacharya.timetablemanagement.dto.LoginRequestDTO;
import com.aacharya.timetablemanagement.entity.User;
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

        String token = jwtService.generateToken(loginRequest.getUsername());

        return token;
    }

}
