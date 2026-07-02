package com.hacnation.reportingadmin.infrastructure.adapter.outbound;

import com.hacnation.reportingadmin.domain.model.Tarif;
import com.hacnation.reportingadmin.domain.port.outbound.TarifRepositoryPort;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface TarifJpaRepository extends JpaRepository<Tarif, String> {

    Optional<Tarif> findByActeMedicalIdAndServiceId(String acteMedicalId, String serviceId);
}

@Repository
class TarifJpaAdapter implements TarifRepositoryPort {

    private final TarifJpaRepository jpaRepository;

    TarifJpaAdapter(TarifJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Tarif save(Tarif tarif) {
        return jpaRepository.save(tarif);
    }

    @Override
    public Optional<Tarif> findById(String id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Tarif> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Optional<Tarif> findByActeMedicalIdAndServiceId(String acteMedicalId, String serviceId) {
        return jpaRepository.findByActeMedicalIdAndServiceId(acteMedicalId, serviceId);
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}
