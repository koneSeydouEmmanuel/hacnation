package com.hacnation.patientidentity.unit;

import com.hacnation.common.dto.PatientDto;
import com.hacnation.common.exception.BusinessException;
import com.hacnation.common.exception.ResourceNotFoundException;
import com.hacnation.patientidentity.application.service.UpdatePatientUseCase;
import com.hacnation.patientidentity.domain.model.Patient;
import com.hacnation.patientidentity.domain.port.PatientEventPublisherPort;
import com.hacnation.patientidentity.domain.port.PatientRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdatePatientUseCaseTest {

    @Mock
    private PatientRepositoryPort patientRepository;

    @Mock
    private PatientEventPublisherPort eventPublisher;

    @InjectMocks
    private UpdatePatientUseCase updatePatientUseCase;

    private Patient existingPatient;
    private PatientDto updatedDto;

    @BeforeEach
    void setUp() {
        existingPatient = new Patient();
        existingPatient.setId("patient-1");
        existingPatient.setNom("Doe");
        existingPatient.setPrenom("John");
        existingPatient.setDateNaissance(LocalDate.of(1990, 1, 1));
        existingPatient.setSexe("M");
        existingPatient.setTelephone("+2250100000001");
        existingPatient.setEmail("john.doe@example.com");
        existingPatient.setStatut("ACTIF");

        updatedDto = new PatientDto();
        updatedDto.setNom("Doe");
        updatedDto.setPrenom("Johnny");
        updatedDto.setTelephone("+2250100000002");
    }

    @Test
    @DisplayName("Should update patient fields and publish event when patient found")
    void updatePatient_shouldUpdateFieldsAndPublishEvent() {
        when(patientRepository.findById("patient-1")).thenReturn(Optional.of(existingPatient));

        Patient savedPatient = new Patient();
        savedPatient.setId("patient-1");
        savedPatient.setNom("Doe");
        savedPatient.setPrenom("Johnny");
        savedPatient.setDateNaissance(LocalDate.of(1990, 1, 1));
        savedPatient.setTelephone("+2250100000002");
        savedPatient.setStatut("ACTIF");

        when(patientRepository.save(any(Patient.class))).thenReturn(savedPatient);

        PatientDto result = updatePatientUseCase.updatePatient("patient-1", updatedDto);

        assertNotNull(result);
        assertEquals("Johnny", result.getPrenom());
        assertEquals("+2250100000002", result.getTelephone());
        verify(eventPublisher).publishPatientUpdated(eq("patient-1"), eq("Doe"), eq("Johnny"), eq("+2250100000002"));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when patient not found")
    void updatePatient_shouldThrowException_whenPatientNotFound() {
        when(patientRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                updatePatientUseCase.updatePatient("nonexistent", updatedDto));

        verify(patientRepository, never()).save(any(Patient.class));
        verify(eventPublisher, never()).publishPatientUpdated(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Should delete patient by setting status to INACTIF")
    void deletePatient_shouldSetStatusInactif() {
        when(patientRepository.findById("patient-1")).thenReturn(Optional.of(existingPatient));

        Patient savedPatient = new Patient();
        savedPatient.setId("patient-1");
        savedPatient.setNom("Doe");
        savedPatient.setPrenom("John");
        savedPatient.setStatut("INACTIF");

        when(patientRepository.save(any(Patient.class))).thenReturn(savedPatient);

        PatientDto result = updatePatientUseCase.deletePatient("patient-1");

        assertNotNull(result);
        assertEquals("INACTIF", result.getStatut());
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent patient")
    void deletePatient_shouldThrowException_whenPatientNotFound() {
        when(patientRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                updatePatientUseCase.deletePatient("nonexistent"));

        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    @DisplayName("Should throw BusinessException when updating with duplicate identity")
    void updatePatient_shouldThrowException_whenDuplicateExists() {
        PatientDto dtoWithNewName = new PatientDto();
        dtoWithNewName.setNom("Smith");
        dtoWithNewName.setPrenom("Jane");
        dtoWithNewName.setDateNaissance(LocalDate.of(1995, 5, 5));

        when(patientRepository.findById("patient-1")).thenReturn(Optional.of(existingPatient));

        Patient duplicate = new Patient();
        duplicate.setId("patient-2");
        duplicate.setNom("Smith");
        duplicate.setPrenom("Jane");
        duplicate.setDateNaissance(LocalDate.of(1995, 5, 5));
        duplicate.setStatut("ACTIF");

        when(patientRepository.findByNomContainingIgnoreCaseAndPrenomContainingIgnoreCaseAndDateNaissance(
                eq("Smith"), eq("Jane"), any()))
                .thenReturn(java.util.List.of(duplicate));

        BusinessException exception = assertThrows(BusinessException.class, () ->
                updatePatientUseCase.updatePatient("patient-1", dtoWithNewName));

        assertEquals(409, exception.getStatus());
        verify(patientRepository, never()).save(any(Patient.class));
    }
}
