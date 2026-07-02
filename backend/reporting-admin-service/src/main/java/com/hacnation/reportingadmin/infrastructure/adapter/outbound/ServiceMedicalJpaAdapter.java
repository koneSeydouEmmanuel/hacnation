package com.hacnation.reportingadmin.infrastructure.adapter.outbound;

import com.hacnation.reportingadmin.domain.model.ServiceMedical;
import com.hacnation.reportingadmin.domain.port.outbound.ServiceMedicalRepositoryPort;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ServiceMedicalJpaRepository extends JpaRepository<ServiceMedical, String> {
}

@Repository
class ServiceMedicalJpaAdapter implements ServiceMedicalRepositoryPort {

    private final ServiceMedicalJpaRepository jpaRepository;

    ServiceMedicalJpaAdapter(ServiceMedicalJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ServiceMedical save(ServiceMedical service) {
        return jpaRepository.save(service);
    }

    @Override
    public Optional<ServiceMedical> findById(String id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<ServiceMedical> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}
