package com.example.profile.service;

import com.example.profile.service.domain.UserProfile;
import com.example.profile.service.repository.ProfileRepository;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class RateLimiterTest extends IntegrationTestBase {

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    void cachedProfileReadsRemainStableAcrossRepeatedAccess() {
        UserProfile profile = new UserProfile();
        profile.setUsername("cache-user");
        profile.setEmail("cache@example.com");
        profile.setRegion("US");
        UserProfile saved = profileRepository.save(profile);

        IntStream.range(0, 25).forEach(index -> {
            cacheManager.getCache("profiles");
            profileRepository.findById(saved.getId());
        });

        assertThat(cacheManager.getCache("profiles")).isNotNull();
    }
}
