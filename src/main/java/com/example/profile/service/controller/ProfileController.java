package com.example.profile.service.controller;

import com.example.profile.service.domain.LoyaltyAccount;
import com.example.profile.service.domain.UserProfile;
import com.example.profile.service.dto.ProfileResponse;
import com.example.profile.service.service.ProfileService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/profiles", "/api/v1/profiles"})
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{id}")
    public ProfileResponse getProfile(@PathVariable Long id) {
        return toResponse(profileService.getProfile(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponse> updateProfile(@PathVariable Long id,
                                                         @Valid @RequestBody ProfileUpdateRequest request) {
        return ResponseEntity.ok(toResponse(profileService.updateProfile(id, request.username(), request.email())));
    }

    private ProfileResponse toResponse(UserProfile profile) {
        LoyaltyAccount loyaltyAccount = profile.getLoyaltyAccount();
        return new ProfileResponse(
                profile.getId(),
                profile.getUsername(),
                profile.getEmail(),
                profile.getRegion(),
                loyaltyAccount != null ? loyaltyAccount.getId() : null,
                loyaltyAccount != null ? loyaltyAccount.getBalance() : null
        );
    }

    public record ProfileUpdateRequest(@NotBlank String username, @NotBlank String email) {
    }
}
