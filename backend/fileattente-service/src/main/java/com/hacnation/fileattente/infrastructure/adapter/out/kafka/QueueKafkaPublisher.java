package com.hacnation.fileattente.infrastructure.adapter.out.kafka;

import com.hacnation.fileattente.domain.port.QueueEventPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class QueueKafkaPublisher implements QueueEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(QueueKafkaPublisher.class);
    private static final String QUEUE_EVENTS_TOPIC = "sic.file-attente";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public QueueKafkaPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishCheckIn(String patientId, String rdvId, String service, Integer position) {
        QueueEventPayload event = new QueueEventPayload();
        event.setEventId(UUID.randomUUID().toString());
        event.setEventType("PATIENT_CHECKED_IN");
        event.setPatientId(patientId);
        event.setRdvId(rdvId);
        event.setService(service);
        event.setPosition(position);
        event.setStatut("EN_ATTENTE");

        kafkaTemplate.send(QUEUE_EVENTS_TOPIC, patientId, event);
        log.info("QueueEvent publie: type={}, patientId={}", event.getEventType(), event.getPatientId());
    }

    @Override
    public void publishCallNext(String patientId, String service) {
        QueueEventPayload event = new QueueEventPayload();
        event.setEventId(UUID.randomUUID().toString());
        event.setEventType("PATIENT_CALLED");
        event.setPatientId(patientId);
        event.setService(service);
        event.setStatut("APPELE");

        kafkaTemplate.send(QUEUE_EVENTS_TOPIC, patientId, event);
        log.info("QueueEvent publie: type={}, patientId={}", event.getEventType(), event.getPatientId());
    }

    public static class QueueEventPayload {
        private String eventId;
        private String eventType;
        private String patientId;
        private String rdvId;
        private String service;
        private Integer position;
        private String statut;
        private LocalDateTime timestamp;

        public QueueEventPayload() {
            this.timestamp = LocalDateTime.now();
        }

        public String getEventId() { return eventId; }
        public void setEventId(String eventId) { this.eventId = eventId; }
        public String getEventType() { return eventType; }
        public void setEventType(String eventType) { this.eventType = eventType; }
        public String getPatientId() { return patientId; }
        public void setPatientId(String patientId) { this.patientId = patientId; }
        public String getRdvId() { return rdvId; }
        public void setRdvId(String rdvId) { this.rdvId = rdvId; }
        public String getService() { return service; }
        public void setService(String service) { this.service = service; }
        public Integer getPosition() { return position; }
        public void setPosition(Integer position) { this.position = position; }
        public String getStatut() { return statut; }
        public void setStatut(String statut) { this.statut = statut; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    }
}
