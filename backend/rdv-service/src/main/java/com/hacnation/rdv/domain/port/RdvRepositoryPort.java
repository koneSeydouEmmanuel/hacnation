package com.hacnation.rdv.domain.port;

import com.hacnation.common.enums.StatutRdv;
import com.hacnation.rdv.domain.model.RendezVous;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RdvRepositoryPort {

    RendezVous save(RendezVous rdv);

    Optional<RendezVous> findById(String id);

    List<RendezVous> findByPatientIdOrderByDateHeureDesc(String patientId);

    List<RendezVous> findByDateHeureBetween(LocalDateTime start, LocalDateTime end);

    List<RendezVous> findByServiceAndDateHeureBetween(String service, LocalDateTime start, LocalDateTime end);

    List<RendezVous> findByStatutAndDateHeureBetween(StatutRdv statut, LocalDateTime start, LocalDateTime end);

    List<RendezVous> findByPraticienIdAndDateHeureBetween(String praticienId, LocalDateTime start, LocalDateTime end);
}
