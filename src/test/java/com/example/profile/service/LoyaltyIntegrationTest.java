package com.example.profile.service;

import com.example.profile.service.domain.LoyaltyAccount;
import com.example.profile.service.repository.LoyaltyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoyaltyIntegrationTest extends IntegrationTestBase {

    @Autowired
    private LoyaltyRepository loyaltyRepository;

    @Test
    void adjustsPointsViaHttpEndpoint() throws Exception {
        LoyaltyAccount account = new LoyaltyAccount();
        account.setBalance(100L);
        LoyaltyAccount saved = loyaltyRepository.save(account);

        mockMvc.perform(post("/loyalty/{accountId}/points", saved.getId())
                        .header("Idempotency-Key", "loyalty-http-op-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"delta\":25}"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}
