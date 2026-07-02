package com.hacnation.hospitalisation.unit;

import com.hacnation.common.enums.StatutLit;
import com.hacnation.common.exception.BusinessException;
import com.hacnation.hospitalisation.application.service.ManageHospitalisationUseCase;
import com.hacnation.hospitalisation.domain.model.Hospitalisation;
import com.hacnation.hospitalisation.domain.model.Lit;
import com.hacnation.hospitalisation.domain.port.AdmissionEventPublisherPort;
import com.hacnation.hospitalisation.domain.port.HospitalisationRepositoryPort;
import com.hacnation.hospitalisation.domain.port.LitRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManageHospitalisationUseCaseTest {

    @Mock
    private HospitalisationRepositoryPort hospitalisationRepository;

    @Mock
    private LitRepositoryPort litRepository;

    @Mock
    private AdmissionEventPublisherPort eventPublisher;

    @InjectMocks
    private ManageHospitalisationUseCase manageHospitalisationUseCase;

    @Test
    void admettre_shouldAllocateBed_whenAvailable() {
        Lit lit = new Lit();
        lit.setId("lit-1");
        lit.setService("Chirurgie");
        lit.setStatut(StatutLit.DISPONIBLE);

        when(litRepository.findByServiceAndStatut("Chirurgie", StatutLit.DISPONIBLE))
                .thenReturn(List.of(lit));

        Hospitalisation saved = new Hospitalisation();
        saved.setId("hosp-1");
        saved.setPatientId("patient-1");
        saved.setLitId("lit-1");
        saved.setStatut("EN_COURS");

        when(hospitalisationRepository.save(any(Hospitalisation.class))).thenReturn(saved);

        Hospitalisation result = manageHospitalisationUseCase.admettre(
                "adm-1", "patient-1", "Chirurgie", "Operation planifiee");

        assertNotNull(result);
        assertEquals("lit-1", result.getLitId());
        assertEquals("EN_COURS", result.getStatut());
        verify(litRepository).save(any(Lit.class));
    }

    @Test
    void admettre_shouldThrow409_whenNoBedAvailable() {
        when(litRepository.findByServiceAndStatut("Chirurgie", StatutLit.DISPONIBLE))
                .thenReturn(Collections.emptyList());

        BusinessException exception = assertThrows(BusinessException.class, () ->
                manageHospitalisationUseCase.admettre("adm-1", "patient-1", "Chirurgie", "Operation"));

        assertEquals(409, exception.getStatus());
        verify(hospitalisationRepository, never()).save(any());
    }

    @Test
    void getLits_shouldReturnLits() {
        when(litRepository.findByService("Chirurgie")).thenReturn(Collections.emptyList());

        List<Lit> lits = manageHospitalisationUseCase.getLits("Chirurgie");

        assertNotNull(lits);
        assertTrue(lits.isEmpty());
    }

    @Test
    void getLitsDisponibles_shouldReturnCount() {
        when(litRepository.countByServiceAndStatut("Chirurgie", StatutLit.DISPONIBLE))
                .thenReturn(3L);

        long count = manageHospitalisationUseCase.getLitsDisponibles("Chirurgie");

        assertEquals(3L, count);
    }
}
