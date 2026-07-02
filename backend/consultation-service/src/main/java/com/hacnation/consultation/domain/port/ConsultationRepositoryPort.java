package com.hacnation.consultation.domain.port;

import com.hacnation.consultation.domain.model.Consultation;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConsultationRepositoryPort {

    Consultation save(Consultation consultation);

    Optional<Consultation> findById(String id);

    Page<Consultation> findByPatientIdOrderByDateDesc(String patientId, Pageable pageable);

    Page<Consultation> findByMedecinIdOrderByDateDesc(String medecinId, Pageable pageable);

    java.util.List<Consultation> findAll();
}
