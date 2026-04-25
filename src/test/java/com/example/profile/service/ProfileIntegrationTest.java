package com.example.profile.service;

import com.example.profile.service.domain.LoyaltyAccount;
import com.example.profile.service.domain.UserProfile;
import com.example.profile.service.repository.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileIntegrationTest extends IntegrationTestBase {

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    void returnsProfilePayload() throws Exception {
        LoyaltyAccount account = new LoyaltyAccount();
        account.setBalance(320L);

        UserProfile profile = new UserProfile();
        profile.setUsername("ada");
        profile.setEmail("ada@example.com");
        profile.setRegion("EU");
        profile.setLoyaltyAccount(account);

        UserProfile saved = profileRepository.save(profile);

        mockMvc.perform(get("/profiles/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("ada"))
                .andExpect(jsonPath("$.loyaltyBalance").value(320));
    }
}
