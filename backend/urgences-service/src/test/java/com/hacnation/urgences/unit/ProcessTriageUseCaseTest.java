package com.hacnation.urgences.unit;

import com.hacnation.common.enums.NiveauTriage;
import com.hacnation.urgences.application.service.ProcessTriageUseCase;
import com.hacnation.urgences.domain.model.Triage;
import com.hacnation.urgences.domain.port.NotificationEventPublisherPort;
import com.hacnation.urgences.domain.port.TriageRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessTriageUseCaseTest {

    @Mock
    private TriageRepositoryPort triageRepository;

    @Mock
    private NotificationEventPublisherPort eventPublisher;

    @InjectMocks
    private ProcessTriageUseCase processTriageUseCase;

    @Test
    void trier_niveau1Critique_shouldPublishAlert() {
        Triage saved = new Triage();
        saved.setId("triage-1");
        saved.setAdmissionId("adm-1");
        saved.setPatientId("patient-1");
        saved.setNiveauGravite(NiveauTriage.NIVEAU_1_CRITIQUE);
        saved.setOrientation("Salle de degradation");
        saved.setMotif("Arret cardiaque");

        when(triageRepository.save(any(Triage.class))).thenReturn(saved);

        Triage result = processTriageUseCase.trier("adm-1", "patient-1",
                NiveauTriage.NIVEAU_1_CRITIQUE, "TA: 60/40", "Arret cardiaque", "Salle de degradation");

        assertNotNull(result);
        assertEquals(NiveauTriage.NIVEAU_1_CRITIQUE, result.getNiveauGravite());
        verify(eventPublisher).publishNotificationEvent(any());
    }

    @Test
    void trier_niveau2TresUrgent_shouldPublishAlert() {
        Triage saved = new Triage();
        saved.setId("triage-1");
        saved.setAdmissionId("adm-1");
        saved.setPatientId("patient-1");
        saved.setNiveauGravite(NiveauTriage.NIVEAU_2_TRES_URGENT);

        when(triageRepository.save(any(Triage.class))).thenReturn(saved);

        Triage result = processTriageUseCase.trier("adm-1", "patient-1",
                NiveauTriage.NIVEAU_2_TRES_URGENT, "TA: 90/60", "Douleur thoracique", "Box urgence");

        assertNotNull(result);
        assertEquals(NiveauTriage.NIVEAU_2_TRES_URGENT, result.getNiveauGravite());
        verify(eventPublisher).publishNotificationEvent(any());
    }

    @Test
    void trier_niveau4Standard_shouldNotPublishAlert() {
        Triage saved = new Triage();
        saved.setId("triage-2");
        saved.setAdmissionId("adm-2");
        saved.setPatientId("patient-2");
        saved.setNiveauGravite(NiveauTriage.NIVEAU_4_STANDARD);

        when(triageRepository.save(any(Triage.class))).thenReturn(saved);

        Triage result = processTriageUseCase.trier("adm-2", "patient-2",
                NiveauTriage.NIVEAU_4_STANDARD, "TA: 120/80", "Fievre legere", "Salle attente");

        assertNotNull(result);
        verify(eventPublisher, never()).publishNotificationEvent(any());
    }
}
