package com.hacnation.reportingadmin.domain.port.outbound;

import com.hacnation.reportingadmin.domain.model.ServiceMedical;
import java.util.List;
import java.util.Optional;

public interface ServiceMedicalRepositoryPort {

    ServiceMedical save(ServiceMedical service);
    Optional<ServiceMedical> findById(String id);
    List<ServiceMedical> findAll();
    void deleteById(String id);
}
