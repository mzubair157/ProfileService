package com.example.profile.service.service;

import com.example.profile.service.domain.UserProfile;
import com.example.profile.service.exception.InvalidRequestException;
import com.example.profile.service.exception.ProfileNotFoundException;
import com.example.profile.service.repository.ProfileRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository repository;

    public ProfileServiceImpl(ProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "profiles", key = "#id")
    public UserProfile getProfile(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException("User not found: " + id));
    }

    @Override
    @Transactional
    @CacheEvict(value = "profiles", key = "#id")
    public UserProfile updateProfile(Long id, String username, String email) {
        if (username == null || username.isBlank()) {
            throw new InvalidRequestException("username must not be blank");
        }
        if (email == null || email.isBlank()) {
            throw new InvalidRequestException("email must not be blank");
        }

        UserProfile profile = repository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException("User not found: " + id));
        profile.setUsername(username.trim());
        profile.setEmail(email.trim());
        return repository.save(profile);
    }
}
