package com.example.profile.service.repository;

import com.example.profile.service.domain.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<UserProfile, Long> {

    @Override
    @EntityGraph(attributePaths = "loyaltyAccount")
    java.util.Optional<UserProfile> findById(Long id);
}
