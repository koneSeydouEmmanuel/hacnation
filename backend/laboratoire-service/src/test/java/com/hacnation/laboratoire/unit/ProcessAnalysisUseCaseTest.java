package com.hacnation.laboratoire.unit;

import com.hacnation.common.enums.StatutPrescription;
import com.hacnation.laboratoire.application.service.ProcessAnalysisUseCase;
import com.hacnation.laboratoire.domain.model.DemandeAnalyse;
import com.hacnation.laboratoire.domain.port.DemandeAnalyseRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessAnalysisUseCaseTest {

    @Mock
    private DemandeAnalyseRepositoryPort demandeAnalyseRepository;

    @InjectMocks
    private ProcessAnalysisUseCase processAnalysisUseCase;

    @Test
    void getDemandesEnAttente_shouldReturnPage() {
        Page<DemandeAnalyse> page = new PageImpl<>(Collections.emptyList());
        when(demandeAnalyseRepository.findByStatutOrderByCreatedAtAsc(any(), any()))
                .thenReturn(page);

        Page<DemandeAnalyse> result = processAnalysisUseCase.getDemandesEnAttente(PageRequest.of(0, 10));

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void saisirResultats_shouldUpdateAnalyse() {
        DemandeAnalyse analyse = new DemandeAnalyse();
        analyse.setId("analyse-1");
        analyse.setStatut(StatutPrescription.EN_ATTENTE);

        DemandeAnalyse saved = new DemandeAnalyse();
        saved.setId("analyse-1");
        saved.setStatut(StatutPrescription.EN_COURS);
        saved.setResultats("{\"resultat\":\"Normal\"}");

        when(demandeAnalyseRepository.findById("analyse-1")).thenReturn(Optional.of(analyse));
        when(demandeAnalyseRepository.save(any(DemandeAnalyse.class))).thenReturn(saved);

        DemandeAnalyse result = processAnalysisUseCase.saisirResultats("analyse-1",
                "{\"resultat\":\"Normal\"}", "labo-1");

        assertNotNull(result);
        assertEquals(StatutPrescription.EN_COURS, result.getStatut());
        assertEquals("{\"resultat\":\"Normal\"}", result.getResultats());
    }
}
