package com.hacnation.rdv.infrastructure.adapter.out.jpa;

import com.hacnation.common.enums.StatutRdv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
interface SpringRdvJpaRepository extends JpaRepository<RendezVousEntity, String> {

    List<RendezVousEntity> findByPatientIdOrderByDateHeureDesc(String patientId);

    List<RendezVousEntity> findByDateHeureBetween(LocalDateTime start, LocalDateTime end);

    List<RendezVousEntity> findByServiceAndDateHeureBetween(String service, LocalDateTime start, LocalDateTime end);

    List<RendezVousEntity> findByStatutAndDateHeureBetween(StatutRdv statut, LocalDateTime start, LocalDateTime end);

    List<RendezVousEntity> findByPraticienIdAndDateHeureBetween(String praticienId, LocalDateTime start, LocalDateTime end);
}
