package com.hacnation.prescription.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacnation.common.dto.PrescriptionDto;
import com.hacnation.common.enums.StatutPrescription;
import com.hacnation.common.enums.TypePrescription;
import com.hacnation.common.events.PrescriptionEvent;
import com.hacnation.common.exception.BusinessException;
import com.hacnation.common.exception.ResourceNotFoundException;
import com.hacnation.prescription.domain.model.Prescription;
import com.hacnation.prescription.domain.port.PrescriptionEventPublisherPort;
import com.hacnation.prescription.domain.port.PrescriptionRepositoryPort;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreatePrescriptionUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreatePrescriptionUseCase.class);

    private final PrescriptionRepositoryPort prescriptionRepository;
    private final PrescriptionEventPublisherPort eventPublisher;
    private final ObjectMapper objectMapper;

    public CreatePrescriptionUseCase(PrescriptionRepositoryPort prescriptionRepository,
                                     PrescriptionEventPublisherPort eventPublisher,
                                     ObjectMapper objectMapper) {
        this.prescriptionRepository = prescriptionRepository;
        this.eventPublisher = eventPublisher;
        this.objectMapper = objectMapper;
    }

    public PrescriptionDto createPrescription(String consultationId, TypePrescription type,
                                              Map<String, Object> details, String patientId) {
        Prescription prescription = new Prescription();
        prescription.setConsultationId(consultationId);
        prescription.setType(type);
        try {
            prescription.setDetails(objectMapper.writeValueAsString(details));
        } catch (JsonProcessingException e) {
            throw new BusinessException("Erreur de serialisation des details: " + e.getMessage());
        }
        prescription.setStatut(StatutPrescription.EN_ATTENTE);

        Prescription saved = prescriptionRepository.save(prescription);

        PrescriptionEvent event = PrescriptionEvent.created(saved.getId(), consultationId,
                patientId, type.name());
        eventPublisher.publishPrescriptionEvent(event);

        log.info("Prescription creee: {} pour consultation {}", saved.getId(), consultationId);
        return toDto(saved);
    }

    public List<PrescriptionDto> getPrescriptionsByConsultation(String consultationId) {
        return prescriptionRepository.findByConsultationId(consultationId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public PrescriptionDto getPrescription(String id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription", id));
        return toDto(prescription);
    }

    public PrescriptionDto updatePrescriptionStatus(String id, StatutPrescription newStatut) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription", id));
        prescription.setStatut(newStatut);
        Prescription updated = prescriptionRepository.save(prescription);

        log.info("Statut prescription mis a jour: {} -> {}", id, newStatut);
        return toDto(updated);
    }

    public PrescriptionDto toDto(Prescription entity) {
        PrescriptionDto dto = new PrescriptionDto();
        dto.setId(entity.getId());
        dto.setConsultationId(entity.getConsultationId());
        dto.setType(entity.getType());
        if (entity.getDetails() != null) {
            try {
                Map<String, Object> map = objectMapper.readValue(entity.getDetails(),
                        new TypeReference<Map<String, Object>>() {});
                dto.setDetails(map);
            } catch (JsonProcessingException e) {
                dto.setDetails(Collections.emptyMap());
            }
        }
        dto.setStatut(entity.getStatut());
        dto.setDateCreation(entity.getDateCreation());
        return dto;
    }
}
