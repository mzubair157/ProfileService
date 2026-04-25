package com.example.profile.service.dto;

import jakarta.validation.constraints.NotNull;

public record PointsRequest(@NotNull Long delta) {
}
