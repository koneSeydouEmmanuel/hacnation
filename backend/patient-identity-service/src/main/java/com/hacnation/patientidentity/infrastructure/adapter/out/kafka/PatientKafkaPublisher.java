package com.hacnation.patientidentity.infrastructure.adapter.out.kafka;

import com.hacnation.patientidentity.domain.port.PatientEventPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PatientKafkaPublisher implements PatientEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(PatientKafkaPublisher.class);
    private static final String TOPIC = "sic.patient";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PatientKafkaPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishPatientCreated(String patientId, String nom, String prenom, String telephone) {
        PatientEventPayload event = new PatientEventPayload(
                UUID.randomUUID().toString(),
                "sic.patient.cree",
                patientId,
                nom,
                prenom,
                telephone
        );
        publishEvent(event);
    }

    @Override
    public void publishPatientUpdated(String patientId, String nom, String prenom, String telephone) {
        PatientEventPayload event = new PatientEventPayload(
                UUID.randomUUID().toString(),
                "sic.patient.modifie",
                patientId,
                nom,
                prenom,
                telephone
        );
        publishEvent(event);
    }

    private void publishEvent(PatientEventPayload event) {
        try {
            kafkaTemplate.send(TOPIC, event.getPatientId(), event)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Erreur lors de la publication de l'evenement patient [{}]: {}",
                                    event.getEventType(), ex.getMessage(), ex);
                        } else {
                            log.info("Evenement patient publie [{}] pour le patient: {}",
                                    event.getEventType(), event.getPatientId());
                        }
                    });
        } catch (Exception e) {
            log.error("Erreur Kafka lors de la publication de l'evenement patient: {}", e.getMessage(), e);
        }
    }

    public static class PatientEventPayload {
        private String eventId;
        private String eventType;
        private String patientId;
        private String nom;
        private String prenom;
        private String telephone;

        public PatientEventPayload() {
        }

        public PatientEventPayload(String eventId, String eventType, String patientId, String nom, String prenom, String telephone) {
            this.eventId = eventId;
            this.eventType = eventType;
            this.patientId = patientId;
            this.nom = nom;
            this.prenom = prenom;
            this.telephone = telephone;
        }

        public String getEventId() { return eventId; }
        public void setEventId(String eventId) { this.eventId = eventId; }
        public String getEventType() { return eventType; }
        public void setEventType(String eventType) { this.eventType = eventType; }
        public String getPatientId() { return patientId; }
        public void setPatientId(String patientId) { this.patientId = patientId; }
        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }
        public String getPrenom() { return prenom; }
        public void setPrenom(String prenom) { this.prenom = prenom; }
        public String getTelephone() { return telephone; }
        public void setTelephone(String telephone) { this.telephone = telephone; }
    }
}
