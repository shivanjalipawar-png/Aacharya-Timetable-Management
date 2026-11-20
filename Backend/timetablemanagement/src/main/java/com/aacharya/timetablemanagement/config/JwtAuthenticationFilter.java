
package com.aacharya.timetablemanagement.config;

import com.aacharya.timetablemanagement.service.CustomUserDetailsService;
import com.aacharya.timetablemanagement.service.JwtService;

import io.jsonwebtoken.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Get Authorization header
        String authHeader = request.getHeader("Authorization");

        // 2. Check whether Bearer token exists
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extract JWT
        String jwt = authHeader.substring(7);

        if (jwt.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            // 4. Check whether authentication already exists
            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                // 5. Extract username
                String username = jwtService.extractUsername(jwt);

                if (username != null) {

                    // 6. Load user from database
                    UserDetails userDetails =
                            userDetailsService.loadUserByUsername(username);

                    // 7. Validate JWT
                    if (jwtService.isTokenValid(jwt, userDetails)) {

                        // 8. Create authentication object
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );

                        // 9. Attach request details
                        authentication.setDetails(
                                new WebAuthenticationDetailsSource()
                                        .buildDetails(request)
                        );

                        // 10. Set authentication in Spring Security
                        SecurityContextHolder.getContext()
                                .setAuthentication(authentication);
                    }
                }
            }

        } catch (JwtException | IllegalArgumentException |
                 UsernameNotFoundException e) {

            // Invalid, expired or unrecognized user token.
            // Continue without authenticating the user.
            SecurityContextHolder.clearContext();
        }

        // 11. Continue request
        filterChain.doFilter(request, response);
    }
}
