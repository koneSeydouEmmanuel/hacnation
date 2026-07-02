package com.hacnation.caisse.infrastructure.adapter.out.jpa;

import com.hacnation.caisse.domain.model.Paiement;
import com.hacnation.caisse.domain.port.PaiementRepositoryPort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class PaiementRepositoryAdapter implements PaiementRepositoryPort {

    private final PaiementJpaRepository jpaRepository;

    public PaiementRepositoryAdapter(PaiementJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Paiement save(Paiement paiement) {
        return jpaRepository.save(paiement);
    }

    @Override
    public Optional<Paiement> findById(String id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Paiement> findByFactureId(String factureId) {
        return jpaRepository.findByFactureId(factureId);
    }

    @Override
    public List<Paiement> findByDatePaiementBetween(LocalDateTime start, LocalDateTime end) {
        return jpaRepository.findByDatePaiementBetween(start, end);
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}
