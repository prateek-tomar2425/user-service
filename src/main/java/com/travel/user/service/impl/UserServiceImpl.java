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
import com.travel.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserPreferenceRepository preferenceRepository;
    @Override
    public UserResponse createUser(UserCreateRequest request) {

        repository.findByEmail(request.email())
                .ifPresent(u -> {
                    throw new DuplicateUserException("User already exists");
                });

        User user = User.builder()
                .id(UUID.randomUUID())
                .email(request.email())
                .passwordHash(encodePassword(request.password()))
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
    public UserPreferenceResponse updatePreferences(
            UUID userId,
            UserPreferenceRequest request) {

        repository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

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
        preference.setUpdatedAt(Instant.now());

        UserPreference saved = preferenceRepository.save(preference);

        return new UserPreferenceResponse(
                userId,
                saved.getTravelStyle(),
                saved.getExplorationStyle(),
                saved.getFoodPreference(),
                saved.getTravelScope()
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
                preference.getTravelScope()
        );
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
