package com.hacnation.laboratoire.domain.port;

import com.hacnation.common.enums.StatutPrescription;
import com.hacnation.laboratoire.domain.model.DemandeAnalyse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DemandeAnalyseRepositoryPort {

    DemandeAnalyse save(DemandeAnalyse demandeAnalyse);

    Optional<DemandeAnalyse> findById(String id);

    Page<DemandeAnalyse> findByStatutOrderByCreatedAtAsc(StatutPrescription statut, Pageable pageable);

    List<DemandeAnalyse> findByPatientIdOrderByCreatedAtDesc(String patientId);
}
