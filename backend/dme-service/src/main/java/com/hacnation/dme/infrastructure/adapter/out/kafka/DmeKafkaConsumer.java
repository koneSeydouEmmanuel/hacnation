package com.hacnation.dme.infrastructure.adapter.out.kafka;

import com.hacnation.dme.application.service.UpdateDmeUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DmeKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(DmeKafkaConsumer.class);

    private final UpdateDmeUseCase updateDmeUseCase;

    public DmeKafkaConsumer(UpdateDmeUseCase updateDmeUseCase) {
        this.updateDmeUseCase = updateDmeUseCase;
    }

    @KafkaListener(topics = "sic.patient", groupId = "dme-group")
    public void handlePatientEvent(Map<String, Object> event) {
        try {
            String eventType = (String) event.get("eventType");
            String patientId = (String) event.get("patientId");

            if ("sic.patient.cree".equals(eventType)) {
                updateDmeUseCase.createDmeForPatient(patientId);
                log.info("DME cree automatiquement pour le patient: {}", patientId);
            }
        } catch (Exception e) {
            log.error("Erreur lors du traitement de l'evenement patient: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "sic.consultation", groupId = "dme-group")
    public void handleConsultationEvent(Map<String, Object> event) {
        try {
            String eventType = (String) event.get("eventType");

            if ("CONSULTATION_TERMINATED".equals(eventType)) {
                String patientId = (String) event.get("patientId");
                String consultationId = (String) event.get("consultationId");
                String medecinId = (String) event.get("medecinId");
                String statut = (String) event.get("statut");

                updateDmeUseCase.addNoteCliniqueFromConsultation(patientId, consultationId, medecinId, statut);
                log.info("Note de consultation terminee ajoutee au DME du patient: {}", patientId);
            }
        } catch (Exception e) {
            log.error("Erreur lors du traitement de l'evenement consultation: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "sic.laboratoire", groupId = "dme-group")
    public void handleLabEvent(Map<String, Object> event) {
        try {
            String eventType = (String) event.get("eventType");

            if ("LAB_RESULT_AVAILABLE".equals(eventType)) {
                String patientId = (String) event.get("patientId");
                String analyseId = (String) event.get("analyseId");
                String prescriptionId = (String) event.get("prescriptionId");
                String statut = (String) event.get("statut");

                updateDmeUseCase.addDocumentFromLab(patientId, analyseId, prescriptionId, statut);
                log.info("Resultat de laboratoire ajoute au DME du patient: {}", patientId);
            }
        } catch (Exception e) {
            log.error("Erreur lors du traitement de l'evenement laboratoire: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "sic.pharmacie", groupId = "dme-group")
    public void handlePharmacyEvent(Map<String, Object> event) {
        try {
            String eventType = (String) event.get("eventType");

            if ("DRUG_DISPENSED".equals(eventType)) {
                String patientId = (String) event.get("patientId");
                String ordonnanceId = (String) event.get("ordonnanceId");
                String prescriptionId = (String) event.get("prescriptionId");
                String medicamentId = (String) event.get("medicamentId");
                Integer quantite = event.get("quantite") != null ? ((Number) event.get("quantite")).intValue() : null;
                String statut = (String) event.get("statut");

                updateDmeUseCase.addNoteFromPharmacy(patientId, ordonnanceId, prescriptionId, medicamentId, quantite, statut);
                log.info("Delivrance de medicament ajoutee au DME du patient: {}", patientId);
            }
        } catch (Exception e) {
            log.error("Erreur lors du traitement de l'evenement pharmacie: {}", e.getMessage(), e);
        }
    }
}
