package com.arnshop.controller;

import com.arnshop.dto.AuthenticationRequest;
import com.arnshop.dto.AuthenticationResponse;
import com.arnshop.dto.RegisterRequest;
import com.arnshop.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8090", allowCredentials = "true", allowedHeaders = "*", exposedHeaders = "Authorization")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        logger.info("Received registration request for email: {}", request.getEmail());
        try {
            logger.debug("Registration request details: firstName={}, lastName={}, email={}",
                    request.getFirstName(), request.getLastName(), request.getEmail());

            AuthenticationResponse response = authenticationService.register(request);
            logger.info("Registration successful for email: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            logger.warn("Registration failed - IllegalStateException: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            logger.error("Registration failed - Unexpected error", e);
            return ResponseEntity.internalServerError().body(new ErrorResponse("Registration failed"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        logger.info("Received login request for email: {}", request.getEmail());
        try {
            AuthenticationResponse response = authenticationService.authenticate(request);
            logger.info("Login successful for email: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.warn("Login failed for email: {} - {}", request.getEmail(), e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse("Invalid credentials"));
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        logger.error("Validation errors during request body processing: {}", errors);
        return ResponseEntity.badRequest().body(new ErrorResponse("Validation failed: " + String.join(", ", errors)));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleAllOtherExceptions(Exception ex) {
        logger.error("An unexpected error occurred", ex);
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse("An unexpected error occurred. Please try again later."));
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class ErrorResponse {
    private String message;
}