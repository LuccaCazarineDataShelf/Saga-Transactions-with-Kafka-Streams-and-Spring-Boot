package org.example.kafka;

import org.example.eventes.SagaEventInfo;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventProducer {
    private static final String ORDER_EVENT_TOPIC = "orderEvent";

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderCreated(SagaEventInfo info){
        kafkaTemplate.send(
                ORDER_EVENT_TOPIC,
                info.getEventId(),
                info.toByteArray()
        );
    }
}
