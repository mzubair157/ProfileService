package com.example.profile.service.service;

import com.example.profile.service.dto.RecommendationDTO;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class RecommendationClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public RecommendationClient(RestTemplate restTemplate,
                                @Value("${recommendation.client.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<RecommendationDTO> fetchRecommendations(Long userId) {
        try {
            RecommendationDTO[] response = restTemplate.getForObject(
                    baseUrl + "/v1/recommend?user={userId}",
                    RecommendationDTO[].class,
                    userId
            );
            if (response == null) {
                return List.of();
            }
            return Arrays.asList(response);
        } catch (RestClientException ex) {
            throw ex;
        }
    }
}
