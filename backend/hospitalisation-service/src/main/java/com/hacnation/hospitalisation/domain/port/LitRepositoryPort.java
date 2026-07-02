package com.hacnation.hospitalisation.domain.port;

import com.hacnation.common.enums.StatutLit;
import com.hacnation.hospitalisation.domain.model.Lit;

import java.util.List;
import java.util.Optional;

public interface LitRepositoryPort {

    Lit save(Lit lit);

    Optional<Lit> findById(String id);

    List<Lit> findByServiceAndStatut(String service, StatutLit statut);

    List<Lit> findByService(String service);

    long countByServiceAndStatut(String service, StatutLit statut);
}
