package com.hacnation.reportingadmin.infrastructure.adapter.outbound;

import com.hacnation.reportingadmin.domain.model.ActeMedical;
import com.hacnation.reportingadmin.domain.port.outbound.ActeMedicalRepositoryPort;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ActeMedicalJpaRepository extends JpaRepository<ActeMedical, String> {

    List<ActeMedical> findByType(String type);
}

@Repository
class ActeMedicalJpaAdapter implements ActeMedicalRepositoryPort {

    private final ActeMedicalJpaRepository jpaRepository;

    ActeMedicalJpaAdapter(ActeMedicalJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ActeMedical save(ActeMedical acte) {
        return jpaRepository.save(acte);
    }

    @Override
    public Optional<ActeMedical> findById(String id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<ActeMedical> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public List<ActeMedical> findByType(String type) {
        return jpaRepository.findByType(type);
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}
