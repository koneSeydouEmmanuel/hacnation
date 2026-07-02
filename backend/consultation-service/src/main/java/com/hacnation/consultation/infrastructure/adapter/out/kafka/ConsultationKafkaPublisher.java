package com.hacnation.consultation.infrastructure.adapter.out.kafka;

import com.hacnation.common.events.ConsultationEvent;
import com.hacnation.consultation.domain.port.ConsultationEventPublisherPort;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ConsultationKafkaPublisher implements ConsultationEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(ConsultationKafkaPublisher.class);
    private static final String CONSULTATION_TOPIC = "sic.consultation";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ConsultationKafkaPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishConsultationEvent(ConsultationEvent event) {
        event.setEventId(UUID.randomUUID().toString());
        kafkaTemplate.send(CONSULTATION_TOPIC, event.getConsultationId(), event);
        log.info("ConsultationEvent publie: type={}, consultationId={}", event.getEventType(), event.getConsultationId());
    }
}
