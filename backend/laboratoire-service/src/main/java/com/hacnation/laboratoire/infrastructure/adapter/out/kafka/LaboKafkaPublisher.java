package com.hacnation.laboratoire.infrastructure.adapter.out.kafka;

import com.hacnation.common.events.LabEvent;
import com.hacnation.common.events.NotificationEvent;
import com.hacnation.laboratoire.domain.port.LaboEventPublisherPort;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class LaboKafkaPublisher implements LaboEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(LaboKafkaPublisher.class);
    private static final String LABO_TOPIC = "sic.laboratoire";
    private static final String NOTIFICATION_TOPIC = "notification-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public LaboKafkaPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishLabEvent(LabEvent event) {
        event.setEventId(UUID.randomUUID().toString());
        kafkaTemplate.send(LABO_TOPIC, event.getAnalyseId(), event);
        log.info("LabEvent publie: type={}, analyseId={}", event.getEventType(), event.getAnalyseId());
    }

    public void publishNotificationEvent(NotificationEvent event) {
        event.setEventId(UUID.randomUUID().toString());
        kafkaTemplate.send(NOTIFICATION_TOPIC, event.getPatientId(), event);
        log.info("NotificationEvent publie: type={}, patientId={}", event.getEventType(), event.getPatientId());
    }
}
