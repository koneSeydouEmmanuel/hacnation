package com.hacnation.dme.domain.port;

import com.hacnation.dme.domain.model.DossierMedical;

import java.util.Optional;

public interface DmeRepositoryPort {

    DossierMedical save(DossierMedical dme);

    Optional<DossierMedical> findById(String id);

    Optional<DossierMedical> findByPatientId(String patientId);
}
