package com.example.profile.service;

import org.junit.jupiter.api.Test;
import org.springframework.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class RedisCacheMockTest extends IntegrationTestBase {

    @Autowired
    private CacheManager cacheManager;

    @Test
    void cacheManagerExposesNamedCaches() {
        assertThat(cacheManager.getCacheNames())
                .contains("profiles", "loyalty", "discounts", "favorites");
    }
}
