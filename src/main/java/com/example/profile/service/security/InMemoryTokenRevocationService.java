package com.example.profile.service.security;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class InMemoryTokenRevocationService implements TokenRevocationService {

    private final Set<String> revokedTokenIds = ConcurrentHashMap.newKeySet();

    @Override
    public boolean isRevoked(String tokenId) {
        return tokenId != null && revokedTokenIds.contains(tokenId);
    }

    public void revoke(String tokenId) {
        if (tokenId != null && !tokenId.isBlank()) {
            revokedTokenIds.add(tokenId);
        }
    }
}
