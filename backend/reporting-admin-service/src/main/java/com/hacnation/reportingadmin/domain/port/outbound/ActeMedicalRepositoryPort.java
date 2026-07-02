package com.hacnation.reportingadmin.domain.port.outbound;

import com.hacnation.reportingadmin.domain.model.ActeMedical;
import java.util.List;
import java.util.Optional;

public interface ActeMedicalRepositoryPort {

    ActeMedical save(ActeMedical acte);
    Optional<ActeMedical> findById(String id);
    List<ActeMedical> findAll();
    List<ActeMedical> findByType(String type);
    void deleteById(String id);
}
