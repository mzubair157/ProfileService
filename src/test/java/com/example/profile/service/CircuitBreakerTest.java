package com.example.profile.service;

import com.example.profile.service.service.RecommendationService;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClientException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class CircuitBreakerTest extends IntegrationTestBase {

    @Autowired
    private RecommendationService recommendationService;

    @Test
    void returnsFallbackDataWhenAiClientFails() {
        when(recommendationClient.fetchRecommendations(999L))
                .thenThrow(new RestClientException("timeout"));

        IntStream.range(0, 3).forEach(index -> recommendationService.getPersonalizedOffers(999L));
        var result = recommendationService.getPersonalizedOffers(999L);

        assertThat(result).isNotEmpty();
        assertThat(result.getFirst().name()).isEqualTo("Popular Picks");
    }
}
