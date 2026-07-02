package com.hacnation.caisse.infrastructure.adapter.out.jpa;

import com.hacnation.caisse.domain.model.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaiementJpaRepository extends JpaRepository<Paiement, String> {

    List<Paiement> findByFactureId(String factureId);

    List<Paiement> findByDatePaiementBetween(LocalDateTime start, LocalDateTime end);
}
