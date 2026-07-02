package com.hacnation.patientidentity.application.service;

import com.hacnation.common.dto.PatientDto;
import com.hacnation.common.exception.BusinessException;
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
public class CreatePatientUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreatePatientUseCase.class);

    private final PatientRepositoryPort patientRepository;
    private final PatientEventPublisherPort eventPublisher;

    public CreatePatientUseCase(PatientRepositoryPort patientRepository,
                                PatientEventPublisherPort eventPublisher) {
        this.patientRepository = patientRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public PatientDto execute(PatientDto dto) {
        checkDuplicate(dto.getNom(), dto.getPrenom(), dto.getDateNaissance());

        Patient patient = fromDto(dto);
        patient.setStatut("ACTIF");
        Patient saved = patientRepository.save(patient);

        eventPublisher.publishPatientCreated(saved.getId(), saved.getNom(),
                saved.getPrenom(), saved.getTelephone());

        log.info("Patient cree: {} {}", saved.getNom(), saved.getPrenom());
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

    public PatientDto toDto(Patient entity) {
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

    private Patient fromDto(PatientDto dto) {
        Patient entity = new Patient();
        entity.setNom(dto.getNom());
        entity.setPrenom(dto.getPrenom());
        entity.setDateNaissance(dto.getDateNaissance());
        entity.setSexe(dto.getSexe());
        entity.setTelephone(dto.getTelephone());
        entity.setEmail(dto.getEmail());
        entity.setAdresse(dto.getAdresse());
        entity.setGroupeSanguin(dto.getGroupeSanguin());
        return entity;
    }
}
