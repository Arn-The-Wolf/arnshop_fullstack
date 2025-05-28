package com.arnshop.service;

import com.arnshop.dto.AuthenticationRequest;
import com.arnshop.dto.AuthenticationResponse;
import com.arnshop.dto.RegisterRequest;
import com.arnshop.model.Role;
import com.arnshop.model.User;
import com.arnshop.repository.UserRepository;
import com.arnshop.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

        private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        public AuthenticationResponse register(RegisterRequest request) {
                logger.info("Attempting to register user: {}", request.getEmail());
                if (userRepository.existsByEmail(request.getEmail())) {
                        logger.warn("Registration failed: Email already exists {}", request.getEmail());
                        throw new IllegalStateException("Email already exists");
                }
                var user = User.builder()
                                .firstName(request.getFirstName())
                                .lastName(request.getLastName())
                                .email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .role(Role.USER)
                                .isVip(false)
                                .isEnabled(true)
                                .build();

                userRepository.save(user);
                logger.info("User registered successfully: {}", user.getEmail());
                var jwtToken = jwtService.generateToken(user);
                logger.info("Generated JWT token for user: {}", user.getEmail());

                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .build();
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) {
                logger.info("Attempting to authenticate user: {}", request.getEmail());
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));
                logger.info("Authentication successful for user: {}", request.getEmail());

                var user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow();
                var jwtToken = jwtService.generateToken(user);
                logger.info("Generated JWT token for user: {}", user.getEmail());

                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .build();
        }
}