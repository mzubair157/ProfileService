package com.example.profile.service;

import com.example.profile.service.domain.LoyaltyAccount;
import com.example.profile.service.domain.UserProfile;
import com.example.profile.service.repository.ProfileRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FullSystemSmokeTest extends IntegrationTestBase {

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    void majorReadEndpointsRespond() throws Exception {
        LoyaltyAccount account = new LoyaltyAccount();
        account.setBalance(90L);

        UserProfile profile = new UserProfile();
        profile.setUsername("smoke");
        profile.setEmail("smoke@example.com");
        profile.setRegion("US");
        profile.setLoyaltyAccount(account);
        UserProfile saved = profileRepository.save(profile);

        when(recommendationClient.fetchRecommendations(saved.getId())).thenReturn(recommendedItems);

        mockMvc.perform(get("/profiles/{id}", saved.getId())).andExpect(status().isOk());
        mockMvc.perform(get("/favorites/{userId}", saved.getId())).andExpect(status().isOk());
        mockMvc.perform(get("/recommendations/users/{userId}", saved.getId())).andExpect(status().isOk());
    }
}
