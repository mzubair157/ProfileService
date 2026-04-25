package com.example.profile.service.controller;

import com.example.profile.service.domain.MediaMetadata;
import com.example.profile.service.dto.MediaDTO;
import com.example.profile.service.service.MediaService;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/media")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping("/profiles/{userId}")
    public CompletableFuture<ResponseEntity<MediaDTO>> uploadProfileImage(@PathVariable Long userId,
                                                                          @RequestParam("file") MultipartFile file) {
        try {
            return mediaService.processProfileImage(userId, file.getOriginalFilename(), file.getContentType(),
                            file.getBytes())
                    .thenApply(ResponseEntity::ok);
        } catch (Exception ex) {
            CompletableFuture<ResponseEntity<MediaDTO>> failed = new CompletableFuture<>();
            failed.completeExceptionally(ex);
            return failed;
        }
    }

    @GetMapping("/{mediaId}")
    public ResponseEntity<byte[]> getMedia(@PathVariable Long mediaId,
                                           @RequestParam(name = "download", defaultValue = "false") boolean download,
                                           @RequestHeader(name = HttpHeaders.IF_NONE_MATCH, required = false)
                                           String ifNoneMatch) {
        MediaMetadata media = mediaService.getMedia(mediaId);
        String etag = "\"" + media.getEtag() + "\"";
        if (etag.equals(ifNoneMatch)) {
            return ResponseEntity.status(304)
                    .eTag(etag)
                    .cacheControl(CacheControl.maxAge(Duration.ofHours(12)).cachePublic())
                    .build();
        }

        ResponseEntity.BodyBuilder response = ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(media.getContentType()))
                .contentLength(media.getSizeBytes())
                .cacheControl(CacheControl.maxAge(Duration.ofHours(12)).cachePublic())
                .eTag(etag)
                .header(HttpHeaders.VARY, HttpHeaders.IF_NONE_MATCH);
        if (download) {
            response.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + media.getFileName() + "\"");
        }
        return response.body(media.getData());
    }
}
