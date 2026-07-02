package com.hacnation.rdv.infrastructure.adapter.out.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaPatientConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaPatientConsumer.class);

    @KafkaListener(topics = "sic.patient", groupId = "rdv-service-group")
    public void consumePatientEvent(Map<String, Object> event) {
        String eventType = (String) event.get("eventType");
        String patientId = (String) event.get("patientId");
        String nom = (String) event.get("nom");
        String prenom = (String) event.get("prenom");

        if ("sic.patient.cree".equals(eventType)) {
            log.info("Patient cree: id={}, nom={} {}", patientId, nom, prenom);
        } else if ("sic.patient.modifie".equals(eventType)) {
            log.info("Patient mis a jour: id={}, nom={} {}", patientId, nom, prenom);
        } else {
            log.info("PatientEvent recu: type={}, patientId={}", eventType, patientId);
        }
    }
}
