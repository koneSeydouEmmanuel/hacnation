package com.hacnation.prescription.infrastructure.adapter.out.kafka;

import com.hacnation.common.events.PrescriptionEvent;
import com.hacnation.prescription.domain.port.PrescriptionEventPublisherPort;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionKafkaPublisher implements PrescriptionEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(PrescriptionKafkaPublisher.class);
    private static final String PRESCRIPTION_TOPIC = "sic.prescription";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PrescriptionKafkaPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPrescriptionEvent(PrescriptionEvent event) {
        event.setEventId(UUID.randomUUID().toString());
        kafkaTemplate.send(PRESCRIPTION_TOPIC, event.getPrescriptionId(), event);
        log.info("PrescriptionEvent publie: type={}, prescriptionId={}", event.getEventType(), event.getPrescriptionId());
    }
}
