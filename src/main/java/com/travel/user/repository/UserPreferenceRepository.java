package com.travel.user.repository;

import com.travel.user.model.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserPreferenceRepository
        extends JpaRepository<UserPreference, UUID> {
}
