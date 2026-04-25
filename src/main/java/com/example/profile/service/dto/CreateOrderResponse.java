package com.example.profile.service.dto;

public record CreateOrderResponse(
        OrderHistoryItemDTO order,
        Long loyaltyBalance,
        Long pointsAwarded
) {
}
