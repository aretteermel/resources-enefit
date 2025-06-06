package org.backend.resourcesenefit.kafka;


import org.backend.resourcesenefit.dto.ResourceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationProducer {

    private final KafkaTemplate<String, ResourceDto> kafkaTemplate;
    private static final Logger logger = LoggerFactory.getLogger(NotificationProducer.class);
    private static final String TOPIC = "resource-updates";

    public NotificationProducer(KafkaTemplate<String, ResourceDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(ResourceDto resourceDto) {
        kafkaTemplate.send(TOPIC, resourceDto);
        logger.info("📤 Sent Kafka notification: Resource with ID {} updated", resourceDto.getId());
    }

    public void sendAllResources(List<ResourceDto> resources) {
        for (ResourceDto dto : resources) {
            kafkaTemplate.send(TOPIC, dto);
            logger.info("📤 Sent Resource with ID {} to Kafka", dto.getId());
        }
    }

}
