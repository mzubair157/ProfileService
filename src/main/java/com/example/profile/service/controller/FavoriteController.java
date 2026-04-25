package com.example.profile.service.controller;

import com.example.profile.service.dto.FavoriteDTO;
import com.example.profile.service.dto.FavoriteItemDTO;
import com.example.profile.service.service.FavoriteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/favorites", "/api/v1/favorites"})
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addFavorite(@Valid @RequestBody FavoriteCommand request) {
        favoriteService.addFavorite(request.userId(), request.productId(), "legacy-body-add");
        return ResponseEntity.ok(Map.of("message", "Favorite added"));
    }

    @PostMapping("/{userId}/{productId}")
    public ResponseEntity<FavoriteItemDTO> addFavorite(@PathVariable Long userId,
                                                       @PathVariable Long productId,
                                                       @RequestHeader("Idempotency-Key") String idempotencyKey) {
        return ResponseEntity.ok(favoriteService.addFavorite(userId, productId, idempotencyKey));
    }

    @GetMapping("/{userId}")
    public FavoriteDTO getFavorites(@PathVariable Long userId) {
        return favoriteService.getFavorites(userId);
    }

    @PutMapping("/{userId}/{productId}")
    public ResponseEntity<FavoriteItemDTO> updateFavorite(@PathVariable Long userId,
                                                          @PathVariable Long productId,
                                                          @RequestHeader("Idempotency-Key") String idempotencyKey,
                                                          @Valid @RequestBody FavoriteMetadataCommand request) {
        return ResponseEntity.ok(
                favoriteService.updateFavorite(userId, productId, request.note(), request.priorityLevel(), idempotencyKey)
        );
    }

    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long userId,
                                               @PathVariable Long productId,
                                               @RequestHeader("Idempotency-Key") String idempotencyKey) {
        favoriteService.removeFavorite(userId, productId, idempotencyKey);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public record FavoriteCommand(@NotNull Long userId, @NotNull Long productId) {
    }

    public record FavoriteMetadataCommand(
            @Size(max = 180) String note,
            @Min(1) @Max(5) Integer priorityLevel
    ) {
    }
}
