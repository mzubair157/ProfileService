package com.example.profile.service.repository;

import com.example.profile.service.domain.LoyaltyAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoyaltyRepository extends JpaRepository<LoyaltyAccount, Long> {
}
