package org.backend.resourcesenefit.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SimpleKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedRate = 5000) // Every 5 seconds
    public void sendSimpleMessage() {
        kafkaTemplate.send("simple-topic", "Hello from Kafka @ " + System.currentTimeMillis());
        System.out.println("📤 Sent simple Kafka message.");
    }
}
