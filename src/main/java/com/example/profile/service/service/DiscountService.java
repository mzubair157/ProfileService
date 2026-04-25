package com.example.profile.service.service;

import com.example.profile.service.dto.DiscountDTO;
import java.util.List;

public interface DiscountService {

    List<DiscountDTO> getAllActiveDiscounts();
}
