package com.aacharya.timetablemanagement.config;

import com.aacharya.timetablemanagement.service.CustomUserDetailsService;
import com.aacharya.timetablemanagement.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

            // 3. Remove "Bearer " from token
            String jwt = authHeader.substring(7);

            try {

                // 4. Extract username from JWT
                String username = jwtService.extractUsername(jwt);

                // 5. Check whether user is already authenticated
                if (username != null &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {

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

                        // 10. Tell Spring Security that user is authenticated
                        SecurityContextHolder
                                .getContext()
                                .setAuthentication(authentication);
                    }
                }

            } catch (Exception e) {

                // Invalid or expired JWT
                System.out.println("JWT validation failed: " + e.getMessage());
            }

            // Continue request
            filterChain.doFilter(request, response);
        }
}
