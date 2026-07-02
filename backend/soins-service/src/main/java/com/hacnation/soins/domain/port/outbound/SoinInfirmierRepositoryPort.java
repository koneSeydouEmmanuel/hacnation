package com.hacnation.soins.domain.port.outbound;

import com.hacnation.soins.domain.model.SoinInfirmier;
import java.util.List;
import java.util.Optional;

public interface SoinInfirmierRepositoryPort {

    SoinInfirmier save(SoinInfirmier soin);

    Optional<SoinInfirmier> findById(String id);

    List<SoinInfirmier> findByHospitalisationId(String hospitalisationId);

    List<SoinInfirmier> findByPatientIdAndStatut(String patientId, String statut);
}
