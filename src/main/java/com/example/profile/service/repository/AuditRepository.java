package com.example.profile.service.repository;

import com.example.profile.service.domain.AuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<AuditEvent, Long> {

    Page<AuditEvent> findByUserIdOrderByOccurredAtDesc(Long userId, Pageable pageable);
}
