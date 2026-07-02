package com.hacnation.accueil.infrastructure.adapter.out.kafka;

import com.hacnation.common.events.AdmissionEvent;
import com.hacnation.accueil.domain.port.AdmissionEventPublisherPort;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AccueilKafkaPublisher implements AdmissionEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(AccueilKafkaPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public AccueilKafkaPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishAdmissionEvent(AdmissionEvent event) {
        event.setEventId(UUID.randomUUID().toString());
        kafkaTemplate.send("sic.admission", event.getAdmissionId(), event);
        log.info("AdmissionEvent publie sur sic.admission: type={}, admissionId={}", event.getEventType(), event.getAdmissionId());
    }
}
