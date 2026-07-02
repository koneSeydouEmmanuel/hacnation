package com.hacnation.caisse.domain.port;

import com.hacnation.caisse.domain.model.Paiement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaiementRepositoryPort {

    Paiement save(Paiement paiement);

    Optional<Paiement> findById(String id);

    List<Paiement> findByFactureId(String factureId);

    List<Paiement> findByDatePaiementBetween(LocalDateTime start, LocalDateTime end);

    void deleteById(String id);
}
