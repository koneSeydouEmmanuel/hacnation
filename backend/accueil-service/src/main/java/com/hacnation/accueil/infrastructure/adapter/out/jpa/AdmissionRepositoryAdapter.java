package com.hacnation.accueil.infrastructure.adapter.out.jpa;

import com.hacnation.accueil.domain.model.Admission;
import com.hacnation.accueil.domain.port.AdmissionRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AdmissionRepositoryAdapter implements AdmissionRepositoryPort {

    private final AdmissionJpaRepository jpaRepository;

    public AdmissionRepositoryAdapter(AdmissionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Admission save(Admission admission) {
        return jpaRepository.save(admission);
    }

    @Override
    public Optional<Admission> findById(String id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Admission> findByPatientId(String patientId) {
        return jpaRepository.findByPatientId(patientId);
    }
}
