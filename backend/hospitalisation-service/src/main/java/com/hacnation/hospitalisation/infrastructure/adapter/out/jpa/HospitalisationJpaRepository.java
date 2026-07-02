package com.hacnation.hospitalisation.infrastructure.adapter.out.jpa;

import com.hacnation.hospitalisation.domain.model.Hospitalisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalisationJpaRepository extends JpaRepository<Hospitalisation, String> {

    List<Hospitalisation> findByLitIdAndStatut(String litId, String statut);

    List<Hospitalisation> findByStatut(String statut);
}
