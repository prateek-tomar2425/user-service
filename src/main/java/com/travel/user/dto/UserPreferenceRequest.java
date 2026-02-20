package com.travel.user.dto;

import java.util.List;

public record UserPreferenceRequest(
        String travelStyle,
        String explorationStyle,
        String foodPreference,
        String travelScope,
        String budget,
        List<String> preferredActivities,
        List<String> preferredDestinations
) {}
