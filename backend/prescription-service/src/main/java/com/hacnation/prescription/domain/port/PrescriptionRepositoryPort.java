package com.hacnation.prescription.domain.port;

import com.hacnation.prescription.domain.model.Prescription;
import java.util.List;
import java.util.Optional;

public interface PrescriptionRepositoryPort {

    Prescription save(Prescription prescription);

    Optional<Prescription> findById(String id);

    List<Prescription> findByConsultationId(String consultationId);

    List<Prescription> findByTypeAndStatut(com.hacnation.common.enums.TypePrescription type,
                                            com.hacnation.common.enums.StatutPrescription statut);
}
