package com.hacnation.hospitalisation.infrastructure.adapter.out.jpa;

import com.hacnation.common.enums.StatutLit;
import com.hacnation.hospitalisation.domain.model.Lit;
import com.hacnation.hospitalisation.domain.port.LitRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LitRepositoryAdapter implements LitRepositoryPort {

    private final LitJpaRepository jpaRepository;

    public LitRepositoryAdapter(LitJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Lit save(Lit lit) {
        return jpaRepository.save(lit);
    }

    @Override
    public Optional<Lit> findById(String id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Lit> findByServiceAndStatut(String service, StatutLit statut) {
        return jpaRepository.findByServiceAndStatut(service, statut);
    }

    @Override
    public List<Lit> findByService(String service) {
        return jpaRepository.findByService(service);
    }

    @Override
    public long countByServiceAndStatut(String service, StatutLit statut) {
        return jpaRepository.countByServiceAndStatut(service, statut);
    }
}
