package com.hacnation.reportingadmin.infrastructure.adapter.outbound;

import com.hacnation.reportingadmin.domain.model.ParametrageSysteme;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ParametrageJpaRepository extends JpaRepository<ParametrageSysteme, String> {
    Optional<ParametrageSysteme> findByCle(String cle);
}

@Repository
class ParametrageJpaAdapter implements com.hacnation.reportingadmin.domain.port.outbound.ParametrageRepositoryPort {

    private final ParametrageJpaRepository jpaRepository;

    ParametrageJpaAdapter(ParametrageJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ParametrageSysteme save(ParametrageSysteme parametrage) {
        return jpaRepository.save(parametrage);
    }

    @Override
    public java.util.Optional<ParametrageSysteme> findById(String id) {
        return jpaRepository.findById(id);
    }

    @Override
    public java.util.Optional<ParametrageSysteme> findByCle(String cle) {
        return jpaRepository.findByCle(cle);
    }

    @Override
    public java.util.List<ParametrageSysteme> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}
