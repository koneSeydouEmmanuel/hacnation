package com.hacnation.dme.unit;

import com.hacnation.common.exception.ResourceNotFoundException;
import com.hacnation.dme.application.service.GetDmeUseCase;
import com.hacnation.dme.domain.model.DossierMedical;
import com.hacnation.dme.domain.port.DmeRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetDmeUseCaseTest {

    @Mock
    private DmeRepositoryPort dmeRepository;

    @InjectMocks
    private GetDmeUseCase getDmeUseCase;

    private DossierMedical dme;

    @BeforeEach
    void setUp() {
        dme = new DossierMedical();
        dme.setId("dme-1");
        dme.setPatientId("patient-1");
        dme.setAntecedents("[]");
        dme.setNotesCliniques("[]");
        dme.setDocuments("[]");
    }

    @Test
    void execute_shouldReturnDmeMap_whenFound() {
        when(dmeRepository.findByPatientId("patient-1")).thenReturn(Optional.of(dme));

        Map<String, Object> result = getDmeUseCase.execute("patient-1");

        assertNotNull(result);
        assertEquals("dme-1", result.get("id"));
        assertEquals("patient-1", result.get("patientId"));
    }

    @Test
    void execute_shouldThrowException_whenNotFound() {
        when(dmeRepository.findByPatientId("nonexistent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                getDmeUseCase.execute("nonexistent"));
    }

    @Test
    void createDme_shouldSaveAndReturnDme() {
        when(dmeRepository.save(any(DossierMedical.class))).thenReturn(dme);

        DossierMedical result = getDmeUseCase.createDme("patient-1");

        assertNotNull(result);
        assertEquals("patient-1", result.getPatientId());
        verify(dmeRepository).save(any(DossierMedical.class));
    }

    @Test
    void addAntecedent_shouldAddToDme() {
        when(dmeRepository.findByPatientId("patient-1")).thenReturn(Optional.of(dme));
        when(dmeRepository.save(any(DossierMedical.class))).thenReturn(dme);

        Map<String, Object> antecedent = Map.of("type", "Allergie", "description", "Penicilline");
        Map<String, Object> result = getDmeUseCase.addAntecedent("patient-1", antecedent);

        assertNotNull(result);
        verify(dmeRepository, atLeastOnce()).save(any(DossierMedical.class));
    }
}
