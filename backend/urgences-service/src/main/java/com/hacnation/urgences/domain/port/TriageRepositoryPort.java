package com.hacnation.urgences.domain.port;

import com.hacnation.urgences.domain.model.Triage;

import java.util.List;
import java.util.Optional;

public interface TriageRepositoryPort {

    Triage save(Triage triage);

    Optional<Triage> findById(String id);

    List<Triage> findByAdmissionId(String admissionId);

    List<Triage> findAll();
}
