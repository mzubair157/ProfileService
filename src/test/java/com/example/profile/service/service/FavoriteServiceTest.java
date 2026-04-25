package com.example.profile.service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.profile.service.domain.UserFavorite;
import com.example.profile.service.dto.FavoriteDTO;
import com.example.profile.service.dto.FavoriteItemDTO;
import com.example.profile.service.exception.InvalidRequestException;
import com.example.profile.service.repository.FavoriteRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Test
    void addFavoriteSkipsInsertWhenFavoriteAlreadyExists() {
        UserFavorite existing = new UserFavorite(10L, 20L);
        existing.setPriorityLevel(2);
        when(favoriteRepository.findByUserIdAndProductId(10L, 20L)).thenReturn(Optional.of(existing));

        FavoriteServiceImpl service = new FavoriteServiceImpl(favoriteRepository);
        FavoriteItemDTO result = service.addFavorite(10L, 20L, "favorite-op-1");

        verify(favoriteRepository, never()).save(org.mockito.ArgumentMatchers.any(UserFavorite.class));
        assertThat(result.productId()).isEqualTo(20L);
        assertThat(result.priorityLevel()).isEqualTo(2);
    }

    @Test
    void addFavoriteRejectsWhenFavoriteLimitIsExceeded() {
        when(favoriteRepository.findByUserIdAndProductId(10L, 20L)).thenReturn(Optional.empty());
        when(favoriteRepository.countByUserId(10L)).thenReturn(500L);

        FavoriteServiceImpl service = new FavoriteServiceImpl(favoriteRepository);

        assertThatThrownBy(() -> service.addFavorite(10L, 20L, "favorite-op-2"))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("Favorite limit exceeded");
    }

    @Test
    void getFavoritesReturnsSortedProductIdsAndMetadata() {
        UserFavorite first = new UserFavorite(7L, 11L);
        first.setNote("Track for summer");
        first.setPriorityLevel(5);
        UserFavorite second = new UserFavorite(7L, 19L);
        second.setPriorityLevel(2);

        when(favoriteRepository.findAllByUserIdOrderByProductIdAsc(7L)).thenReturn(List.of(first, second));

        FavoriteServiceImpl service = new FavoriteServiceImpl(favoriteRepository);
        FavoriteDTO result = service.getFavorites(7L);

        assertThat(result.userId()).isEqualTo(7L);
        assertThat(result.productIds()).containsExactly(11L, 19L);
        assertThat(result.items()).hasSize(2);
        assertThat(result.items().getFirst().note()).isEqualTo("Track for summer");
        assertThat(result.items().getFirst().priorityLevel()).isEqualTo(5);
    }
}
