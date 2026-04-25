package com.example.profile.service.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderHistoryItemDTO(Long id, Instant date, BigDecimal amount) {
}
