package com.example.profile.service.security;

public interface TokenRevocationService {

    boolean isRevoked(String tokenId);
}
