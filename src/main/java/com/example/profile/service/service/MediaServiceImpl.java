package com.example.profile.service.service;

import com.example.profile.service.domain.MediaMetadata;
import com.example.profile.service.dto.MediaDTO;
import com.example.profile.service.exception.ResourceNotFoundException;
import com.example.profile.service.repository.MediaRepository;
import com.example.profile.service.util.DataValidationUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final Executor mediaProcessingExecutor;

    public MediaServiceImpl(MediaRepository mediaRepository,
                            @Qualifier("mediaProcessingExecutor") Executor mediaProcessingExecutor) {
        this.mediaRepository = mediaRepository;
        this.mediaProcessingExecutor = mediaProcessingExecutor;
    }

    @Override
    public CompletableFuture<MediaDTO> processProfileImage(Long userId,
                                                           String fileName,
                                                           String contentType,
                                                           byte[] rawData) {
        DataValidationUtils.requirePositiveId(userId, "userId");
        if (rawData == null || rawData.length == 0) {
            throw new IllegalArgumentException("rawData must not be empty");
        }

        return CompletableFuture.supplyAsync(() -> saveProcessedImage(userId, fileName, contentType, rawData),
                mediaProcessingExecutor);
    }

    @Override
    @Transactional(readOnly = true)
    public MediaMetadata getMedia(Long mediaId) {
        return mediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found: " + mediaId));
    }

    @Transactional
    protected MediaDTO saveProcessedImage(Long userId, String fileName, String contentType, byte[] rawData) {
        byte[] processed = rawData.clone();

        MediaMetadata media = new MediaMetadata();
        media.setUserId(userId);
        media.setFileName(fileName == null || fileName.isBlank() ? "profile-image.bin" : fileName);
        media.setContentType(contentType == null || contentType.isBlank() ? "application/octet-stream" : contentType);
        media.setSizeBytes((long) processed.length);
        media.setEtag(calculateEtag(processed));
        media.setData(processed);

        MediaMetadata saved = mediaRepository.save(media);
        return new MediaDTO(
                saved.getId(),
                saved.getUserId(),
                saved.getFileName(),
                saved.getContentType(),
                saved.getSizeBytes(),
                "/media/" + saved.getId()
        );
    }

    private String calculateEtag(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(data));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is not available", ex);
        }
    }
}
