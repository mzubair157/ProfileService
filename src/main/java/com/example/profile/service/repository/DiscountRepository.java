package com.example.profile.service.repository;

import com.example.profile.service.domain.Discount;
import com.example.profile.service.dto.DiscountDTO;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    @Query("""
            select new com.example.profile.service.dto.DiscountDTO(
                d.code,
                d.percentage,
                count(u.id)
            )
            from Discount d
            left join d.eligibleUsers u
            where d.active = true
              and (d.expiryDate is null or d.expiryDate >= :now)
            group by d.id, d.code, d.percentage
            order by d.code
            """)
    List<DiscountDTO> findActiveDiscountSummaries(@Param("now") LocalDateTime now);
}
