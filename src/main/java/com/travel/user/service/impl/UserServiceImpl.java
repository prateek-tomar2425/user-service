package com.travel.user.service.impl;

import com.travel.user.dto.UserCreateRequest;
import com.travel.user.dto.UserPreferenceRequest;
import com.travel.user.dto.UserPreferenceResponse;
import com.travel.user.dto.UserResponse;
import com.travel.user.exception.DuplicateUserException;
import com.travel.user.exception.ResourceNotFoundException;
import com.travel.user.mapper.UserMapper;
import com.travel.user.model.User;
import com.travel.user.model.UserPreference;
import com.travel.user.repository.UserPreferenceRepository;
import com.travel.user.repository.UserRepository;
import com.travel.user.service.PasswordService;
import com.travel.user.service.UserService;
import com.travel.user.util.PreferenceValidationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordService passwordService;
    private final UserPreferenceRepository preferenceRepository;
    private final ObjectMapper objectMapper;  // For JSON serialization
    @Override
    public UserResponse createUser(UserCreateRequest request) {

        repository.findByEmail(request.email())
                .ifPresent(u -> {
                    throw new DuplicateUserException("User already exists");
                });
        User.Role userRole = User.Role.USER;

        if (request.role() != null) {
            userRole = User.Role.valueOf(request.role().toUpperCase());
        }
        User user = User.builder()
                .id(UUID.randomUUID())
                .email(request.email())
                .passwordHash(encodePassword(request.password()))
                .role(userRole)
                .createdAt(Instant.now())
                .build();

        return UserMapper.toResponse(repository.save(user));
    }
    @Override
    public UserResponse getUser(UUID id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return UserMapper.toResponse(user);
    }

    @Override
    public UserResponse getByEmail(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return UserMapper.toResponse(user);
    }

    @Override
    public UserPreferenceResponse updatePreferences(
            UUID userId,
            UserPreferenceRequest request) {

        repository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // VALIDATE ACTIVITIES - must be from predefined list
        PreferenceValidationUtil.validateActivities(request.preferredActivities());

        // VALIDATE DESTINATIONS - must be from predefined list
        PreferenceValidationUtil.validateDestinations(request.preferredDestinations());

        UserPreference preference = preferenceRepository
                .findById(userId)
                .orElse(UserPreference.builder()
                        .userId(userId)
                        .build());
        preference.setUserId(userId);
        preference.setTravelStyle(request.travelStyle());
        preference.setExplorationStyle(request.explorationStyle());
        preference.setFoodPreference(request.foodPreference());
        preference.setTravelScope(request.travelScope());

        // Set new fields
        preference.setBudget(request.budget());
        preference.setPreferredActivities(serializeList(request.preferredActivities()));
        preference.setPreferredDestinations(serializeList(request.preferredDestinations()));

        preference.setUpdatedAt(Instant.now());

        UserPreference saved = preferenceRepository.save(preference);

        return new UserPreferenceResponse(
                userId,
                saved.getTravelStyle(),
                saved.getExplorationStyle(),
                saved.getFoodPreference(),
                saved.getTravelScope(),
                saved.getBudget(),
                deserializeList(saved.getPreferredActivities()),
                deserializeList(saved.getPreferredDestinations())
        );
    }

    @Override
    public UserPreferenceResponse getPreferences(UUID userId) {
        repository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserPreference preference = preferenceRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User preferences not found"));

        return new UserPreferenceResponse(
                userId,
                preference.getTravelStyle(),
                preference.getExplorationStyle(),
                preference.getFoodPreference(),
                preference.getTravelScope(),
                preference.getBudget(),
                deserializeList(preference.getPreferredActivities()),
                deserializeList(preference.getPreferredDestinations())
        );
    }

    private String encodePassword(String password) {
        return passwordService.hash(password);
    }

    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordService.matches(rawPassword, encodedPassword);
    }

    /**
     * Serialize a list of strings to JSON format
     */
    private String serializeList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            // If serialization fails, return null
            return null;
        }
    }

    /**
     * Deserialize a JSON string to a list of strings
     */
    private List<String> deserializeList(String json) {
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
        } catch (Exception e) {
            // If deserialization fails, return empty list
            return new ArrayList<>();
        }
    }
}
