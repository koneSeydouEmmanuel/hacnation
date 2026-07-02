package com.hacnation.laboratoire.infrastructure.adapter.out.jpa;

import com.hacnation.common.enums.StatutPrescription;
import com.hacnation.laboratoire.domain.model.DemandeAnalyse;
import com.hacnation.laboratoire.domain.port.DemandeAnalyseRepositoryPort;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandeAnalyseJpaRepository extends JpaRepository<DemandeAnalyse, String>, DemandeAnalyseRepositoryPort {

    Page<DemandeAnalyse> findByStatutOrderByCreatedAtAsc(StatutPrescription statut, Pageable pageable);

    List<DemandeAnalyse> findByPatientIdOrderByCreatedAtDesc(String patientId);
}
