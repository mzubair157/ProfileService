package com.example.profile.service.repository;

import com.example.profile.service.domain.UserFavorite;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavoriteRepository extends JpaRepository<UserFavorite, Long> {

    boolean existsByUserIdAndProductId(Long userId, Long productId);

    List<UserFavorite> findAllByUserIdOrderByProductIdAsc(Long userId);

    long countByUserId(Long userId);

    java.util.Optional<UserFavorite> findByUserIdAndProductId(Long userId, Long productId);

    @Modifying
    @Query("""
            update UserFavorite uf
               set uf.note = :note,
                   uf.priorityLevel = :priorityLevel
             where uf.userId = :userId
               and uf.productId = :productId
            """)
    int updateMetadata(@Param("userId") Long userId,
                       @Param("productId") Long productId,
                       @Param("note") String note,
                       @Param("priorityLevel") Integer priorityLevel);

    @Modifying
    @Query("""
            delete from UserFavorite uf
             where uf.userId = :userId
               and uf.productId = :productId
            """)
    int deleteByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
}
