package com.example.profile.service.service;

import com.example.profile.service.domain.AuditEvent;
import org.springframework.data.domain.Page;

public interface AuditService {

    Page<AuditEvent> getEventsForUser(Long userId, Integer pageSize);
}
