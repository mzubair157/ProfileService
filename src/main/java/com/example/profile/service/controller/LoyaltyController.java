package com.example.profile.service.controller;

import com.example.profile.service.dto.PointsRequest;
import com.example.profile.service.service.LoyaltyService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loyalty")
public class LoyaltyController {

    private final LoyaltyService loyaltyService;

    public LoyaltyController(LoyaltyService loyaltyService) {
        this.loyaltyService = loyaltyService;
    }

    @PostMapping("/{accountId}/points")
    public ResponseEntity<Map<String, String>> adjustPoints(@PathVariable Long accountId,
                                                            @RequestHeader("Idempotency-Key") String idempotencyKey,
                                                            @Valid @RequestBody PointsRequest request) {
        loyaltyService.adjustPoints(accountId, request.delta(), idempotencyKey);
        return ResponseEntity.ok(Map.of(
                "message", "Points updated",
                "idempotencyKey", idempotencyKey
        ));
    }
}
