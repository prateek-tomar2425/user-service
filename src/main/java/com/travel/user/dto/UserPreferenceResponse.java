package com.travel.user.dto;

import java.util.UUID;

public record UserPreferenceResponse(
        UUID userId,
        String travelStyle,
        String explorationStyle,
        String foodPreference,
        String travelScope
) {}