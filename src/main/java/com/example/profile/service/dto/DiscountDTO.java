package com.example.profile.service.dto;

import java.math.BigDecimal;

public record DiscountDTO(String code, BigDecimal percentage, long eligibleUserCount) {
}
