package com.hacnation.facturation.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacnation.common.events.BillingEvent;
import com.hacnation.common.events.NotificationEvent;
import com.hacnation.facturation.domain.model.Facture;
import com.hacnation.facturation.domain.model.StatutFacture;
import com.hacnation.facturation.domain.port.BillingEventPublisherPort;
import com.hacnation.facturation.domain.port.FactureRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class GenerateInvoiceUseCase {

    private final FactureRepositoryPort factureRepository;
    private final BillingEventPublisherPort eventPublisher;
    private final ObjectMapper objectMapper;

    public GenerateInvoiceUseCase(FactureRepositoryPort factureRepository,
                                  BillingEventPublisherPort eventPublisher,
                                  ObjectMapper objectMapper) {
        this.factureRepository = factureRepository;
        this.eventPublisher = eventPublisher;
        this.objectMapper = objectMapper;
    }

    public FactureDto genererFacture(String consultationId, String patientId, List<Map<String, Object>> actes) {
        Facture facture = new Facture();
        facture.setPatientId(patientId);
        facture.setConsultationId(consultationId);

        BigDecimal total = BigDecimal.ZERO;
        List<Map<String, Object>> lignesList = new ArrayList<>();

        for (Map<String, Object> acte : actes) {
            String description = (String) acte.get("description");
            int quantite = ((Number) acte.get("quantite")).intValue();
            BigDecimal prixUnitaire = new BigDecimal(acte.get("prixUnitaire").toString());

            BigDecimal montant = prixUnitaire.multiply(BigDecimal.valueOf(quantite));
            total = total.add(montant);

            Map<String, Object> ligne = new HashMap<>();
            ligne.put("description", description);
            ligne.put("quantite", quantite);
            ligne.put("prixUnitaire", prixUnitaire);
            ligne.put("montant", montant);
            lignesList.add(ligne);
        }

        try {
            facture.setLignes(objectMapper.writeValueAsString(lignesList));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erreur lors de la serialisation des lignes de facture", e);
        }
        facture.setTotal(total);

        Facture saved = factureRepository.save(facture);

        BillingEvent billingEvent = BillingEvent.invoiceCreated(saved.getId(), patientId, total);
        eventPublisher.publishBillingEvent(billingEvent);

        NotificationEvent notificationEvent = new NotificationEvent();
        notificationEvent.setEventType("INVOICE_CREATED");
        notificationEvent.setPatientId(patientId);
        notificationEvent.setCanal("INFO");
        notificationEvent.setContenu("Votre facture #" + saved.getId() + " d'un montant de " + total + " a ete creee.");
        eventPublisher.publishNotificationEvent(notificationEvent);

        return FactureDto.fromEntity(saved);
    }

    public FactureDto getFacture(String id) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facture introuvable: " + id));
        return FactureDto.fromEntity(facture);
    }

    public Page<FactureDto> getFacturesByPatient(String patientId, Pageable pageable) {
        return factureRepository.findByPatientIdOrderByDateCreationDesc(patientId, pageable)
                .map(FactureDto::fromEntity);
    }

    public List<FactureDto> getFacturesByPeriode(LocalDateTime start, LocalDateTime end) {
        return factureRepository.findByDateCreationBetween(start, end).stream()
                .map(FactureDto::fromEntity)
                .collect(Collectors.toList());
    }

    public void consoliderActes(String consultationId) {
        List<Facture> factures = factureRepository.findAll().stream()
                .filter(f -> consultationId.equals(f.getConsultationId()))
                .collect(Collectors.toList());

        if (factures.isEmpty()) {
            return;
        }

        BigDecimal totalConsolide = BigDecimal.ZERO;
        List<Map<String, Object>> allLignes = new ArrayList<>();

        for (Facture f : factures) {
            totalConsolide = totalConsolide.add(f.getTotal());
            try {
                List<Map<String, Object>> lignes = objectMapper.readValue(f.getLignes(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));
                allLignes.addAll(lignes);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erreur lors de la lecture des lignes de facture", e);
            }
        }

        if (factures.size() == 1) {
            Facture facture = factures.get(0);
            facture.setTotal(totalConsolide);
            try {
                facture.setLignes(objectMapper.writeValueAsString(allLignes));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erreur lors de la serialisation des lignes", e);
            }
            factureRepository.save(facture);
        } else {
            Facture first = factures.get(0);
            try {
                first.setLignes(objectMapper.writeValueAsString(allLignes));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erreur lors de la serialisation des lignes", e);
            }
            first.setTotal(totalConsolide);
            factureRepository.save(first);

            for (int i = 1; i < factures.size(); i++) {
                factures.get(i).setStatut(StatutFacture.ANNULEE);
                factureRepository.save(factures.get(i));
            }
        }
    }

    public static class FactureDto {
        private String id;
        private String patientId;
        private String consultationId;
        private String lignes;
        private BigDecimal total;
        private String statut;
        private String modePaiement;
        private LocalDateTime dateCreation;
        private LocalDateTime datePaiement;

        public FactureDto() {
        }

        public static FactureDto fromEntity(Facture facture) {
            FactureDto dto = new FactureDto();
            dto.setId(facture.getId());
            dto.setPatientId(facture.getPatientId());
            dto.setConsultationId(facture.getConsultationId());
            dto.setLignes(facture.getLignes());
            dto.setTotal(facture.getTotal());
            dto.setStatut(facture.getStatut() != null ? facture.getStatut().name() : null);
            dto.setModePaiement(facture.getModePaiement() != null ? facture.getModePaiement().name() : null);
            dto.setDateCreation(facture.getDateCreation());
            dto.setDatePaiement(facture.getDatePaiement());
            return dto;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getPatientId() { return patientId; }
        public void setPatientId(String patientId) { this.patientId = patientId; }
        public String getConsultationId() { return consultationId; }
        public void setConsultationId(String consultationId) { this.consultationId = consultationId; }
        public String getLignes() { return lignes; }
        public void setLignes(String lignes) { this.lignes = lignes; }
        public BigDecimal getTotal() { return total; }
        public void setTotal(BigDecimal total) { this.total = total; }
        public String getStatut() { return statut; }
        public void setStatut(String statut) { this.statut = statut; }
        public String getModePaiement() { return modePaiement; }
        public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }
        public LocalDateTime getDateCreation() { return dateCreation; }
        public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
        public LocalDateTime getDatePaiement() { return datePaiement; }
        public void setDatePaiement(LocalDateTime datePaiement) { this.datePaiement = datePaiement; }
    }
}
