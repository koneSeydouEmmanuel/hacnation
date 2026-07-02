package com.hacnation.urgences.application.service;

import com.hacnation.common.enums.NiveauTriage;
import com.hacnation.common.events.NotificationEvent;
import com.hacnation.urgences.domain.model.Triage;
import com.hacnation.urgences.domain.port.NotificationEventPublisherPort;
import com.hacnation.urgences.domain.port.TriageRepositoryPort;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProcessTriageUseCase {

    private static final Logger log = LoggerFactory.getLogger(ProcessTriageUseCase.class);

    private final TriageRepositoryPort triageRepository;
    private final NotificationEventPublisherPort eventPublisher;

    public ProcessTriageUseCase(TriageRepositoryPort triageRepository,
                                 NotificationEventPublisherPort eventPublisher) {
        this.triageRepository = triageRepository;
        this.eventPublisher = eventPublisher;
    }

    public Triage trier(String admissionId, String patientId, NiveauTriage niveauGravite,
                        String constantes, String motif, String orientation) {
        Triage triage = new Triage();
        triage.setAdmissionId(admissionId);
        triage.setPatientId(patientId);
        triage.setNiveauGravite(niveauGravite);
        triage.setConstantes(constantes);
        triage.setMotif(motif);
        triage.setOrientation(orientation);
        triage.setDateTriage(LocalDateTime.now());

        Triage saved = triageRepository.save(triage);

        if (niveauGravite == NiveauTriage.NIVEAU_1_CRITIQUE
                || niveauGravite == NiveauTriage.NIVEAU_2_TRES_URGENT) {
            log.warn("Triage niveau critique/urgent: admissionId={}, patientId={}, niveau={}",
                    admissionId, patientId, niveauGravite);

            NotificationEvent event = new NotificationEvent();
            event.setEventType("TRIAGE_CRITIQUE");
            event.setPatientId(patientId);
            event.setCanal("SYSTEME");
            event.setContenu("Triage critique: " + niveauGravite + " - Patient: " + patientId);
            event.setDestinataire("urgences");
            eventPublisher.publishNotificationEvent(event);
        }

        return saved;
    }
}
