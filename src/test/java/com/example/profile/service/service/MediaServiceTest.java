package com.example.profile.service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.profile.service.domain.MediaMetadata;
import com.example.profile.service.dto.MediaDTO;
import com.example.profile.service.repository.MediaRepository;
import java.util.concurrent.Executor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MediaServiceTest {

    @Mock
    private MediaRepository mediaRepository;

    @Test
    void processProfileImagePersistsMetadataAndReturnsDownloadUrl() {
        Executor directExecutor = Runnable::run;
        MediaServiceImpl service = new MediaServiceImpl(mediaRepository, directExecutor);

        when(mediaRepository.save(any(MediaMetadata.class))).thenAnswer(invocation -> {
            MediaMetadata media = invocation.getArgument(0);
            media.setId(12L);
            return media;
        });

        MediaDTO result = service.processProfileImage(7L, "avatar.png", "image/png", new byte[]{1, 2, 3}).join();

        assertThat(result.id()).isEqualTo(12L);
        assertThat(result.userId()).isEqualTo(7L);
        assertThat(result.downloadUrl()).isEqualTo("/media/12");
        verify(mediaRepository).save(any(MediaMetadata.class));
    }
}
