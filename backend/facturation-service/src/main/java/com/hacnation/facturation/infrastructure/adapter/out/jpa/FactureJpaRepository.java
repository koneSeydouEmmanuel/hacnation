package com.hacnation.facturation.infrastructure.adapter.out.jpa;

import com.hacnation.facturation.domain.model.Facture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FactureJpaRepository extends JpaRepository<Facture, String> {

    Page<Facture> findByPatientIdOrderByDateCreationDesc(String patientId, Pageable pageable);

    List<Facture> findByStatut(com.hacnation.facturation.domain.model.StatutFacture statut);

    List<Facture> findByDateCreationBetween(LocalDateTime start, LocalDateTime end);
}
