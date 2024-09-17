package io.github.justinscottjenecke.spring_security_jpa_tutorial.config;

import io.github.justinscottjenecke.spring_security_jpa_tutorial.user.User;
import io.jsonwebtoken.security.SecurityBuilder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService; // used to gain access to jwt related filtering/validating/etc. functionality
    private final UserDetailsService userDetailsService; // used to interface with db

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        // filters:

        // if no header found or header string does not start with 'Bearer'
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwtToken);

        // if userEmail exists so incoming request has valid jwt but user has not been loaded from database
        // or user is not yet authenticated.
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // retrieve user from db user using email in token
            UserDetails user = this.userDetailsService.loadUserByUsername(userEmail);

            if(jwtService.isTokenValid(jwtToken, user)) {

                // if token is valid, generate new authentication token
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );

                // set details of auth token
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // persist auth token inside security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            filterChain.doFilter(request, response);
        }

    }
}
