package org.backend.resourcesenefit.kafka;

import org.backend.resourcesenefit.dto.ResourceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

import static org.mockito.Mockito.*;

class NotificationProducerTest {

    @Mock
    private KafkaTemplate<String, ResourceDto> kafkaTemplate;

    @InjectMocks
    private NotificationProducer producer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendNotification_shouldCallKafkaTemplate() {
        ResourceDto dto = new ResourceDto();
        dto.setId(1L);

        producer.sendNotification(dto);

        verify(kafkaTemplate).send("resource-updates", dto);
    }

    @Test
    void sendAllResources_shouldCallKafkaTemplateForEach() {
        ResourceDto dto1 = new ResourceDto();
        dto1.setId(1L);
        ResourceDto dto2 = new ResourceDto();
        dto2.setId(2L);

        List<ResourceDto> resources = List.of(dto1, dto2);

        producer.sendAllResources(resources);

        verify(kafkaTemplate, times(1)).send("resource-updates", dto1);
        verify(kafkaTemplate, times(1)).send("resource-updates", dto2);
    }
}
