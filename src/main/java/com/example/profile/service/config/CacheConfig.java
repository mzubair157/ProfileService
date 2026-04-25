package com.example.profile.service.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.EnableCaching;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(java.util.List.of(
                buildCache("profiles", 20_000, Duration.ofMinutes(15)),
                buildCache("loyalty", 30_000, Duration.ofMinutes(5)),
                buildCache("discounts", 50_000, Duration.ofSeconds(45)),
                buildCache("favorites", 50_000, Duration.ofMinutes(3))
        ));
        return cacheManager;
    }

    private CaffeineCache buildCache(String name, long maximumSize, Duration ttl) {
        return new CaffeineCache(name, Caffeine.newBuilder()
                .initialCapacity(1_000)
                .maximumSize(maximumSize)
                .expireAfterWrite(ttl)
                .recordStats()
                .build());
    }
}
