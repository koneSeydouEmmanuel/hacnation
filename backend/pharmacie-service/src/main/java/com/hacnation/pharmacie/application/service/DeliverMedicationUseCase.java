package com.hacnation.pharmacie.application.service;

import com.hacnation.common.enums.StatutPrescription;
import com.hacnation.common.events.NotificationEvent;
import com.hacnation.common.events.PharmacyEvent;
import com.hacnation.common.exception.BusinessException;
import com.hacnation.common.exception.ResourceNotFoundException;
import com.hacnation.pharmacie.domain.model.Ordonnance;
import com.hacnation.pharmacie.domain.model.StockMedicament;
import com.hacnation.pharmacie.domain.port.OrdonnanceRepositoryPort;
import com.hacnation.pharmacie.domain.port.PharmacyEventPublisherPort;
import com.hacnation.pharmacie.domain.port.StockRepositoryPort;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeliverMedicationUseCase {

    private static final Logger log = LoggerFactory.getLogger(DeliverMedicationUseCase.class);

    private final OrdonnanceRepositoryPort ordonnanceRepository;
    private final StockRepositoryPort stockRepository;
    private final PharmacyEventPublisherPort eventPublisher;
    private final ObjectMapper objectMapper;

    public DeliverMedicationUseCase(OrdonnanceRepositoryPort ordonnanceRepository,
                                     StockRepositoryPort stockRepository,
                                     PharmacyEventPublisherPort eventPublisher,
                                     ObjectMapper objectMapper) {
        this.ordonnanceRepository = ordonnanceRepository;
        this.stockRepository = stockRepository;
        this.eventPublisher = eventPublisher;
        this.objectMapper = objectMapper;
    }

    public Page<Ordonnance> getOrdonnancesEnAttente(Pageable pageable) {
        return ordonnanceRepository.findByStatutOrderByCreatedAtAsc(StatutPrescription.EN_ATTENTE, pageable);
    }

    public Ordonnance getOrdonnanceById(String id) {
        return ordonnanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ordonnance", id));
    }

    @Transactional
    public Ordonnance delivrerOrdonnance(String ordonnanceId, String pharmacienId) {
        Ordonnance ordonnance = ordonnanceRepository.findById(ordonnanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Ordonnance", ordonnanceId));

        List<Map<String, Object>> medicaments;
        try {
            medicaments = objectMapper.readValue(ordonnance.getMedicaments(),
                    new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            throw new BusinessException("Impossible de parser la liste de medicaments: " + e.getMessage(), 400);
        }

        List<String> shortages = new ArrayList<>();

        for (Map<String, Object> item : medicaments) {
            String medicamentId = (String) item.get("medicamentId");
            Integer quantiteRequise = item.get("quantite") instanceof Integer
                    ? (Integer) item.get("quantite")
                    : ((Number) item.get("quantite")).intValue();

            List<StockMedicament> stockEntries = stockRepository
                    .findByMedicamentIdOrderByDatePeremptionAsc(medicamentId);

            int quantiteRestante = quantiteRequise;
            for (StockMedicament stock : stockEntries) {
                if (quantiteRestante <= 0) {
                    break;
                }
                int deducted = Math.min(stock.getQuantite(), quantiteRestante);
                stock.setQuantite(stock.getQuantite() - deducted);
                quantiteRestante -= deducted;
                stockRepository.save(stock);
            }

            if (quantiteRestante > 0) {
                shortages.add("Medicament " + medicamentId + ": besoin " + quantiteRequise
                        + ", manque " + quantiteRestante);
            }

            PharmacyEvent pharmacyEvent = PharmacyEvent.dispensed(
                    ordonnanceId, ordonnance.getPatientId(), medicamentId, quantiteRequise - quantiteRestante);
            eventPublisher.publishPharmacyEvent(pharmacyEvent);
        }

        if (!shortages.isEmpty()) {
            throw new BusinessException("Rupture de stock: " + String.join("; ", shortages), 409);
        }

        ordonnance.setStatut(StatutPrescription.TERMINEE);
        ordonnance.setPharmacienId(pharmacienId);
        ordonnance.setDateDelivrance(LocalDateTime.now());

        Ordonnance saved = ordonnanceRepository.save(ordonnance);

        NotificationEvent notificationEvent = new NotificationEvent();
        notificationEvent.setEventType("DELIVRANCE_TERMINEE");
        notificationEvent.setPatientId(saved.getPatientId());
        notificationEvent.setContenu("Votre ordonnance a ete delivree avec succes.");
        notificationEvent.setCanal("EMAIL");
        eventPublisher.publishNotificationEvent(notificationEvent);

        log.info("Ordonnance delivree: id={}, pharmacien={}", ordonnanceId, pharmacienId);
        return saved;
    }
}
