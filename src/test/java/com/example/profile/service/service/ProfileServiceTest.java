package com.example.profile.service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.profile.service.domain.UserProfile;
import com.example.profile.service.exception.ProfileNotFoundException;
import com.example.profile.service.repository.ProfileRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Test
    void getProfileReturnsProfileWhenFound() {
        UserProfile profile = new UserProfile();
        profile.setId(7L);
        profile.setUsername("ada");

        when(profileRepository.findById(7L)).thenReturn(Optional.of(profile));

        ProfileServiceImpl service = new ProfileServiceImpl(profileRepository);
        UserProfile result = service.getProfile(7L);

        assertThat(result.getUsername()).isEqualTo("ada");
        verify(profileRepository, times(1)).findById(7L);
    }

    @Test
    void getProfileThrowsWhenMissing() {
        when(profileRepository.findById(99L)).thenReturn(Optional.empty());

        ProfileServiceImpl service = new ProfileServiceImpl(profileRepository);

        assertThatThrownBy(() -> service.getProfile(99L))
                .isInstanceOf(ProfileNotFoundException.class)
                .hasMessage("User not found: 99");
    }
}
