package com.hacnation.consultation.application.service;

import com.hacnation.common.dto.ConsultationDto;
import com.hacnation.common.enums.StatutConsultation;
import com.hacnation.common.events.ConsultationEvent;
import com.hacnation.common.exception.ResourceNotFoundException;
import com.hacnation.consultation.domain.model.Consultation;
import com.hacnation.consultation.domain.port.ConsultationEventPublisherPort;
import com.hacnation.consultation.domain.port.ConsultationRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TerminateConsultationUseCase {

    private static final Logger log = LoggerFactory.getLogger(TerminateConsultationUseCase.class);

    private final ConsultationRepositoryPort consultationRepository;
    private final ConsultationEventPublisherPort eventPublisher;
    private final CreateConsultationUseCase createConsultationUseCase;

    public TerminateConsultationUseCase(ConsultationRepositoryPort consultationRepository,
                                        ConsultationEventPublisherPort eventPublisher,
                                        CreateConsultationUseCase createConsultationUseCase) {
        this.consultationRepository = consultationRepository;
        this.eventPublisher = eventPublisher;
        this.createConsultationUseCase = createConsultationUseCase;
    }

    public ConsultationDto terminerConsultation(String id) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation", id));

        consultation.setStatut(StatutConsultation.TERMINEE);

        Consultation updated = consultationRepository.save(consultation);

        ConsultationEvent event = ConsultationEvent.terminated(updated.getId(), updated.getPatientId());
        eventPublisher.publishConsultationEvent(event);

        log.info("Consultation terminee: {}", id);
        return createConsultationUseCase.toDto(updated);
    }
}
