package com.example.profile.service.service;

import com.example.profile.service.dto.DiscountDTO;
import com.example.profile.service.repository.DiscountRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;

    public DiscountServiceImpl(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "discounts", key = "'active'")
    public List<DiscountDTO> getAllActiveDiscounts() {
        return discountRepository.findActiveDiscountSummaries(LocalDateTime.now());
    }
}
