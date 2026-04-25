package com.example.profile.service.service;

import com.example.profile.service.dto.RecommendationDTO;
import java.util.List;

public interface RecommendationService {

    List<RecommendationDTO> getPersonalizedOffers(Long userId);
}
