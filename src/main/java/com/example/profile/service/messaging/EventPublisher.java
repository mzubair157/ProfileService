package com.example.profile.service.messaging;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(EventPublisher.class);

    private final OrderEventConsumer consumer;
    private final Executor executor;

    public EventPublisher(OrderEventConsumer consumer) {
        this.consumer = consumer;
        this.executor = ForkJoinPool.commonPool();
    }

    public CompletableFuture<Void> publishOrder(OrderEvent event) {
        return CompletableFuture.runAsync(() -> consumer.onMessage(event), executor)
                .exceptionally(ex -> {
                    log.error("Failed to publish order event {}", event.orderId(), ex);
                    return null;
                });
    }
}
