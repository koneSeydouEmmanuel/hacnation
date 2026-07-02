package com.hacnation.hospitalisation.infrastructure.adapter.out.jpa;

import com.hacnation.common.enums.StatutLit;
import com.hacnation.hospitalisation.domain.model.Lit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LitJpaRepository extends JpaRepository<Lit, String> {

    List<Lit> findByServiceAndStatut(String service, StatutLit statut);

    List<Lit> findByService(String service);

    long countByServiceAndStatut(String service, StatutLit statut);
}
