package com.example.profile.service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.profile.service.dto.DiscountDTO;
import com.example.profile.service.repository.DiscountRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DiscountServiceTest {

    @Mock
    private DiscountRepository discountRepository;

    @Test
    void getAllActiveDiscountsReturnsRepositoryProjection() {
        List<DiscountDTO> discounts = List.of(new DiscountDTO("SPRING25", BigDecimal.valueOf(25), 17));
        when(discountRepository.findActiveDiscountSummaries(org.mockito.ArgumentMatchers.any(LocalDateTime.class)))
                .thenReturn(discounts);

        DiscountServiceImpl service = new DiscountServiceImpl(discountRepository);
        List<DiscountDTO> result = service.getAllActiveDiscounts();

        assertThat(result).containsExactlyElementsOf(discounts);
        verify(discountRepository).findActiveDiscountSummaries(org.mockito.ArgumentMatchers.any(LocalDateTime.class));
    }
}
