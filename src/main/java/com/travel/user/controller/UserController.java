package com.travel.user.controller;

import com.travel.user.dto.UserCreateRequest;
import com.travel.user.dto.UserPreferenceRequest;
import com.travel.user.dto.UserPreferenceResponse;
import com.travel.user.dto.UserResponse;
import com.travel.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserCreateRequest request) {

        return ResponseEntity.ok(service.createUser(request));
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID id) {

        return ResponseEntity.ok(service.getUser(id));
    }
    @PutMapping("/{id}/preferences")
    public ResponseEntity<UserPreferenceResponse> updatePreferences(
            @PathVariable UUID id,
            @RequestBody UserPreferenceRequest request) {

        return ResponseEntity.ok(
                service.updatePreferences(id, request)
        );
    }
    @GetMapping("/{id}/preferences")
    public ResponseEntity<UserPreferenceResponse> getPreferences(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                service.getPreferences(id)
        );
    }


}
