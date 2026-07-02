package com.hacnation.soins.infrastructure.adapter.outbound;

import com.hacnation.soins.domain.model.SoinInfirmier;
import com.hacnation.soins.domain.port.outbound.SoinInfirmierRepositoryPort;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SoinInfirmierJpaRepository extends JpaRepository<SoinInfirmier, String> {

    List<SoinInfirmier> findByHospitalisationId(String hospitalisationId);

    List<SoinInfirmier> findByPatientIdAndStatut(String patientId, String statut);
}

@Repository
class SoinInfirmierJpaAdapter implements SoinInfirmierRepositoryPort {

    private final SoinInfirmierJpaRepository jpaRepository;

    SoinInfirmierJpaAdapter(SoinInfirmierJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public SoinInfirmier save(SoinInfirmier soin) {
        return jpaRepository.save(soin);
    }

    @Override
    public Optional<SoinInfirmier> findById(String id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<SoinInfirmier> findByHospitalisationId(String hospitalisationId) {
        return jpaRepository.findByHospitalisationId(hospitalisationId);
    }

    @Override
    public List<SoinInfirmier> findByPatientIdAndStatut(String patientId, String statut) {
        return jpaRepository.findByPatientIdAndStatut(patientId, statut);
    }
}
