package com.hacnation.consultation.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacnation.common.dto.ConsultationDto;
import com.hacnation.common.enums.StatutConsultation;
import com.hacnation.common.events.ConsultationEvent;
import com.hacnation.common.exception.BusinessException;
import com.hacnation.common.exception.ResourceNotFoundException;
import com.hacnation.consultation.domain.model.Consultation;
import com.hacnation.consultation.domain.port.ConsultationEventPublisherPort;
import com.hacnation.consultation.domain.port.ConsultationRepositoryPort;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateConsultationUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateConsultationUseCase.class);

    private final ConsultationRepositoryPort consultationRepository;
    private final ConsultationEventPublisherPort eventPublisher;
    private final ObjectMapper objectMapper;

    public CreateConsultationUseCase(ConsultationRepositoryPort consultationRepository,
                                     ConsultationEventPublisherPort eventPublisher,
                                     ObjectMapper objectMapper) {
        this.consultationRepository = consultationRepository;
        this.eventPublisher = eventPublisher;
        this.objectMapper = objectMapper;
    }

    public ConsultationDto createConsultation(String patientId, String rdvId, String medecinId,
                                              Map<String, String> constantes, String motif) {
        Consultation consultation = new Consultation();
        consultation.setPatientId(patientId);
        consultation.setRdvId(rdvId);
        consultation.setMedecinId(medecinId);
        try {
            consultation.setConstantes(objectMapper.writeValueAsString(constantes));
        } catch (JsonProcessingException e) {
            throw new BusinessException("Erreur de serialisation des constantes: " + e.getMessage());
        }
        consultation.setDiagnostic(motif);
        consultation.setStatut(StatutConsultation.EN_COURS);
        consultation.setDate(LocalDateTime.now());

        Consultation saved = consultationRepository.save(consultation);

        ConsultationEvent event = ConsultationEvent.created(saved.getId(), saved.getPatientId(), saved.getMedecinId());
        eventPublisher.publishConsultationEvent(event);

        log.info("Consultation creee: {}", saved.getId());
        return toDto(saved);
    }

    public ConsultationDto getConsultation(String id) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation", id));
        return toDto(consultation);
    }

    public Page<ConsultationDto> getConsultationsByPatient(String patientId, Pageable pageable) {
        return consultationRepository.findByPatientIdOrderByDateDesc(patientId, pageable)
                .map(this::toDto);
    }

    public Page<ConsultationDto> getConsultationsByMedecin(String medecinId, Pageable pageable) {
        return consultationRepository.findByMedecinIdOrderByDateDesc(medecinId, pageable)
                .map(this::toDto);
    }

    public ConsultationDto updateConsultation(String id, Map<String, String> constantes,
                                              String diagnostic, String compteRendu) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation", id));

        if (constantes != null) {
            try {
                consultation.setConstantes(objectMapper.writeValueAsString(constantes));
            } catch (JsonProcessingException e) {
                throw new BusinessException("Erreur de serialisation des constantes: " + e.getMessage());
            }
        }
        if (diagnostic != null) {
            consultation.setDiagnostic(diagnostic);
        }
        if (compteRendu != null) {
            consultation.setCompteRendu(compteRendu);
        }

        Consultation updated = consultationRepository.save(consultation);

        ConsultationEvent event = ConsultationEvent.created(updated.getId(), updated.getPatientId(), updated.getMedecinId());
        event.setEventType("CONSULTATION_UPDATED");
        eventPublisher.publishConsultationEvent(event);

        log.info("Consultation mise a jour: {}", id);
        return toDto(updated);
    }

    public ConsultationDto toDto(Consultation entity) {
        ConsultationDto dto = new ConsultationDto();
        dto.setId(entity.getId());
        dto.setPatientId(entity.getPatientId());
        dto.setRdvId(entity.getRdvId());
        dto.setMedecinId(entity.getMedecinId());
        if (entity.getConstantes() != null) {
            try {
                Map<String, String> map = objectMapper.readValue(entity.getConstantes(),
                        new TypeReference<Map<String, String>>() {});
                dto.setConstantes(map);
            } catch (JsonProcessingException e) {
                dto.setConstantes(Collections.emptyMap());
            }
        }
        dto.setDiagnostic(entity.getDiagnostic());
        dto.setCompteRendu(entity.getCompteRendu());
        dto.setStatut(entity.getStatut());
        dto.setDate(entity.getDate());
        return dto;
    }
}
