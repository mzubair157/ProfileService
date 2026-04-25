package com.example.profile.service.repository;

import com.example.profile.service.domain.LoyaltyTransaction;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoyaltyTransactionRepository extends JpaRepository<LoyaltyTransaction, Long> {

    Optional<LoyaltyTransaction> findByAccountIdAndIdempotencyKey(Long accountId, String idempotencyKey);
}
