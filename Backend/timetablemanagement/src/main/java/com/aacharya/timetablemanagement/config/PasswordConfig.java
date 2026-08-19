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

@Configuration
@EnableWebSecurity
public class PasswordConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public PasswordConfig(CustomUserDetailsService userDetailsService,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter =jwtAuthenticationFilter;
    }

    // Password Bean → Creates BCrypt encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authentication Provider → Verifies user credentials
    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    // Authentication Manager → Manages authentication
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    // Security Filter → Controls API access
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        ))

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

                .authorizeHttpRequests(auth ->
                        auth


                                // ================= PUBLIC APIs =================

                                .requestMatchers(
                                        "/api/auth/**",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/error"
                                )
                                .permitAll()


                                // ================= BATCH =================

                                 // ADMIN only → Create
                                .requestMatchers(HttpMethod.POST, "/batches")
                                .hasRole("ADMIN")

                               // ADMIN only → Update
                                .requestMatchers(HttpMethod.PUT, "/batches/**")
                                .hasRole("ADMIN")

                                // ADMIN only → Delete
                                .requestMatchers(HttpMethod.DELETE, "/batches/**")
                                .hasRole("ADMIN")

                                   // ADMIN / TEACHER / STUDENT → Read
                                .requestMatchers(HttpMethod.GET, "/batches/**")
                                .hasAnyRole("ADMIN", "TEACHER", "STUDENT")


                                   // ================= SUBJECT =================

                                  // ADMIN only → Create
                                .requestMatchers(HttpMethod.POST, "/subjects")
                                .hasRole("ADMIN")

                               // ADMIN only → Update
                                .requestMatchers(HttpMethod.PUT, "/subjects/**")
                                .hasRole("ADMIN")

                               // ADMIN only → Delete
                                .requestMatchers(HttpMethod.DELETE, "/subjects/**")
                                .hasRole("ADMIN")

                              // ADMIN / TEACHER / STUDENT → Read
                                .requestMatchers(HttpMethod.GET, "/subjects/**")
                                .hasAnyRole("ADMIN", "TEACHER", "STUDENT")


                              // ================= TEACHER =================

                               // ADMIN only → Create
                                .requestMatchers(HttpMethod.POST, "/teachers")
                                .hasRole("ADMIN")

                                // ADMIN only → Update
                                .requestMatchers(HttpMethod.PUT, "/teachers/**")
                                .hasRole("ADMIN")

                               // ADMIN only → Delete
                                .requestMatchers(HttpMethod.DELETE, "/teachers/**")
                                .hasRole("ADMIN")

                               // ADMIN / TEACHER / STUDENT → Read
                                .requestMatchers(HttpMethod.GET, "/teachers/**")
                                .hasAnyRole("ADMIN", "TEACHER", "STUDENT")


                           // ================= TIMETABLE =================
                                // ADMIN only → Create
                                .requestMatchers(HttpMethod.POST, "/api/timetables")
                                .hasRole("ADMIN")

                                 // ADMIN only → Update
                                .requestMatchers(HttpMethod.PUT, "/api/timetables/**")
                                .hasRole("ADMIN")

                                // ADMIN only → Delete
                                .requestMatchers(HttpMethod.DELETE, "/api/timetables/**")
                                .hasRole("ADMIN")

                                // ADMIN / TEACHER / STUDENT → Read
                                .requestMatchers(HttpMethod.GET, "/api/timetables/**")
                                .hasAnyRole("ADMIN", "TEACHER", "STUDENT")

                                .anyRequest()
                                .authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}