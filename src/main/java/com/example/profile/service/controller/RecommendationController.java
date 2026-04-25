package com.example.profile.service.controller;

import com.example.profile.service.dto.RecommendationDTO;
import com.example.profile.service.service.RecommendationService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/users/{userId}")
    public List<RecommendationDTO> getPersonalizedOffers(@PathVariable Long userId) {
        return recommendationService.getPersonalizedOffers(userId);
    }
}
