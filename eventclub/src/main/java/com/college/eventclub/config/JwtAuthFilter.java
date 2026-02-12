package com.college.eventclub.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String SECRET_KEY =
            "mysecretkeymysecretkeymysecretkey123";


    @Override
protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();

    // skip auth endpoints
    if (path.startsWith("/api/auth/")) {
        return true;
    }

    // skip OPTIONS (important for POST)
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
        return true;
    }

    return false;
}


    @Override
protected void doFilterInternal(HttpServletRequest request,
                                HttpServletResponse response,
                                FilterChain filterChain)
        throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
    filterChain.doFilter(request, response);
    return;
}


    String token = authHeader.substring(7);

    try {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String email = claims.getSubject();
        String role = claims.get("role", String.class);

        // Create authentication object
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + role))
                );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        System.out.println("SETTING AUTHORITY = ROLE_" + role);
System.out.println("AUTH IN CONTEXT = " +
    SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        filterChain.doFilter(request, response);

    } catch (Exception e) {
    filterChain.doFilter(request, response);
    return;
}

    System.out.println("AUTH HEADER = " + request.getHeader("Authorization"));

}
}