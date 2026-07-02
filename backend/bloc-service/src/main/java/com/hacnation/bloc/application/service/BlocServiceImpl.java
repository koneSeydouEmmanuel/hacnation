package com.hacnation.bloc.application.service;

import com.hacnation.common.exception.BusinessException;
import com.hacnation.common.exception.ResourceNotFoundException;
import com.hacnation.bloc.domain.model.InterventionChirurgicale;
import com.hacnation.bloc.domain.port.inbound.BlocUseCase;
import com.hacnation.bloc.domain.port.outbound.InterventionChirurgicaleRepositoryPort;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BlocServiceImpl implements BlocUseCase {

    private static final Logger log = LoggerFactory.getLogger(BlocServiceImpl.class);

    private final InterventionChirurgicaleRepositoryPort interventionRepository;

    public BlocServiceImpl(InterventionChirurgicaleRepositoryPort interventionRepository) {
        this.interventionRepository = interventionRepository;
    }

    @Override
    @Transactional
    public InterventionChirurgicale programmerIntervention(String patientId, String typeIntervention,
                                                           String chirurgienId, String salle,
                                                           LocalDateTime dateHeure, Integer dureeEstimee) {
        LocalDateTime finEstimee = dateHeure.plusMinutes(dureeEstimee != null ? dureeEstimee : 60);
        List<InterventionChirurgicale> conflits = interventionRepository
                .findBySalleAndDateHeureBetween(salle, dateHeure, finEstimee);

        if (!conflits.isEmpty()) {
            throw new BusinessException(
                    "Conflit de salle: la salle " + salle + " est deja occupee sur ce creneau", 409);
        }

        InterventionChirurgicale intervention = new InterventionChirurgicale();
        intervention.setPatientId(patientId);
        intervention.setTypeIntervention(typeIntervention);
        intervention.setChirurgienId(chirurgienId);
        intervention.setSalle(salle);
        intervention.setDateHeure(dateHeure);
        intervention.setDureeEstimee(dureeEstimee);
        intervention.setStatut("PROGRAMMEE");

        InterventionChirurgicale saved = interventionRepository.save(intervention);

        log.info("Intervention programmee: id={}, patientId={}, salle={}, dateHeure={}",
                saved.getId(), patientId, salle, dateHeure);
        return saved;
    }

    @Override
    @Transactional
    public InterventionChirurgicale updateStatutIntervention(String id, String newStatut) {
        InterventionChirurgicale intervention = interventionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InterventionChirurgicale", id));

        intervention.setStatut(newStatut);
        InterventionChirurgicale saved = interventionRepository.save(intervention);

        log.info("Statut intervention mis a jour: id={}, statut={}", id, newStatut);
        return saved;
    }

    @Override
    public List<InterventionChirurgicale> getPlanningSalle(String salle, LocalDate date) {
        LocalDateTime debut = date.atStartOfDay();
        LocalDateTime fin = date.atTime(LocalTime.MAX);
        return interventionRepository.findBySalleAndDateHeureBetween(salle, debut, fin);
    }
}
