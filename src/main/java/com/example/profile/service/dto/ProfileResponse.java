package com.example.profile.service.dto;

public record ProfileResponse(
        Long id,
        String username,
        String email,
        String region,
        Long loyaltyAccountId,
        Long loyaltyBalance
) {
}
