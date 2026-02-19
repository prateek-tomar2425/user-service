package com.travel.user.dto;

public record UserPreferenceRequest(
        String travelStyle,
        String explorationStyle,
        String foodPreference,
        String travelScope
) {}
