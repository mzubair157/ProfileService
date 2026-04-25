package com.example.profile.service.messaging;

import com.example.profile.service.exception.MessagingException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);

    private final List<OrderEvent> deadLetterEvents = new CopyOnWriteArrayList<>();
    private final List<Long> processedOrderIds = new CopyOnWriteArrayList<>();

    public void onMessage(OrderEvent event) {
        if (event.orderId() < 0) {
            deadLetterEvents.add(event);
            throw new MessagingException("Rejected order event and routed to dead-letter queue: " + event.orderId());
        }

        log.info("Processing order event: {}", event.orderId());
        processedOrderIds.add(event.orderId());
        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            log.warn("Order event processing interrupted for {}", event.orderId());
        }
    }

    public List<OrderEvent> getDeadLetterEvents() {
        return List.copyOf(deadLetterEvents);
    }

    public List<Long> getProcessedOrderIds() {
        return List.copyOf(processedOrderIds);
    }
}
