package com.hacnation.rdv.infrastructure.adapter.out.kafka;

import com.hacnation.rdv.domain.port.RdvEventPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class RdvKafkaPublisher implements RdvEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(RdvKafkaPublisher.class);
    private static final String RDV_EVENTS_TOPIC = "sic.rdv";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public RdvKafkaPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishRdvCreated(String rdvId, String patientId, String service, LocalDateTime dateHeure) {
        RdvEventPayload event = new RdvEventPayload();
        event.setEventId(UUID.randomUUID().toString());
        event.setEventType("RDV_CREATED");
        event.setRdvId(rdvId);
        event.setPatientId(patientId);
        event.setService(service);
        event.setDateHeure(dateHeure);

        kafkaTemplate.send(RDV_EVENTS_TOPIC, rdvId, event);
        log.info("RdvEvent publie: type={}, rdvId={}", event.getEventType(), event.getRdvId());
    }

    @Override
    public void publishRdvStatusChanged(String rdvId, String patientId, String statut) {
        RdvEventPayload event = new RdvEventPayload();
        event.setEventId(UUID.randomUUID().toString());
        event.setEventType("RDV_STATUS_CHANGED");
        event.setRdvId(rdvId);
        event.setPatientId(patientId);
        event.setStatut(statut);

        kafkaTemplate.send(RDV_EVENTS_TOPIC, rdvId, event);
        log.info("RdvEvent publie: type={}, rdvId={}", event.getEventType(), event.getRdvId());
    }

    public static class RdvEventPayload {
        private String eventId;
        private String eventType;
        private String rdvId;
        private String patientId;
        private String service;
        private String statut;
        private LocalDateTime dateHeure;
        private LocalDateTime timestamp;

        public RdvEventPayload() {
            this.timestamp = LocalDateTime.now();
        }

        public String getEventId() { return eventId; }
        public void setEventId(String eventId) { this.eventId = eventId; }
        public String getEventType() { return eventType; }
        public void setEventType(String eventType) { this.eventType = eventType; }
        public String getRdvId() { return rdvId; }
        public void setRdvId(String rdvId) { this.rdvId = rdvId; }
        public String getPatientId() { return patientId; }
        public void setPatientId(String patientId) { this.patientId = patientId; }
        public String getService() { return service; }
        public void setService(String service) { this.service = service; }
        public String getStatut() { return statut; }
        public void setStatut(String statut) { this.statut = statut; }
        public LocalDateTime getDateHeure() { return dateHeure; }
        public void setDateHeure(LocalDateTime dateHeure) { this.dateHeure = dateHeure; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    }
}
