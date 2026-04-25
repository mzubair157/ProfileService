package com.example.profile.service.service;

import com.example.profile.service.dto.RecommendationDTO;
import com.example.profile.service.util.DataValidationUtils;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private static final int FAILURE_THRESHOLD = 3;
    private static final long OPEN_WINDOW_MILLIS = 30_000;

    private final RecommendationClient recommendationClient;
    private final AtomicInteger consecutiveFailures = new AtomicInteger();
    private final AtomicLong circuitOpenUntil = new AtomicLong();

    public RecommendationServiceImpl(RecommendationClient recommendationClient) {
        this.recommendationClient = recommendationClient;
    }

    @Override
    public List<RecommendationDTO> getPersonalizedOffers(Long userId) {
        DataValidationUtils.requirePositiveId(userId, "userId");

        long now = System.currentTimeMillis();
        if (circuitOpenUntil.get() > now) {
            return fallbackRecommendations(userId);
        }

        try {
            List<RecommendationDTO> recommendations = recommendationClient.fetchRecommendations(userId);
            consecutiveFailures.set(0);
            circuitOpenUntil.set(0);
            return recommendations;
        } catch (RestClientException ex) {
            int failures = consecutiveFailures.incrementAndGet();
            if (failures >= FAILURE_THRESHOLD) {
                circuitOpenUntil.set(now + OPEN_WINDOW_MILLIS);
            }
            return fallbackRecommendations(userId);
        }
    }

    private List<RecommendationDTO> fallbackRecommendations(Long userId) {
        return List.of(
                new RecommendationDTO(-userId, "Popular Picks", "Fallback recommendations while AI is unavailable", 0.15),
                new RecommendationDTO(-(userId + 1), "Trending Discounts", "Static backup selection", 0.10)
        );
    }
}
