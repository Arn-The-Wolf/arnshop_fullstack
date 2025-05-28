package com.arnshop.security;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        logger.info("Processing request: {} {}", request.getMethod(), request.getRequestURI());

        // Handle CORS preflight requests
        if (request.getMethod().equals("OPTIONS")) {
            logger.info("Handling OPTIONS request");
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request, response);
            return;
        }

        // Skip authentication for public endpoints
        String requestPath = request.getRequestURI();
        if (requestPath.startsWith("/api/auth/") ||
                requestPath.startsWith("/api/products/") ||
                requestPath.startsWith("/api/categories/") ||
                requestPath.startsWith("/api/brands/") ||
                requestPath.startsWith("/api/testimonials/") ||
                requestPath.startsWith("/api/newsletter/")) {
            logger.info("Skipping authentication for public endpoint: {}", requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Missing or invalid Authorization header");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        try {
            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUsername(jwt);
            logger.debug("Extracted user email from token: {}", userEmail);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                logger.debug("User email found and no authentication in security context");
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                logger.debug("Loaded UserDetails for user: {}", userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    logger.debug("Token is valid for user: {}", userEmail);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.debug("Security context set for user: {}", userEmail);
                } else {
                    logger.warn("Invalid token for user: {}", userEmail);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Invalid token");
                    return;
                }
            }
        } catch (Exception e) {
            logger.error("Error processing JWT token", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token format");
            return;
        }

        logger.debug("Continuing filter chain");
        filterChain.doFilter(request, response);
    }
}