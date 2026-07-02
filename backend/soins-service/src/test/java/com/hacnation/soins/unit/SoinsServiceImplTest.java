package com.hacnation.soins.unit;

import com.hacnation.soins.application.service.SoinsServiceImpl;
import com.hacnation.soins.domain.model.SoinInfirmier;
import com.hacnation.soins.domain.port.outbound.SoinInfirmierRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SoinsServiceImplTest {

    @Mock
    private SoinInfirmierRepositoryPort soinInfirmierRepository;

    @InjectMocks
    private SoinsServiceImpl soinsService;

    @Test
    void creerPlanSoins_shouldCreateAndReturn() {
        SoinInfirmier saved = new SoinInfirmier();
        saved.setId("soin-1");
        saved.setHospitalisationId("hosp-1");
        saved.setPatientId("patient-1");
        saved.setTypeSoin("PANSEMENT");
        saved.setDescription("Pansement plaie");
        saved.setFrequence("2x/jour");
        saved.setStatut("EN_ATTENTE");

        when(soinInfirmierRepository.save(any(SoinInfirmier.class))).thenReturn(saved);

        SoinInfirmier result = soinsService.creerPlanSoins("hosp-1", "patient-1",
                "PANSEMENT", "Pansement plaie", "2x/jour", "Utiliser compresses steriles");

        assertNotNull(result);
        assertEquals("PANSEMENT", result.getTypeSoin());
        assertEquals("EN_ATTENTE", result.getStatut());
    }

    @Test
    void administrerSoin_shouldUpdateStatus() {
        SoinInfirmier existing = new SoinInfirmier();
        existing.setId("soin-1");
        existing.setPatientId("patient-1");
        existing.setTypeSoin("PANSEMENT");
        existing.setStatut("EN_ATTENTE");

        SoinInfirmier updated = new SoinInfirmier();
        updated.setId("soin-1");
        updated.setPatientId("patient-1");
        updated.setTypeSoin("PANSEMENT");
        updated.setStatut("ADMINISTRE");
        updated.setInfirmierId("inf-1");

        when(soinInfirmierRepository.findById("soin-1")).thenReturn(Optional.of(existing));
        when(soinInfirmierRepository.save(any(SoinInfirmier.class))).thenReturn(updated);

        SoinInfirmier result = soinsService.administrerSoin("soin-1", "inf-1");

        assertNotNull(result);
        assertEquals("ADMINISTRE", result.getStatut());
        assertEquals("inf-1", result.getInfirmierId());
    }

    @Test
    void nonAdministre_shouldSetStatus() {
        SoinInfirmier existing = new SoinInfirmier();
        existing.setId("soin-1");
        existing.setPatientId("patient-1");
        existing.setStatut("EN_ATTENTE");

        SoinInfirmier updated = new SoinInfirmier();
        updated.setId("soin-1");
        updated.setPatientId("patient-1");
        updated.setStatut("NON_ADMINISTRE");
        updated.setMotifNonAdministration("Patient refuse");

        when(soinInfirmierRepository.findById("soin-1")).thenReturn(Optional.of(existing));
        when(soinInfirmierRepository.save(any(SoinInfirmier.class))).thenReturn(updated);

        SoinInfirmier result = soinsService.nonAdministre("soin-1", "Patient refuse");

        assertNotNull(result);
        assertEquals("NON_ADMINISTRE", result.getStatut());
        assertEquals("Patient refuse", result.getMotifNonAdministration());
    }

    @Test
    void getSoinsPatient_shouldReturnList() {
        when(soinInfirmierRepository.findByPatientIdAndStatut("patient-1", "EN_ATTENTE"))
                .thenReturn(Collections.emptyList());

        List<SoinInfirmier> soins = soinsService.getSoinsPatient("patient-1");

        assertNotNull(soins);
        assertTrue(soins.isEmpty());
    }
}
