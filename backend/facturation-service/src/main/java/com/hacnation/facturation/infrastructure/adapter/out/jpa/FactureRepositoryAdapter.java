package com.hacnation.facturation.infrastructure.adapter.out.jpa;

import com.hacnation.facturation.domain.model.Facture;
import com.hacnation.facturation.domain.model.StatutFacture;
import com.hacnation.facturation.domain.port.FactureRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class FactureRepositoryAdapter implements FactureRepositoryPort {

    private final FactureJpaRepository jpaRepository;

    public FactureRepositoryAdapter(FactureJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Facture save(Facture facture) {
        return jpaRepository.save(facture);
    }

    @Override
    public Optional<Facture> findById(String id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Page<Facture> findByPatientIdOrderByDateCreationDesc(String patientId, Pageable pageable) {
        return jpaRepository.findByPatientIdOrderByDateCreationDesc(patientId, pageable);
    }

    @Override
    public List<Facture> findByStatut(StatutFacture statut) {
        return jpaRepository.findByStatut(statut);
    }

    @Override
    public List<Facture> findByDateCreationBetween(LocalDateTime start, LocalDateTime end) {
        return jpaRepository.findByDateCreationBetween(start, end);
    }

    @Override
    public List<Facture> findAll() {
        return jpaRepository.findAll();
    }
}
