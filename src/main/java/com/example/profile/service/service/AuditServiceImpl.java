package com.example.profile.service.service;

import com.example.profile.service.domain.AuditEvent;
import com.example.profile.service.repository.AuditRepository;
import com.example.profile.service.util.DataValidationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    public AuditServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditEvent> getEventsForUser(Long userId, Integer pageSize) {
        DataValidationUtils.requirePositiveId(userId, "userId");
        int normalizedPageSize = DataValidationUtils.normalizePageSize(pageSize);
        return auditRepository.findByUserIdOrderByOccurredAtDesc(userId, PageRequest.of(0, normalizedPageSize));
    }
}
