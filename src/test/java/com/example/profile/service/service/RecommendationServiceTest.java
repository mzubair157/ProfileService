package com.example.profile.service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.profile.service.dto.RecommendationDTO;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private RecommendationClient recommendationClient;

    @Test
    void returnsClientRecommendationsWhenClientSucceeds() {
        when(recommendationClient.fetchRecommendations(9L))
                .thenReturn(List.of(new RecommendationDTO(1L, "Noise Cancelling Headphones", "Affinity match", 0.92)));

        RecommendationServiceImpl service = new RecommendationServiceImpl(recommendationClient);
        List<RecommendationDTO> result = service.getPersonalizedOffers(9L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().name()).isEqualTo("Noise Cancelling Headphones");
    }

    @Test
    void returnsFallbackRecommendationsWhenClientFails() {
        when(recommendationClient.fetchRecommendations(9L))
                .thenThrow(new RestClientException("timeout"));

        RecommendationServiceImpl service = new RecommendationServiceImpl(recommendationClient);
        List<RecommendationDTO> result = service.getPersonalizedOffers(9L);

        assertThat(result).hasSize(2);
        assertThat(result.getFirst().name()).isEqualTo("Popular Picks");
    }
}
