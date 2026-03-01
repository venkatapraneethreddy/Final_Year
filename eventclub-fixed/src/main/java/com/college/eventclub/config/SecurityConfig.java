package com.college.eventclub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})
            .authorizeHttpRequests(auth -> auth

                // PUBLIC
                .requestMatchers("/api/auth/**").permitAll()

                // ADMIN only
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // ORGANIZER only
                .requestMatchers("/api/events/my").hasRole("ORGANIZER")
                .requestMatchers("/api/events/organizer/**").hasRole("ORGANIZER")
                .requestMatchers(HttpMethod.POST, "/api/events/**").hasRole("ORGANIZER")
                .requestMatchers(HttpMethod.PUT, "/api/events/**").hasRole("ORGANIZER")
                .requestMatchers(HttpMethod.DELETE, "/api/events/**").hasRole("ORGANIZER")
                .requestMatchers("/api/clubs/my").hasRole("ORGANIZER")
                .requestMatchers(HttpMethod.POST, "/api/clubs").hasRole("ORGANIZER")

                // ATTENDANCE - organizer scans QR
                .requestMatchers("/api/attendance/**").hasRole("ORGANIZER")

                // STUDENT
                .requestMatchers(HttpMethod.GET, "/api/events").hasAnyRole("STUDENT", "ORGANIZER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/events/**").hasAnyRole("STUDENT", "ORGANIZER", "ADMIN")
                .requestMatchers("/api/registrations/event/**").hasRole("ORGANIZER")
                .requestMatchers("/api/registrations/**").hasRole("STUDENT")
                .requestMatchers("/api/payments/**").hasRole("STUDENT")

                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
