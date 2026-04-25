package com.example.profile.service.service;

import com.example.profile.service.domain.UserProfile;

public interface ProfileService {

    UserProfile getProfile(Long id);

    UserProfile updateProfile(Long id, String username, String email);
}
