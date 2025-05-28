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

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8090", allowCredentials = "true", allowedHeaders = "*", exposedHeaders = "Authorization")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(authenticationService.register(request));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Registration failed"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            return ResponseEntity.ok(authenticationService.authenticate(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Invalid credentials"));
        }
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class ErrorResponse {
    private String message;
}