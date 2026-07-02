package com.hacnation.accueil.domain.port;

import com.hacnation.accueil.domain.model.Admission;
import com.hacnation.common.events.AdmissionEvent;

import java.util.List;
import java.util.Optional;

public interface AdmissionRepositoryPort {

    Admission save(Admission admission);

    Optional<Admission> findById(String id);

    List<Admission> findByPatientId(String patientId);
}
