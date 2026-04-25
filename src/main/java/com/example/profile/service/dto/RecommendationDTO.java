package com.example.profile.service.dto;

public record RecommendationDTO(
        Long id,
        String name,
        String rationale,
        double score
) {
}
