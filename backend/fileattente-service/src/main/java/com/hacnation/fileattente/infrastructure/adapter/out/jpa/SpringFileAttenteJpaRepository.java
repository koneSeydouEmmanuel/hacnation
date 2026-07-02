package com.hacnation.fileattente.infrastructure.adapter.out.jpa;

import com.hacnation.common.enums.StatutFileAttente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
interface SpringFileAttenteJpaRepository extends JpaRepository<FileAttenteEntity, String> {

    List<FileAttenteEntity> findByServiceAndStatutOrderByPositionAsc(String service, StatutFileAttente statut);

    Optional<FileAttenteEntity> findByPatientIdAndStatut(String patientId, StatutFileAttente statut);

    Integer countByServiceAndStatut(String service, StatutFileAttente statut);
}
