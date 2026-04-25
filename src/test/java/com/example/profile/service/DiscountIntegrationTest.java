package com.example.profile.service;

import com.example.profile.service.domain.Discount;
import com.example.profile.service.repository.DiscountRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DiscountIntegrationTest extends IntegrationTestBase {

    @Autowired
    private DiscountRepository discountRepository;

    @Test
    void returnsOnlyActiveNonExpiredDiscounts() throws Exception {
        Discount active = new Discount();
        active.setCode("FLASH20");
        active.setPercentage(BigDecimal.valueOf(20));
        active.setActive(true);
        active.setExpiryDate(LocalDateTime.now().plusDays(1));
        discountRepository.save(active);

        Discount expired = new Discount();
        expired.setCode("OLD");
        expired.setPercentage(BigDecimal.valueOf(5));
        expired.setActive(true);
        expired.setExpiryDate(LocalDateTime.now().minusDays(1));
        discountRepository.save(expired);

        mockMvc.perform(get("/discounts/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("FLASH20"))
                .andExpect(jsonPath("$[0].eligibleUserCount").value(0));
    }
}
