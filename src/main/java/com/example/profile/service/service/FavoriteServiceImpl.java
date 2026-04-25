package com.example.profile.service.service;

import com.example.profile.service.domain.UserFavorite;
import com.example.profile.service.dto.FavoriteDTO;
import com.example.profile.service.dto.FavoriteItemDTO;
import com.example.profile.service.exception.InvalidRequestException;
import com.example.profile.service.exception.ResourceNotFoundException;
import com.example.profile.service.repository.FavoriteRepository;
import com.example.profile.service.util.DataValidationUtils;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private static final long MAX_FAVORITES_PER_USER = 500L;
    private final FavoriteRepository favoriteRepository;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    @Transactional
    @CacheEvict(value = "favorites", key = "#userId")
    public FavoriteItemDTO addFavorite(Long userId, Long productId, String idempotencyKey) {
        DataValidationUtils.requirePositiveId(userId, "userId");
        DataValidationUtils.requirePositiveId(productId, "productId");
        DataValidationUtils.requireNonBlank(idempotencyKey, "idempotencyKey");

        return favoriteRepository.findByUserIdAndProductId(userId, productId)
                .map(this::toItem)
                .orElseGet(() -> {
                    if (favoriteRepository.countByUserId(userId) >= MAX_FAVORITES_PER_USER) {
                        throw new InvalidRequestException("Favorite limit exceeded for user " + userId);
                    }

                    UserFavorite favorite = new UserFavorite(userId, productId);
                    favorite.setPriorityLevel(3);
                    return toItem(favoriteRepository.save(favorite));
                });
    }

    @Override
    @Transactional
    @CacheEvict(value = "favorites", key = "#userId")
    public FavoriteItemDTO updateFavorite(Long userId,
                                          Long productId,
                                          String note,
                                          Integer priorityLevel,
                                          String idempotencyKey) {
        DataValidationUtils.requirePositiveId(userId, "userId");
        DataValidationUtils.requirePositiveId(productId, "productId");
        DataValidationUtils.requireNonBlank(idempotencyKey, "idempotencyKey");
        validateMetadata(note, priorityLevel);

        int updated = favoriteRepository.updateMetadata(userId, productId, normalizeNote(note), priorityLevel);
        if (updated == 0) {
            throw new ResourceNotFoundException("Favorite not found for user " + userId + " and product " + productId);
        }

        return favoriteRepository.findByUserIdAndProductId(userId, productId)
                .map(this::toItem)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Favorite not found for user " + userId + " and product " + productId));
    }

    @Override
    @Transactional
    @CacheEvict(value = "favorites", key = "#userId")
    public void removeFavorite(Long userId, Long productId, String idempotencyKey) {
        DataValidationUtils.requirePositiveId(userId, "userId");
        DataValidationUtils.requirePositiveId(productId, "productId");
        DataValidationUtils.requireNonBlank(idempotencyKey, "idempotencyKey");
        favoriteRepository.deleteByUserIdAndProductId(userId, productId);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "favorites", key = "#userId")
    public FavoriteDTO getFavorites(Long userId) {
        DataValidationUtils.requirePositiveId(userId, "userId");
        List<UserFavorite> favorites = favoriteRepository.findAllByUserIdOrderByProductIdAsc(userId);
        List<Long> productIds = favorites.stream()
                .map(UserFavorite::getProductId)
                .toList();
        List<FavoriteItemDTO> items = favorites.stream()
                .map(this::toItem)
                .toList();
        return new FavoriteDTO(userId, productIds, items);
    }

    private FavoriteItemDTO toItem(UserFavorite favorite) {
        return new FavoriteItemDTO(favorite.getProductId(), favorite.getNote(), favorite.getPriorityLevel());
    }

    private void validateMetadata(String note, Integer priorityLevel) {
        if (note != null && note.length() > 180) {
            throw new InvalidRequestException("note must be 180 characters or fewer");
        }
        if (priorityLevel != null && (priorityLevel < 1 || priorityLevel > 5)) {
            throw new InvalidRequestException("priorityLevel must be between 1 and 5");
        }
    }

    private String normalizeNote(String note) {
        if (note == null) {
            return null;
        }
        String trimmed = note.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
