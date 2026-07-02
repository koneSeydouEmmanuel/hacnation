package com.hacnation.pharmacie.infrastructure.adapter.out.jpa;

import com.hacnation.common.enums.StatutPrescription;
import com.hacnation.pharmacie.domain.model.Ordonnance;
import com.hacnation.pharmacie.domain.port.OrdonnanceRepositoryPort;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdonnanceJpaRepository extends JpaRepository<Ordonnance, String>, OrdonnanceRepositoryPort {

    Page<Ordonnance> findByStatutOrderByCreatedAtAsc(StatutPrescription statut, Pageable pageable);

    List<Ordonnance> findByPatientIdOrderByCreatedAtDesc(String patientId);
}
