package com.example.profile.service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateOrderRequest(
        @NotNull Long userId,
        @NotNull @DecimalMin(value = "0.01") BigDecimal amount
) {
}
