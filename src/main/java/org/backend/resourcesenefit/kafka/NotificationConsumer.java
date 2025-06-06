package org.backend.resourcesenefit.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SimpleKafkaConsumer {

    @KafkaListener(topics = "simple-topic", groupId = "test-group")
    public void listen(String message) {
        System.out.println("📥 Received Kafka message: " + message);
    }
}
