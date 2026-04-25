package com.example.profile.service.controller;

import com.example.profile.service.domain.AuditEvent;
import com.example.profile.service.service.AuditService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/audit")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping("/users/{userId}")
    public Page<AuditEvent> getAuditTrail(@PathVariable Long userId,
                                          @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        return auditService.getEventsForUser(userId, pageSize);
    }
}
