package com.college.eventclub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        .cors(cors -> {})   // ✅ ENABLE CORS
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/api/attendance/scan").authenticated()
            .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll() // ✅ VERY IMPORTANT
            .anyRequest().authenticated()
        )
        .addFilterBefore(
    jwtAuthFilter(),
    UsernamePasswordAuthenticationFilter.class
)
;

    return http.build();
}


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
