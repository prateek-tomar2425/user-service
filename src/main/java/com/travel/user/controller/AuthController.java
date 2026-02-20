package com.travel.user.controller;

import com.travel.user.dto.AuthRequest;
import com.travel.user.dto.AuthResponse;
import com.travel.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Register a new user - PUBLIC ENDPOINT
     * This endpoint allows users to create an account without authentication
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Register a new admin user - REQUIRES ADMIN SECRET KEY
     * This endpoint allows creation of admin accounts with a secret key
     * Pass the secret key in X-Admin-Secret header
     */
    @PostMapping("/register-admin")
    public ResponseEntity<AuthResponse> registerAdmin(
            @Valid @RequestBody AuthRequest request,
            @RequestHeader(value = "X-Admin-Secret") String adminSecret) {
        return ResponseEntity.ok(authService.registerAdmin(request, adminSecret));
    }

    /**
     * Login with email and password to get JWT token
     * This endpoint allows users to authenticate and receive a JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * Validate JWT token
     * This endpoint verifies if a token is still valid
     */
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(
            @RequestHeader(value = "Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(false);
        }

        String token = authHeader.substring(7);
        boolean isValid = authService.validateToken(token);
        return ResponseEntity.ok(isValid);
    }

    /**
     * Refresh JWT token
     * This endpoint allows users to get a new token using their current token
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestHeader(value = "Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }

        String token = authHeader.substring(7);
        return ResponseEntity.ok(authService.refreshToken(token));
    }
}

