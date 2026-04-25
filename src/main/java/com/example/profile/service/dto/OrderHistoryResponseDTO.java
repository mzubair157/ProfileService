package com.example.profile.service.dto;

import java.util.List;

public record OrderHistoryResponseDTO(List<OrderHistoryItemDTO> items, int page, boolean hasMore) {
}
