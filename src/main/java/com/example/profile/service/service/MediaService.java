package com.example.profile.service.service;

import com.example.profile.service.domain.MediaMetadata;
import com.example.profile.service.dto.MediaDTO;
import java.util.concurrent.CompletableFuture;

public interface MediaService {

    CompletableFuture<MediaDTO> processProfileImage(Long userId, String fileName, String contentType, byte[] rawData);

    MediaMetadata getMedia(Long mediaId);
}
