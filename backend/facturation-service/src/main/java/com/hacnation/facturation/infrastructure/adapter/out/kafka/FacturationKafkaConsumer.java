package com.hacnation.facturation.infrastructure.adapter.out.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacnation.common.events.ConsultationEvent;
import com.hacnation.common.events.LabEvent;
import com.hacnation.common.events.PharmacyEvent;
import com.hacnation.facturation.application.service.GenerateInvoiceUseCase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class FacturationKafkaConsumer {

    private final GenerateInvoiceUseCase generateInvoiceUseCase;
    private final ObjectMapper objectMapper;

    public FacturationKafkaConsumer(GenerateInvoiceUseCase generateInvoiceUseCase, ObjectMapper objectMapper) {
        this.generateInvoiceUseCase = generateInvoiceUseCase;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "sic.consultation", groupId = "facturation-group")
    public void handleConsultationEvent(String message) {
        try {
            ConsultationEvent event = objectMapper.readValue(message, ConsultationEvent.class);

            if ("CONSULTATION_TERMINATED".equals(event.getEventType())) {
                List<Map<String, Object>> actes = new ArrayList<>();
                Map<String, Object> consultationActe = new LinkedHashMap<>();
                consultationActe.put("description", "Consultation medicale");
                consultationActe.put("quantite", 1);
                consultationActe.put("prixUnitaire", BigDecimal.ZERO);
                actes.add(consultationActe);

                generateInvoiceUseCase.genererFacture(
                        event.getConsultationId(),
                        event.getPatientId(),
                        actes);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erreur lors du traitement de l'evenement consultation", e);
        }
    }

    @KafkaListener(topics = "sic.pharmacie", groupId = "facturation-group")
    public void handlePharmacyEvent(String message) {
        try {
            PharmacyEvent event = objectMapper.readValue(message, PharmacyEvent.class);

            if ("DRUG_DISPENSED".equals(event.getEventType())) {
                List<Map<String, Object>> actes = new ArrayList<>();
                Map<String, Object> medicamentActe = new LinkedHashMap<>();
                medicamentActe.put("description", event.getMedicamentId());
                medicamentActe.put("quantite", event.getQuantite() != null ? event.getQuantite() : 1);
                medicamentActe.put("prixUnitaire", BigDecimal.ZERO);
                actes.add(medicamentActe);

                generateInvoiceUseCase.genererFacture(
                        event.getOrdonnanceId(),
                        event.getPatientId(),
                        actes);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erreur lors du traitement de l'evenement pharmacie", e);
        }
    }

    @KafkaListener(topics = "sic.laboratoire", groupId = "facturation-group")
    public void handleLabEvent(String message) {
        try {
            LabEvent event = objectMapper.readValue(message, LabEvent.class);

            if ("ANALYSIS_VALIDATED".equals(event.getEventType()) || "RESULT_VALIDATED".equals(event.getEventType())) {
                List<Map<String, Object>> actes = new ArrayList<>();
                Map<String, Object> examenActe = new LinkedHashMap<>();
                examenActe.put("description", "Examen Laboratoire");
                examenActe.put("quantite", 1);
                examenActe.put("prixUnitaire", BigDecimal.ZERO);
                actes.add(examenActe);

                generateInvoiceUseCase.genererFacture(
                        event.getAnalyseId(),
                        event.getPatientId(),
                        actes);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erreur lors du traitement de l'evenement laboratoire", e);
        }
    }
}
