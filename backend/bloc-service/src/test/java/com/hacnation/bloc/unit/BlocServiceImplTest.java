package com.hacnation.bloc.unit;

import com.hacnation.common.exception.BusinessException;
import com.hacnation.bloc.application.service.BlocServiceImpl;
import com.hacnation.bloc.domain.model.InterventionChirurgicale;
import com.hacnation.bloc.domain.port.outbound.InterventionChirurgicaleRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlocServiceImplTest {

    @Mock
    private InterventionChirurgicaleRepositoryPort interventionRepository;

    @InjectMocks
    private BlocServiceImpl blocService;

    @Test
    void programmerIntervention_shouldSchedule() {
        when(interventionRepository.findBySalleAndDateHeureBetween(anyString(), any(), any()))
                .thenReturn(Collections.emptyList());

        InterventionChirurgicale saved = new InterventionChirurgicale();
        saved.setId("interv-1");
        saved.setPatientId("patient-1");
        saved.setTypeIntervention("APPENDICECTOMIE");
        saved.setChirurgienId("chir-1");
        saved.setSalle("Bloc A");
        saved.setDateHeure(LocalDateTime.of(2026, 7, 3, 10, 0));
        saved.setDureeEstimee(60);
        saved.setStatut("PROGRAMMEE");

        when(interventionRepository.save(any(InterventionChirurgicale.class))).thenReturn(saved);

        InterventionChirurgicale result = blocService.programmerIntervention(
                "patient-1", "APPENDICECTOMIE", "chir-1", "Bloc A",
                LocalDateTime.of(2026, 7, 3, 10, 0), 60);

        assertNotNull(result);
        assertEquals("PROGRAMMEE", result.getStatut());
        assertEquals("Bloc A", result.getSalle());
    }

    @Test
    void programmerIntervention_shouldThrow409_whenRoomConflict() {
        InterventionChirurgicale existing = new InterventionChirurgicale();
        existing.setId("interv-1");
        existing.setSalle("Bloc A");
        existing.setStatut("PROGRAMMEE");

        when(interventionRepository.findBySalleAndDateHeureBetween(anyString(), any(), any()))
                .thenReturn(List.of(existing));

        BusinessException exception = assertThrows(BusinessException.class, () ->
                blocService.programmerIntervention("patient-1", "APPENDICECTOMIE", "chir-1", "Bloc A",
                        LocalDateTime.now(), 60));

        assertEquals(409, exception.getStatus());
        verify(interventionRepository, never()).save(any());
    }

    @Test
    void updateStatutIntervention_shouldUpdateStatus() {
        InterventionChirurgicale existing = new InterventionChirurgicale();
        existing.setId("interv-1");
        existing.setSalle("Bloc A");
        existing.setStatut("PROGRAMMEE");

        InterventionChirurgicale updated = new InterventionChirurgicale();
        updated.setId("interv-1");
        updated.setSalle("Bloc A");
        updated.setStatut("EN_COURS");

        when(interventionRepository.findById("interv-1")).thenReturn(java.util.Optional.of(existing));
        when(interventionRepository.save(any(InterventionChirurgicale.class))).thenReturn(updated);

        InterventionChirurgicale result = blocService.updateStatutIntervention("interv-1", "EN_COURS");

        assertNotNull(result);
        assertEquals("EN_COURS", result.getStatut());
    }
}
