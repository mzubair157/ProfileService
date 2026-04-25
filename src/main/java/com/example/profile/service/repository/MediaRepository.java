package com.example.profile.service.repository;

import com.example.profile.service.domain.MediaMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<MediaMetadata, Long> {
}
