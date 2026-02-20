package com.travel.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private UUID userId;
    private String email;
    private String token;
    private Long expiresIn;
    private String message;

    public static AuthResponse success(UUID userId, String email, String token, Long expiresIn) {
        return AuthResponse.builder()
                .userId(userId)
                .email(email)
                .token(token)
                .expiresIn(expiresIn)
                .message("Authentication successful")
                .build();
    }

    public static AuthResponse error(String message) {
        return AuthResponse.builder()
                .message(message)
                .build();
    }
}

