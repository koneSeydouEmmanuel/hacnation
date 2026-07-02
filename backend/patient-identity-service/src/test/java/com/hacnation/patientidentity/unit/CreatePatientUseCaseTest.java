package com.hacnation.patientidentity.unit;

import com.hacnation.common.dto.PatientDto;
import com.hacnation.common.exception.BusinessException;
import com.hacnation.patientidentity.application.service.CreatePatientUseCase;
import com.hacnation.patientidentity.domain.model.Patient;
import com.hacnation.patientidentity.domain.port.PatientEventPublisherPort;
import com.hacnation.patientidentity.domain.port.PatientRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
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
class CreatePatientUseCaseTest {

    @Mock
    private PatientRepositoryPort patientRepository;

    @Mock
    private PatientEventPublisherPort eventPublisher;

    @InjectMocks
    private CreatePatientUseCase createPatientUseCase;

    private PatientDto patientDto;

    @BeforeEach
    void setUp() {
        patientDto = new PatientDto();
        patientDto.setNom("Doe");
        patientDto.setPrenom("John");
        patientDto.setDateNaissance(LocalDate.of(1990, 1, 1));
        patientDto.setSexe("M");
        patientDto.setTelephone("+2250100000001");
        patientDto.setEmail("john.doe@example.com");
    }

    @Test
    void execute_shouldCreatePatientAndPublishEvent() {
        when(patientRepository.findByNomContainingIgnoreCaseAndPrenomContainingIgnoreCaseAndDateNaissance(
                eq("Doe"), eq("John"), any())).thenReturn(Collections.emptyList());

        Patient savedPatient = new Patient();
        savedPatient.setId("patient-1");
        savedPatient.setNom("Doe");
        savedPatient.setPrenom("John");
        savedPatient.setDateNaissance(LocalDate.of(1990, 1, 1));
        savedPatient.setStatut("ACTIF");

        when(patientRepository.save(any(Patient.class))).thenReturn(savedPatient);

        PatientDto result = createPatientUseCase.execute(patientDto);

        assertNotNull(result);
        assertEquals("Doe", result.getNom());
        assertEquals("ACTIF", result.getStatut());
        verify(eventPublisher).publishPatientCreated(eq("patient-1"), eq("Doe"), eq("John"), eq("+2250100000001"));
    }

    @Test
    void execute_shouldThrow409_whenDuplicateExists() {
        Patient existingPatient = new Patient();
        existingPatient.setId("existing-1");
        existingPatient.setNom("Doe");
        existingPatient.setPrenom("John");
        existingPatient.setDateNaissance(LocalDate.of(1990, 1, 1));
        existingPatient.setStatut("ACTIF");

        when(patientRepository.findByNomContainingIgnoreCaseAndPrenomContainingIgnoreCaseAndDateNaissance(
                eq("Doe"), eq("John"), any())).thenReturn(java.util.List.of(existingPatient));

        BusinessException exception = assertThrows(BusinessException.class, () ->
                createPatientUseCase.execute(patientDto));

        assertEquals(409, exception.getStatus());
        verify(patientRepository, never()).save(any(Patient.class));
        verify(eventPublisher, never()).publishPatientCreated(anyString(), anyString(), anyString(), anyString());
    }
}
