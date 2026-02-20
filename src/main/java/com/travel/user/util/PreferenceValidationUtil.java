package com.travel.user.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class for validating user preferences against predefined lists
 * Uses the same valid activities and destinations as Discovery Service
 */
public class PreferenceValidationUtil {

    // Predefined list of valid activities (57 total)
    public static final Set<String> VALID_ACTIVITIES = new HashSet<>(Arrays.asList(
            "art-gallery-visit", "beach-relaxation", "bungee-jumping", "cable-car-ride",
            "camping", "casino-gaming", "camel-safari", "cathedral-tour", "cave-exploration",
            "city-walk", "climbing", "concert-attendance", "cooking-class", "cycling",
            "dance-lessons", "desert-trek", "diving", "elephant-sanctuary", "festival-attendance",
            "fine-dining", "fishing", "food-tour", "geyser-viewing", "glacier-hiking",
            "gondola-ride", "golf", "hiking", "horse-riding", "hot-air-balloon", "jazz-club",
            "kayaking", "meditation", "monument-visit", "mountain-climbing", "museum-visit",
            "music-festival", "night-market", "photography-tour", "pub-crawl", "quad-biking",
            "rock-climbing", "safari", "sailing", "scuba-diving", "shopping-tour", "skiing",
            "snorkeling", "spa-treatment", "street-food-tour", "sunrise-hike", "sunset-cruise",
            "tea-ceremony", "temple-visit", "theater-show", "trekking", "volcano-tour",
            "water-skiing", "waterfall-visit", "whale-watching", "wine-tasting", "zip-lining", "local-culture",
            "photography", "nature", "historical-sites", "culinary-experience", "nightlife", "relaxation", "adventure-sports"
    ));

    // Predefined list of valid destinations
    public static final Set<String> VALID_DESTINATIONS = new HashSet<>(Arrays.asList(
            "mountains", "beaches", "cities", "countryside", "deserts", "forests", "islands",
            "coastal-areas", "historic-sites", "adventure-zones", "cultural-hubs", "food-capitals",
            "tropical", "arctic", "temperate", "urban", "rural", "sacred-sites", "nature-reserves",
            "wildlife-sanctuaries", "wine-regions", "skiing-destinations", "diving-spots", "trekking-trails"
    ));

    /**
     * Validate that all provided activities are in the valid activities list
     * @param activities list of activities to validate
     * @throws IllegalArgumentException if invalid activities are found
     */
    public static void validateActivities(java.util.List<String> activities) {
        if (activities == null || activities.isEmpty()) {
            return;
        }

        Set<String> invalidActivities = activities.stream()
                .map(String::toLowerCase)
                .filter(activity -> !VALID_ACTIVITIES.contains(activity))
                .collect(Collectors.toSet());

        if (!invalidActivities.isEmpty()) {
            throw new IllegalArgumentException(
                    "Invalid activities provided: " + invalidActivities +
                    ". Valid activities are: " + VALID_ACTIVITIES
            );
        }
    }

    /**
     * Validate that all provided destinations are in the valid destinations list
     * @param destinations list of destinations to validate
     * @throws IllegalArgumentException if invalid destinations are found
     */
    public static void validateDestinations(java.util.List<String> destinations) {
        if (destinations == null || destinations.isEmpty()) {
            return;
        }

        Set<String> invalidDestinations = destinations.stream()
                .map(String::toLowerCase)
                .filter(destination -> !VALID_DESTINATIONS.contains(destination))
                .collect(Collectors.toSet());

        if (!invalidDestinations.isEmpty()) {
            throw new IllegalArgumentException(
                    "Invalid destinations provided: " + invalidDestinations +
                    ". Valid destinations are: " + VALID_DESTINATIONS
            );
        }
    }

    /**
     * Get list of valid activities
     * @return set of all valid activities
     */
    public static Set<String> getValidActivities() {
        return new HashSet<>(VALID_ACTIVITIES);
    }

    /**
     * Get list of valid destinations
     * @return set of all valid destinations
     */
    public static Set<String> getValidDestinations() {
        return new HashSet<>(VALID_DESTINATIONS);
    }
}

