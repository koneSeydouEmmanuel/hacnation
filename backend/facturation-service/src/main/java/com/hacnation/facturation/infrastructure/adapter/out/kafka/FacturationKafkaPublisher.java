package com.hacnation.facturation.infrastructure.adapter.out.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacnation.common.events.BillingEvent;
import com.hacnation.common.events.NotificationEvent;
import com.hacnation.facturation.domain.port.BillingEventPublisherPort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class FacturationKafkaPublisher implements BillingEventPublisherPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public FacturationKafkaPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishBillingEvent(BillingEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("sic.facturation", event.getFactureId(), payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erreur lors de la publication de l'evenement billing", e);
        }
    }

    @Override
    public void publishNotificationEvent(NotificationEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("sic.notification", event.getPatientId(), payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erreur lors de la publication de l'evenement notification", e);
        }
    }
}
