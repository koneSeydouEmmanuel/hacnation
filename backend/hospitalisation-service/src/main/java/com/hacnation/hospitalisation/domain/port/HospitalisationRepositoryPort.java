package com.hacnation.hospitalisation.domain.port;

import com.hacnation.common.enums.StatutLit;
import com.hacnation.hospitalisation.domain.model.Hospitalisation;
import com.hacnation.hospitalisation.domain.model.Lit;

import java.util.List;
import java.util.Optional;

public interface HospitalisationRepositoryPort {

    Hospitalisation save(Hospitalisation hospitalisation);

    Optional<Hospitalisation> findById(String id);

    List<Hospitalisation> findByLitIdAndStatut(String litId, String statut);

    List<Hospitalisation> findByStatut(String statut);
}
