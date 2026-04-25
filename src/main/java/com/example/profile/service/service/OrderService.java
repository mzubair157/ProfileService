package com.example.profile.service.service;

import com.example.profile.service.dto.CreateOrderRequest;
import com.example.profile.service.dto.CreateOrderResponse;
import com.example.profile.service.dto.OrderHistoryResponseDTO;

public interface OrderService {

    OrderHistoryResponseDTO getOrderHistory(Long userId, int page, int size);

    CreateOrderResponse createOrder(CreateOrderRequest request);
}
