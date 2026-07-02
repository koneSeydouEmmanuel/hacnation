package com.hacnation.pharmacie.application.service;

import com.hacnation.common.events.PharmacyEvent;
import com.hacnation.pharmacie.domain.model.StockMedicament;
import com.hacnation.pharmacie.domain.port.PharmacyEventPublisherPort;
import com.hacnation.pharmacie.domain.port.StockRepositoryPort;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManageStockUseCase {

    private static final Logger log = LoggerFactory.getLogger(ManageStockUseCase.class);

    private final StockRepositoryPort stockRepository;
    private final PharmacyEventPublisherPort eventPublisher;

    public ManageStockUseCase(StockRepositoryPort stockRepository,
                               PharmacyEventPublisherPort eventPublisher) {
        this.stockRepository = stockRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<StockMedicament> getStock() {
        return stockRepository.findAll();
    }

    @Transactional
    public StockMedicament addStock(String medicamentId, String nom, String lot, Integer quantite,
                                     LocalDate datePeremption, Integer seuilMin, String emplacement) {
        StockMedicament stock = new StockMedicament();
        stock.setMedicamentId(medicamentId);
        stock.setNom(nom);
        stock.setLot(lot);
        stock.setQuantite(quantite);
        stock.setDatePeremption(datePeremption);
        if (seuilMin != null) {
            stock.setSeuilMin(seuilMin);
        }
        stock.setEmplacement(emplacement);

        StockMedicament saved = stockRepository.save(stock);
        log.info("Stock ajoute: medicamentId={}, lot={}, quantite={}", medicamentId, lot, quantite);
        return saved;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional(readOnly = true)
    public void checkFefoExpiry() {
        LocalDate today = LocalDate.now();
        List<StockMedicament> expired = stockRepository.findByDatePeremptionBefore(today);

        if (!expired.isEmpty()) {
            log.warn("Medicaments expires trouves: {}", expired.size());
            for (StockMedicament stock : expired) {
                log.warn("Medicament expire: id={}, nom={}, lot={}, peremption={}",
                        stock.getMedicamentId(), stock.getNom(), stock.getLot(), stock.getDatePeremption());

                PharmacyEvent alertEvent = new PharmacyEvent();
                alertEvent.setEventType("MEDICAMENT_EXPIRED");
                alertEvent.setMedicamentId(stock.getMedicamentId());
                alertEvent.setPatientId("SYSTEM");
                alertEvent.setQuantite(stock.getQuantite());
                alertEvent.setStatut("EXPIRE");
                eventPublisher.publishPharmacyEvent(alertEvent);
            }
        }

        List<StockMedicament> lowStock = stockRepository.findByQuantiteLessThanEqual(5);
        if (!lowStock.isEmpty()) {
            log.warn("Medicaments en stock faible: {}", lowStock.size());
            for (StockMedicament stock : lowStock) {
                log.warn("Stock faible: medicamentId={}, nom={}, quantite={}, seuil={}",
                        stock.getMedicamentId(), stock.getNom(), stock.getQuantite(), stock.getSeuilMin());
            }
        }
    }
}
