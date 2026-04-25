package com.example.profile.service.controller;

import com.example.profile.service.dto.DiscountDTO;
import com.example.profile.service.service.DiscountService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/discounts")
public class DiscountController {

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping("/active")
    public List<DiscountDTO> getActiveDiscounts() {
        return discountService.getAllActiveDiscounts();
    }
}
