package com.example.profile.service.service;

import com.example.profile.service.exception.InvalidRequestException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    private final Executor securityWorkExecutor;

    public SecurityServiceImpl(@Qualifier("securityWorkExecutor") Executor securityWorkExecutor) {
        this.securityWorkExecutor = securityWorkExecutor;
    }

    @Override
    public boolean verifyPassword(String raw, String hashed) {
        if (raw == null || raw.isBlank() || hashed == null || hashed.isBlank()) {
            throw new InvalidRequestException("Both raw and hashed passwords are required");
        }

        return CompletableFuture.supplyAsync(() -> BCrypt.checkpw(raw, hashed), securityWorkExecutor).join();
    }
}
