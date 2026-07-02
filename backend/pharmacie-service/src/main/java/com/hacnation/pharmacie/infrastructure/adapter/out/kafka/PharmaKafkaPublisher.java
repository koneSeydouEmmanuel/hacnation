package com.hacnation.pharmacie.infrastructure.adapter.out.kafka;

import com.hacnation.common.events.NotificationEvent;
import com.hacnation.common.events.PharmacyEvent;
import com.hacnation.pharmacie.domain.port.PharmacyEventPublisherPort;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PharmaKafkaPublisher implements PharmacyEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(PharmaKafkaPublisher.class);
    private static final String PHARMACIE_TOPIC = "sic.pharmacie";
    private static final String NOTIFICATION_TOPIC = "notification-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PharmaKafkaPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPharmacyEvent(PharmacyEvent event) {
        event.setEventId(UUID.randomUUID().toString());
        kafkaTemplate.send(PHARMACIE_TOPIC, event.getOrdonnanceId(), event);
        log.info("PharmacyEvent publie: type={}, ordonnanceId={}", event.getEventType(), event.getOrdonnanceId());
    }

    public void publishNotificationEvent(NotificationEvent event) {
        event.setEventId(UUID.randomUUID().toString());
        kafkaTemplate.send(NOTIFICATION_TOPIC, event.getPatientId(), event);
        log.info("NotificationEvent publie: type={}, patientId={}", event.getEventType(), event.getPatientId());
    }
}
