package com.hacnation.hospitalisation.infrastructure.adapter.out.jpa;

import com.hacnation.hospitalisation.domain.model.Hospitalisation;
import com.hacnation.hospitalisation.domain.port.HospitalisationRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class HospitalisationRepositoryAdapter implements HospitalisationRepositoryPort {

    private final HospitalisationJpaRepository jpaRepository;

    public HospitalisationRepositoryAdapter(HospitalisationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Hospitalisation save(Hospitalisation hospitalisation) {
        return jpaRepository.save(hospitalisation);
    }

    @Override
    public Optional<Hospitalisation> findById(String id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Hospitalisation> findByLitIdAndStatut(String litId, String statut) {
        return jpaRepository.findByLitIdAndStatut(litId, statut);
    }

    @Override
    public List<Hospitalisation> findByStatut(String statut) {
        return jpaRepository.findByStatut(statut);
    }
}
