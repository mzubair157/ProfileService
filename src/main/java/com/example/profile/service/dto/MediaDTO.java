package com.example.profile.service.dto;

public record MediaDTO(
        Long id,
        Long userId,
        String fileName,
        String contentType,
        Long sizeBytes,
        String downloadUrl
) {
}
