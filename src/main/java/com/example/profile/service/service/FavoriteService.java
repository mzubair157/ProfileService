package com.example.profile.service.service;

import com.example.profile.service.dto.FavoriteDTO;
import com.example.profile.service.dto.FavoriteItemDTO;

public interface FavoriteService {

    FavoriteItemDTO addFavorite(Long userId, Long productId, String idempotencyKey);

    FavoriteItemDTO updateFavorite(Long userId, Long productId, String note, Integer priorityLevel, String idempotencyKey);

    void removeFavorite(Long userId, Long productId, String idempotencyKey);

    FavoriteDTO getFavorites(Long userId);
}
