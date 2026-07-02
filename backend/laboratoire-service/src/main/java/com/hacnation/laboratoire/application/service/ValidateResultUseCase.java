package com.hacnation.laboratoire.application.service;

import com.hacnation.common.enums.StatutPrescription;
import com.hacnation.common.events.LabEvent;
import com.hacnation.common.events.NotificationEvent;
import com.hacnation.common.exception.ResourceNotFoundException;
import com.hacnation.laboratoire.domain.model.DemandeAnalyse;
import com.hacnation.laboratoire.domain.port.DemandeAnalyseRepositoryPort;
import com.hacnation.laboratoire.domain.port.LaboEventPublisherPort;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ValidateResultUseCase {

    private static final Logger log = LoggerFactory.getLogger(ValidateResultUseCase.class);

    private final DemandeAnalyseRepositoryPort demandeAnalyseRepository;
    private final LaboEventPublisherPort eventPublisher;

    public ValidateResultUseCase(DemandeAnalyseRepositoryPort demandeAnalyseRepository,
                                  LaboEventPublisherPort eventPublisher) {
        this.demandeAnalyseRepository = demandeAnalyseRepository;
        this.eventPublisher = eventPublisher;
    }

    public DemandeAnalyse validerResultats(String analyseId, String validateurId) {
        DemandeAnalyse analyse = demandeAnalyseRepository.findById(analyseId)
                .orElseThrow(() -> new ResourceNotFoundException("DemandeAnalyse", analyseId));

        analyse.setStatut(StatutPrescription.TERMINEE);
        analyse.setValidateurId(validateurId);
        analyse.setDateValidation(LocalDateTime.now());

        DemandeAnalyse saved = demandeAnalyseRepository.save(analyse);

        LabEvent labEvent = LabEvent.resultAvailable(
                saved.getId(), saved.getPrescriptionId(), saved.getPatientId());
        eventPublisher.publishLabEvent(labEvent);

        NotificationEvent notificationEvent = new NotificationEvent();
        notificationEvent.setEventType("LAB_RESULT_AVAILABLE");
        notificationEvent.setPatientId(saved.getPatientId());
        notificationEvent.setContenu("Vos resultats d'analyse sont disponibles (Analyse: " + saved.getTypeAnalyse() + ")");
        notificationEvent.setCanal("EMAIL");
        eventPublisher.publishNotificationEvent(notificationEvent);

        log.info("Resultats valides pour analyse: id={}, validateur={}", analyseId, validateurId);
        return saved;
    }
}
