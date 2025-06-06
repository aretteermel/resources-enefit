package org.backend.resourcesenefit.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);

    @KafkaListener(topics = "resource-updates", groupId = "notification-group")
    public void listen(String message) {
        logger.info("📥 Received Kafka notification: {}", message);
    }
}
