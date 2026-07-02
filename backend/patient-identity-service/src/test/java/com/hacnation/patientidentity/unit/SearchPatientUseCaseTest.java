package com.hacnation.patientidentity.unit;

import com.hacnation.common.dto.PatientDto;
import com.hacnation.common.exception.ResourceNotFoundException;
import com.hacnation.patientidentity.application.service.SearchPatientUseCase;
import com.hacnation.patientidentity.domain.model.Patient;
import com.hacnation.patientidentity.domain.port.PatientRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchPatientUseCaseTest {

    @Mock
    private PatientRepositoryPort patientRepository;

    @InjectMocks
    private SearchPatientUseCase searchPatientUseCase;

    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId("patient-1");
        patient.setNom("Doe");
        patient.setPrenom("John");
        patient.setDateNaissance(LocalDate.of(1990, 1, 1));
        patient.setTelephone("+2250100000001");
        patient.setStatut("ACTIF");
    }

    @Test
    void getPatient_shouldReturnPatient_whenFound() {
        when(patientRepository.findById("patient-1")).thenReturn(Optional.of(patient));

        PatientDto result = searchPatientUseCase.getPatient("patient-1");

        assertNotNull(result);
        assertEquals("Doe", result.getNom());
        assertEquals("John", result.getPrenom());
    }

    @Test
    void getPatient_shouldThrowException_whenNotFound() {
        when(patientRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                searchPatientUseCase.getPatient("nonexistent"));
    }

    @Test
    void searchPatients_shouldReturnAll_whenQueryIsBlank() {
        Page<Patient> patientPage = new PageImpl<>(List.of(patient));
        when(patientRepository.findAll(any())).thenReturn(patientPage);

        Page<PatientDto> results = searchPatientUseCase.searchPatients("", PageRequest.of(0, 10));

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.getTotalElements());
    }

    @Test
    void searchPatients_shouldReturnEmptyPage_whenNoMatches() {
        Page<Patient> emptyPage = new PageImpl<>(Collections.emptyList());
        when(patientRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(
                anyString(), anyString(), any())).thenReturn(emptyPage);

        Page<PatientDto> results = searchPatientUseCase.searchPatients("Unknown", PageRequest.of(0, 10));

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }
}
