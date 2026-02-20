package com.travel.user.service;

import com.travel.user.model.User;
import com.travel.user.dto.AuthRequest;
import com.travel.user.dto.AuthResponse;
import com.travel.user.dto.UserCreateRequest;
import com.travel.user.exception.InvalidCredentialsException;
import com.travel.user.exception.UserAlreadyExistsException;
import com.travel.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Register a new user with email and password
     * Returns a JWT token for immediate use
     */
    public AuthResponse register(AuthRequest request) {
        return registerWithRole(request, User.Role.USER);
    }

    /**
     * Register a new admin user
     * Requires ADMIN_SECRET_KEY environment variable to be set
     * @param request The registration request
     * @param adminSecretKey The secret key for admin registration
     * @return AuthResponse with token and user details
     */
    public AuthResponse registerAdmin(AuthRequest request, String adminSecretKey) {
        // Validate admin secret key
        String validAdminSecret = System.getenv("ADMIN_SECRET_KEY");
        if (validAdminSecret == null) {
            validAdminSecret = "CHANGE_THIS_IN_PRODUCTION_12345"; // Default for development
        }

        if (!adminSecretKey.equals(validAdminSecret)) {
            throw new InvalidCredentialsException("Invalid admin secret key");
        }

        return registerWithRole(request, User.Role.ADMIN);
    }

    /**
     * Internal method to register a user with a specific role
     */
    private AuthResponse registerWithRole(AuthRequest request, User.Role role) {
        // Check if user already exists
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }

        // Create new user
        User user = new User();
        user.setId(java.util.UUID.randomUUID());  // Generate UUID for new user
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setCreatedAt(java.time.Instant.now());
        user.setUpdatedAt(java.time.Instant.now());

        User savedUser = userRepository.save(user);

        // Generate JWT token for new user
        String token = generateToken(savedUser.getId(), savedUser.getEmail(), savedUser.getRole().name());

        return AuthResponse.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .token(token)
                .expiresIn(jwtExpiration)
                .message(role == User.Role.ADMIN ? "Admin user registered successfully" : "User registered successfully")
                .build();
    }

    /**
     * Login with email and password
     * Returns a JWT token if credentials are valid
     */
    public AuthResponse login(AuthRequest request) {
        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        // Generate JWT token
        String token = generateToken(user.getId(), user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .token(token)
                .expiresIn(jwtExpiration)
                .message("Login successful")
                .build();
    }

    /**
     * Generate a JWT token for the user
     */
    private String generateToken(java.util.UUID userId, String email, String role) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiryDate = new Date(nowMillis + jwtExpiration);

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId.toString())
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Validate a JWT token
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get claims from a JWT token
     */
    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Refresh a JWT token
     * Validates the old token and generates a new one
     */
    public AuthResponse refreshToken(String oldToken) {
        // Validate old token
        if (!validateToken(oldToken)) {
            throw new InvalidCredentialsException("Invalid or expired token");
        }

        // Extract claims from old token
        Claims claims = getClaimsFromToken(oldToken);
        String email = claims.getSubject();
        String role = claims.get("role", String.class);
        String userIdStr = claims.get("userId", String.class);

        // Get user from database to ensure they still exist
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        // Generate new token
        String newToken = generateToken(user.getId(), user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .token(newToken)
                .expiresIn(jwtExpiration)
                .message("Token refreshed successfully")
                .build();
    }
}

