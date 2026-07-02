package com.hacnation.pharmacie.domain.port;

import com.hacnation.common.enums.StatutPrescription;
import com.hacnation.pharmacie.domain.model.Ordonnance;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrdonnanceRepositoryPort {

    Ordonnance save(Ordonnance ordonnance);

    Optional<Ordonnance> findById(String id);

    Page<Ordonnance> findByStatutOrderByCreatedAtAsc(StatutPrescription statut, Pageable pageable);

    List<Ordonnance> findByPatientIdOrderByCreatedAtDesc(String patientId);
}
