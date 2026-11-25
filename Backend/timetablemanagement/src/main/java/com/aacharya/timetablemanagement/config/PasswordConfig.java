
package com.aacharya.timetablemanagement.config;

import com.aacharya.timetablemanagement.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class PasswordConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public PasswordConfig(
            CustomUserDetailsService userDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // CORS Configuration
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        // React Vite frontend
        configuration.setAllowedOrigins(
                List.of("http://localhost:5173")
        );

        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );

        configuration.setAllowedHeaders(
                List.of("Authorization", "Content-Type", "Accept")
        );

        // JWT uses Authorization header, not cookies
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    // BCrypt Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authentication Provider
    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    // Authentication Manager
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    // Spring Security Filter Chain
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http
                .cors(cors ->
                        cors.configurationSource(corsConfigurationSource())
                )

                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // Handle 401 and 403 responses
                .exceptionHandling(exception -> exception

                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    "Unauthorized"
                            );
                        })

                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendError(
                                    HttpServletResponse.SC_FORBIDDEN,
                                    "Forbidden"
                            );
                        })
                )

                // ROLE-BASED AUTHORIZATION
                .authorizeHttpRequests(auth -> auth

                        // PUBLIC APIs
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/login"
                        ).permitAll()

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/error"
                        ).permitAll()


                        // USER MANAGEMENT

                        // ADMIN only: Create user
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/users"
                        ).hasRole("ADMIN")

                        // ADMIN only: View user profiles
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/users/**"
                        ).hasRole("ADMIN")


                        // BATCH MANAGEMENT

                        // ADMIN only: Create, update and delete
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/batches/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/batches/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/batches/**"
                        ).hasRole("ADMIN")


                        // SUBJECT MANAGEMENT

                        // ADMIN only: Create, update and delete
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/subjects/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/subjects/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/subjects/**"
                        ).hasRole("ADMIN")


                        // TEACHER MANAGEMENT

                        // ADMIN only: Create, update and delete
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/teachers/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/teachers/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/teachers/**"
                        ).hasRole("ADMIN")


                        // TIMETABLE MANAGEMENT

                        // ADMIN only: Create, update and delete
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/timetables/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/timetables/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/timetables/**"
                        ).hasRole("ADMIN")

                                // STUDENT MANAGEMENT

                              // ADMIN only: Create student
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/students",
                                        "/api/students/**"
                                ).hasRole("ADMIN")

                      // ADMIN only: View student records
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/students",
                                        "/api/students/**"
                                ).hasRole("ADMIN")

                        // ADMIN only: Update student
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/students/**"
                        ).hasRole("ADMIN")


                        // ADMIN only: Delete student
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/students/**"
                        ).hasRole("ADMIN")

                        // READ PERMISSIONS

                        // ADMIN, TEACHER and STUDENT can read
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/batches/**",
                                "/api/subjects/**",
                                "/api/teachers/**",
                                "/api/timetables/**"
                        ).hasAnyRole("ADMIN", "TEACHER", "STUDENT")


                        // Any other endpoint requires authentication
                        .anyRequest().authenticated()
                )

                // JWT Authentication Filter
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
