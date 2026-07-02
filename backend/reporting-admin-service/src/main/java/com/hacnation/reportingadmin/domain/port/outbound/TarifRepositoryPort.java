package com.hacnation.reportingadmin.domain.port.outbound;

import com.hacnation.reportingadmin.domain.model.Tarif;
import java.util.List;
import java.util.Optional;

public interface TarifRepositoryPort {

    Tarif save(Tarif tarif);
    Optional<Tarif> findById(String id);
    List<Tarif> findAll();
    Optional<Tarif> findByActeMedicalIdAndServiceId(String acteMedicalId, String serviceId);
    void deleteById(String id);
}
