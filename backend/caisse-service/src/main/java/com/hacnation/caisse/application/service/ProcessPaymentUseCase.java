package com.hacnation.caisse.application.service;

import com.hacnation.common.exception.BusinessException;
import com.hacnation.common.events.BillingEvent;
import com.hacnation.common.events.NotificationEvent;
import com.hacnation.caisse.domain.model.ModePaiement;
import com.hacnation.caisse.domain.model.Paiement;
import com.hacnation.caisse.domain.port.PaiementRepositoryPort;
import com.hacnation.caisse.domain.port.PaymentEventPublisherPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class ProcessPaymentUseCase {

    private final PaiementRepositoryPort paiementRepository;
    private final PaymentEventPublisherPort eventPublisher;

    public ProcessPaymentUseCase(PaiementRepositoryPort paiementRepository,
                                 PaymentEventPublisherPort eventPublisher) {
        this.paiementRepository = paiementRepository;
        this.eventPublisher = eventPublisher;
    }

    public Map<String, Object> payer(String factureId, String patientId, BigDecimal totalFacture,
                                      String modePaiementStr, String telephone, BigDecimal montant) {
        ModePaiement modePaiement = ModePaiement.valueOf(modePaiementStr.toUpperCase());

        String reference = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        if (modePaiement == ModePaiement.ORANGE_MONEY
                || modePaiement == ModePaiement.MTN_MONEY
                || modePaiement == ModePaiement.WAVE) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Paiement interrompu", e);
            }
        }

        Paiement paiement = new Paiement();
        paiement.setFactureId(factureId);
        paiement.setMontant(montant);
        paiement.setModePaiement(modePaiement);
        paiement.setReference(reference);
        paiement.setTelephone(telephone);
        paiement.setStatut("SUCCES");
        paiementRepository.save(paiement);

        BillingEvent billingEvent = BillingEvent.paymentReceived(factureId, patientId, montant, modePaiement.name());
        eventPublisher.publishBillingEvent(billingEvent);

        NotificationEvent notificationEvent = new NotificationEvent();
        notificationEvent.setEventType("PAYMENT_RECEIVED");
        notificationEvent.setPatientId(patientId);
        notificationEvent.setCanal("INFO");
        notificationEvent.setContenu("Paiement de " + montant + " recu pour la facture #" + factureId
                + ". Reference: " + reference);
        eventPublisher.publishNotificationEvent(notificationEvent);

        Map<String, Object> receipt = new LinkedHashMap<>();
        receipt.put("factureId", factureId);
        receipt.put("patientId", patientId);
        receipt.put("totalFacture", totalFacture);
        receipt.put("montantPaye", montant);
        receipt.put("modePaiement", modePaiement.name());
        receipt.put("reference", reference);
        receipt.put("date", paiement.getDatePaiement().toString());
        receipt.put("statut", "SUCCES");

        return receipt;
    }

    public List<Paiement> getPaiementsByFactureId(String factureId) {
        return paiementRepository.findByFactureId(factureId);
    }

    public List<Paiement> getPaiementsByPeriode(LocalDateTime start, LocalDateTime end) {
        return paiementRepository.findByDatePaiementBetween(start, end);
    }

    public Map<String, Object> getResumeCaisse(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        List<Paiement> paiements = paiementRepository.findByDatePaiementBetween(start, end);

        long nombreFactures = paiements.stream()
                .map(Paiement::getFactureId)
                .distinct()
                .count();

        BigDecimal totalPaiements = paiements.stream()
                .map(Paiement::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> detailsByMode = new LinkedHashMap<>();
        for (ModePaiement mode : ModePaiement.values()) {
            BigDecimal sum = paiements.stream()
                    .filter(p -> p.getModePaiement() == mode)
                    .map(Paiement::getMontant)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (sum.compareTo(BigDecimal.ZERO) > 0) {
                detailsByMode.put(mode.name(), sum);
            }
        }

        Map<String, Object> resume = new LinkedHashMap<>();
        resume.put("date", date.toString());
        resume.put("nombreFactures", nombreFactures);
        resume.put("totalPaiements", totalPaiements);
        resume.put("detailsByModePaiement", detailsByMode);
        resume.put("nombreTransactions", paiements.size());

        return resume;
    }

    public void verifierPaiementNonModifiable(String paiementId) {
        var paiement = paiementRepository.findById(paiementId);
        if (paiement.isPresent() && "SUCCES".equals(paiement.get().getStatut())) {
            throw new BusinessException(
                    "Ce paiement est valide et ne peut plus etre modifie", 409);
        }
    }

    public void supprimerPaiement(String paiementId) {
        verifierPaiementNonModifiable(paiementId);
        paiementRepository.deleteById(paiementId);
    }
}
