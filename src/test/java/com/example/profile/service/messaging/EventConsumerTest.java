package com.example.profile.service.messaging;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;

class EventConsumerTest {

    @Test
    void poisonPillEventIsMovedToDeadLetterList() {
        OrderEventConsumer consumer = new OrderEventConsumer();
        EventPublisher publisher = new EventPublisher(consumer);

        CompletableFuture<Void> result = publisher.publishOrder(new OrderEvent(-99L, 1L));
        result.join();

        assertThat(consumer.getDeadLetterEvents()).hasSize(1);
        assertThat(consumer.getDeadLetterEvents().getFirst().orderId()).isEqualTo(-99L);
        assertThat(consumer.getProcessedOrderIds()).isEmpty();
    }

    @Test
    void validEventIsProcessed() {
        OrderEventConsumer consumer = new OrderEventConsumer();
        EventPublisher publisher = new EventPublisher(consumer);

        publisher.publishOrder(new OrderEvent(42L, 1L)).join();

        assertThat(consumer.getProcessedOrderIds()).containsExactly(42L);
        assertThat(consumer.getDeadLetterEvents()).isEmpty();
    }
}
