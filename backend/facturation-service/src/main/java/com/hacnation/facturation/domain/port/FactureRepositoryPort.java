package com.hacnation.facturation.domain.port;

import com.hacnation.facturation.domain.model.Facture;
import com.hacnation.facturation.domain.model.StatutFacture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FactureRepositoryPort {

    Facture save(Facture facture);

    Optional<Facture> findById(String id);

    Page<Facture> findByPatientIdOrderByDateCreationDesc(String patientId, Pageable pageable);

    List<Facture> findByStatut(StatutFacture statut);

    List<Facture> findByDateCreationBetween(LocalDateTime start, LocalDateTime end);

    List<Facture> findAll();
}
