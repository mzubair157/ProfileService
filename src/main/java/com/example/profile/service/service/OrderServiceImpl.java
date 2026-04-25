package com.example.profile.service.service;

import com.example.profile.service.domain.UserProfile;
import com.example.profile.service.dto.CreateOrderRequest;
import com.example.profile.service.dto.CreateOrderResponse;
import com.example.profile.service.dto.OrderHistoryItemDTO;
import com.example.profile.service.dto.OrderHistoryResponseDTO;
import com.example.profile.service.messaging.EventPublisher;
import com.example.profile.service.messaging.OrderEvent;
import com.example.profile.service.repository.ProfileRepository;
import com.example.profile.service.util.DataValidationUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final AtomicLong nextOrderId = new AtomicLong(1000);
    private final Map<Long, CopyOnWriteArrayList<OrderHistoryItemDTO>> ordersByUser = new ConcurrentHashMap<>();
    private final ProfileRepository profileRepository;
    private final LoyaltyService loyaltyService;
    private final EventPublisher eventPublisher;

    public OrderServiceImpl(ProfileRepository profileRepository,
                            LoyaltyService loyaltyService,
                            EventPublisher eventPublisher) {
        this.profileRepository = profileRepository;
        this.loyaltyService = loyaltyService;
        this.eventPublisher = eventPublisher;
        seedHistory();
    }

    @Override
    public OrderHistoryResponseDTO getOrderHistory(Long userId, int page, int size) {
        DataValidationUtils.requirePositiveId(userId, "userId");
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(size, 1);
        List<OrderHistoryItemDTO> orders = ordersByUser.getOrDefault(userId, new CopyOnWriteArrayList<>());
        int fromIndex = safePage * safeSize;
        if (fromIndex >= orders.size()) {
            return new OrderHistoryResponseDTO(List.of(), safePage, false);
        }

        int toIndex = Math.min(fromIndex + safeSize, orders.size());
        return new OrderHistoryResponseDTO(orders.subList(fromIndex, toIndex), safePage, toIndex < orders.size());
    }

    @Override
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        DataValidationUtils.requirePositiveId(request.userId(), "userId");
        UserProfile profile = profileRepository.findById(request.userId()).orElseThrow();

        long orderId = nextOrderId.incrementAndGet();
        Instant now = Instant.now();
        OrderHistoryItemDTO createdOrder = new OrderHistoryItemDTO(
                orderId,
                now,
                request.amount().setScale(2, RoundingMode.HALF_UP)
        );
        ordersByUser.computeIfAbsent(request.userId(), ignored -> new CopyOnWriteArrayList<>()).add(0, createdOrder);

        long pointsAwarded = Math.max(1L, request.amount().setScale(0, RoundingMode.HALF_UP).longValue());
        Long loyaltyBalance = null;
        if (profile.getLoyaltyAccount() != null) {
            Long loyaltyAccountId = profile.getLoyaltyAccount().getId();
            loyaltyService.adjustPoints(loyaltyAccountId, pointsAwarded, "order-" + orderId);
            loyaltyBalance = profileRepository.findById(request.userId())
                    .map(UserProfile::getLoyaltyAccount)
                    .map(account -> account != null ? account.getBalance() : null)
                    .orElse(null);
        }

        eventPublisher.publishOrder(new OrderEvent(orderId, request.userId()));
        return new CreateOrderResponse(createdOrder, loyaltyBalance, pointsAwarded);
    }

    private void seedHistory() {
        CopyOnWriteArrayList<OrderHistoryItemDTO> seeded = new CopyOnWriteArrayList<>();
        Instant base = Instant.parse("2026-04-24T12:00:00Z");
        for (int index = 0; index < 18; index++) {
            long orderId = nextOrderId.incrementAndGet();
            seeded.add(new OrderHistoryItemDTO(
                    orderId,
                    base.minusSeconds(index * 3600L),
                    BigDecimal.valueOf(18 + index).setScale(2, RoundingMode.HALF_UP)
            ));
        }
        seeded.sort(Comparator.comparing(OrderHistoryItemDTO::date).reversed());
        ordersByUser.put(1L, seeded);
    }
}
