package com.example.profile.service.messaging;

import java.time.Instant;

public record OrderEvent(long orderId, Long userId, Instant occurredAt) {

    public OrderEvent(long orderId, Long userId) {
        this(orderId, userId, Instant.now());
    }
}
