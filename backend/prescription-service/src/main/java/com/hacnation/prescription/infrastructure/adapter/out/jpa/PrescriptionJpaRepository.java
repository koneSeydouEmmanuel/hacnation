package com.hacnation.prescription.infrastructure.adapter.out.jpa;

import com.hacnation.prescription.domain.model.Prescription;
import com.hacnation.prescription.domain.port.PrescriptionRepositoryPort;
import java.util.List;
import com.hacnation.common.enums.StatutPrescription;
import com.hacnation.common.enums.TypePrescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescriptionJpaRepository extends JpaRepository<Prescription, String>, PrescriptionRepositoryPort {

    List<Prescription> findByConsultationId(String consultationId);

    List<Prescription> findByTypeAndStatut(TypePrescription type, StatutPrescription statut);
}
