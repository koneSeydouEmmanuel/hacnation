package com.hacnation.urgences.infrastructure.adapter.out.kafka;

import com.hacnation.common.events.NotificationEvent;
import com.hacnation.urgences.domain.port.NotificationEventPublisherPort;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UrgencesKafkaPublisher implements NotificationEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(UrgencesKafkaPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public UrgencesKafkaPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishNotificationEvent(NotificationEvent event) {
        event.setEventId(UUID.randomUUID().toString());
        kafkaTemplate.send("sic.notification", event.getPatientId(), event);
        log.info("NotificationEvent publie sur sic.notification: type={}, patientId={}", event.getEventType(), event.getPatientId());
    }
}
