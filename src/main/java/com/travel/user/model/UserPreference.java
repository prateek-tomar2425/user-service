package com.travel.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;
@Entity
@Table(name = "user_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreference {

    @Id
    private UUID userId;

    private String travelStyle;
    private String explorationStyle;
    private String foodPreference;
    private String travelScope;

    private Instant updatedAt;
}
