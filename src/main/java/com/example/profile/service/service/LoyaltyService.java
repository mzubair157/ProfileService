package com.example.profile.service.service;

public interface LoyaltyService {

    void adjustPoints(Long accountId, Long delta, String idempotencyKey);
}
