package com.travel.user.service;

import com.travel.user.dto.UserCreateRequest;
import com.travel.user.dto.UserPreferenceRequest;
import com.travel.user.dto.UserPreferenceResponse;
import com.travel.user.dto.UserResponse;

import java.util.UUID;

public interface UserService {

    UserResponse createUser(UserCreateRequest request);
    UserResponse getUser(UUID id);
    UserResponse getByEmail(String email);
    UserPreferenceResponse updatePreferences(
            UUID userId,
            UserPreferenceRequest request);

    UserPreferenceResponse getPreferences(UUID userId);
}
