package com.hacnation.patientidentity.application.service;

import com.hacnation.common.dto.PatientDto;
import com.hacnation.common.exception.BusinessException;
import com.hacnation.common.exception.ResourceNotFoundException;
import com.hacnation.patientidentity.domain.model.Patient;
import com.hacnation.patientidentity.domain.port.PatientEventPublisherPort;
import com.hacnation.patientidentity.domain.port.PatientRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UpdatePatientUseCase {

    private static final Logger log = LoggerFactory.getLogger(UpdatePatientUseCase.class);

    private final PatientRepositoryPort patientRepository;
    private final PatientEventPublisherPort eventPublisher;

    public UpdatePatientUseCase(PatientRepositoryPort patientRepository,
                                PatientEventPublisherPort eventPublisher) {
        this.patientRepository = patientRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public PatientDto updatePatient(String id, PatientDto dto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", id));

        if (dto.getNom() != null && dto.getPrenom() != null && dto.getDateNaissance() != null) {
            if (!patient.getNom().equalsIgnoreCase(dto.getNom())
                    || !patient.getPrenom().equalsIgnoreCase(dto.getPrenom())
                    || (patient.getDateNaissance() != null
                        && !patient.getDateNaissance().equals(dto.getDateNaissance()))) {
                checkDuplicate(dto.getNom(), dto.getPrenom(), dto.getDateNaissance());
            }
        }

        updateEntity(patient, dto);
        Patient saved = patientRepository.save(patient);

        eventPublisher.publishPatientUpdated(saved.getId(), saved.getNom(),
                saved.getPrenom(), saved.getTelephone());

        log.info("Patient mis a jour: {}", id);
        return toDto(saved);
    }

    @Transactional
    public PatientDto deletePatient(String id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", id));
        patient.setStatut("INACTIF");
        Patient saved = patientRepository.save(patient);
        log.info("Patient desactive: {}", id);
        return toDto(saved);
    }

    private void checkDuplicate(String nom, String prenom, java.time.LocalDate dateNaissance) {
        List<Patient> duplicates = patientRepository
                .findByNomContainingIgnoreCaseAndPrenomContainingIgnoreCaseAndDateNaissance(
                        nom, prenom, dateNaissance);
        if (!duplicates.isEmpty()) {
            List<PatientDto> matches = duplicates.stream()
                    .filter(p -> p.getStatut() == null || !p.getStatut().equals("INACTIF"))
                    .map(this::toDto)
                    .collect(Collectors.toList());
            if (!matches.isEmpty()) {
                throw new BusinessException(
                        "Un patient avec les memes nom, prenom et date de naissance existe deja",
                        409);
            }
        }
    }

    private void updateEntity(Patient entity, PatientDto dto) {
        if (dto.getNom() != null) entity.setNom(dto.getNom());
        if (dto.getPrenom() != null) entity.setPrenom(dto.getPrenom());
        if (dto.getDateNaissance() != null) entity.setDateNaissance(dto.getDateNaissance());
        if (dto.getSexe() != null) entity.setSexe(dto.getSexe());
        if (dto.getTelephone() != null) entity.setTelephone(dto.getTelephone());
        if (dto.getEmail() != null) entity.setEmail(dto.getEmail());
        if (dto.getAdresse() != null) entity.setAdresse(dto.getAdresse());
        if (dto.getGroupeSanguin() != null) entity.setGroupeSanguin(dto.getGroupeSanguin());
        if (dto.getStatut() != null) entity.setStatut(dto.getStatut());
    }

    private PatientDto toDto(Patient entity) {
        PatientDto dto = new PatientDto();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        dto.setPrenom(entity.getPrenom());
        dto.setDateNaissance(entity.getDateNaissance());
        dto.setSexe(entity.getSexe());
        dto.setTelephone(entity.getTelephone());
        dto.setEmail(entity.getEmail());
        dto.setAdresse(entity.getAdresse());
        dto.setGroupeSanguin(entity.getGroupeSanguin());
        dto.setStatut(entity.getStatut());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
