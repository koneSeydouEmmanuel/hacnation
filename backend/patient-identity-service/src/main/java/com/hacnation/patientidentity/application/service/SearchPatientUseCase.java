package com.hacnation.patientidentity.application.service;

import com.hacnation.common.dto.PatientDto;
import com.hacnation.common.exception.ResourceNotFoundException;
import com.hacnation.patientidentity.domain.model.Patient;
import com.hacnation.patientidentity.domain.port.PatientRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SearchPatientUseCase {

    private final PatientRepositoryPort patientRepository;

    public SearchPatientUseCase(PatientRepositoryPort patientRepository) {
        this.patientRepository = patientRepository;
    }

    public PatientDto getPatient(String id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", id));
        return toDto(patient);
    }

    public Page<PatientDto> searchPatients(String query, Pageable pageable) {
        if (query == null || query.isBlank()) {
            return patientRepository.findAll(pageable).map(this::toDto);
        }
        String q = query.trim();
        return patientRepository
                .findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(q, q, pageable)
                .map(this::toDto);
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
