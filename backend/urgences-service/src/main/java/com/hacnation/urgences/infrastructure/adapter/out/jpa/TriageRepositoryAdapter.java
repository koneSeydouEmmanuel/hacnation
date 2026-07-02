package com.hacnation.urgences.infrastructure.adapter.out.jpa;

import com.hacnation.urgences.domain.model.Triage;
import com.hacnation.urgences.domain.port.TriageRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TriageRepositoryAdapter implements TriageRepositoryPort {

    private final TriageJpaRepository jpaRepository;

    public TriageRepositoryAdapter(TriageJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Triage save(Triage triage) {
        return jpaRepository.save(triage);
    }

    @Override
    public Optional<Triage> findById(String id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Triage> findByAdmissionId(String admissionId) {
        return jpaRepository.findByAdmissionId(admissionId);
    }

    @Override
    public List<Triage> findAll() {
        return jpaRepository.findAll();
    }
}
