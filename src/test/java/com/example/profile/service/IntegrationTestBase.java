package com.example.profile.service;

import com.example.profile.service.dto.DiscountDTO;
import com.example.profile.service.dto.RecommendationDTO;
import com.example.profile.service.service.RecommendationClient;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.reset;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class IntegrationTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected CacheManager cacheManager;

    @MockBean
    protected RecommendationClient recommendationClient;

    protected final List<RecommendationDTO> recommendedItems = List.of(
            new RecommendationDTO(501L, "Travel Adapter", "Frequent buyer cohort", 0.77)
    );

    protected final List<DiscountDTO> discountFixtures = List.of(
            new DiscountDTO("WELCOME10", BigDecimal.TEN, 4)
    );

    @BeforeEach
    void resetStubs() {
        reset(recommendationClient);
        cacheManager.getCacheNames().forEach(name -> {
            var cache = cacheManager.getCache(name);
            if (cache != null) {
                cache.clear();
            }
        });
    }
}
