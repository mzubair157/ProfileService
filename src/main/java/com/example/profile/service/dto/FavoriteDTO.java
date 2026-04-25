package com.example.profile.service.dto;

import java.util.List;

public record FavoriteDTO(Long userId, List<Long> productIds, List<FavoriteItemDTO> items) {
}
