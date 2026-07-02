package com.hacnation.laboratoire.unit;

import com.hacnation.common.enums.StatutPrescription;
import com.hacnation.common.events.LabEvent;
import com.hacnation.common.events.NotificationEvent;
import com.hacnation.common.exception.ResourceNotFoundException;
import com.hacnation.laboratoire.application.service.ValidateResultUseCase;
import com.hacnation.laboratoire.domain.model.DemandeAnalyse;
import com.hacnation.laboratoire.domain.port.DemandeAnalyseRepositoryPort;
import com.hacnation.laboratoire.domain.port.LaboEventPublisherPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidateResultUseCaseTest {

    @Mock
    private DemandeAnalyseRepositoryPort demandeAnalyseRepository;

    @Mock
    private LaboEventPublisherPort eventPublisher;

    @InjectMocks
    private ValidateResultUseCase validateResultUseCase;

    @Test
    @DisplayName("Should validate results, set TERMINEE status, and publish events")
    void validerResultats_shouldSetTermineeAndPublishEvents() {
        DemandeAnalyse analyse = new DemandeAnalyse();
        analyse.setId("analyse-1");
        analyse.setPrescriptionId("presc-1");
        analyse.setPatientId("patient-1");
        analyse.setTypeAnalyse("NFS");
        analyse.setStatut(StatutPrescription.EN_COURS);

        when(demandeAnalyseRepository.findById("analyse-1")).thenReturn(Optional.of(analyse));

        DemandeAnalyse saved = new DemandeAnalyse();
        saved.setId("analyse-1");
        saved.setPrescriptionId("presc-1");
        saved.setPatientId("patient-1");
        saved.setTypeAnalyse("NFS");
        saved.setStatut(StatutPrescription.TERMINEE);
        saved.setValidateurId("validateur-1");

        when(demandeAnalyseRepository.save(any(DemandeAnalyse.class))).thenReturn(saved);

        DemandeAnalyse result = validateResultUseCase.validerResultats("analyse-1", "validateur-1");

        assertNotNull(result);
        assertEquals(StatutPrescription.TERMINEE, result.getStatut());
        assertEquals("validateur-1", result.getValidateurId());
        assertNotNull(result.getDateValidation());
        verify(eventPublisher).publishLabEvent(any(LabEvent.class));
        verify(eventPublisher).publishNotificationEvent(any(NotificationEvent.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when analysis not found")
    void validerResultats_shouldThrowException_whenNotFound() {
        when(demandeAnalyseRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                validateResultUseCase.validerResultats("nonexistent", "validateur-1"));

        verify(demandeAnalyseRepository, never()).save(any(DemandeAnalyse.class));
        verify(eventPublisher, never()).publishLabEvent(any());
        verify(eventPublisher, never()).publishNotificationEvent(any());
    }

    @Test
    @DisplayName("Should publish both LabEvent and NotificationEvent on validation")
    void validerResultats_shouldPublishBothEvents() {
        DemandeAnalyse analyse = new DemandeAnalyse();
        analyse.setId("analyse-1");
        analyse.setPrescriptionId("presc-1");
        analyse.setPatientId("patient-1");
        analyse.setTypeAnalyse("Glycemie");
        analyse.setStatut(StatutPrescription.EN_COURS);

        when(demandeAnalyseRepository.findById("analyse-1")).thenReturn(Optional.of(analyse));

        DemandeAnalyse saved = new DemandeAnalyse();
        saved.setId("analyse-1");
        saved.setPrescriptionId("presc-1");
        saved.setPatientId("patient-1");
        saved.setTypeAnalyse("Glycemie");
        saved.setStatut(StatutPrescription.TERMINEE);

        when(demandeAnalyseRepository.save(any(DemandeAnalyse.class))).thenReturn(saved);

        validateResultUseCase.validerResultats("analyse-1", "validateur-1");

        verify(eventPublisher).publishLabEvent(argThat(event ->
                "LAB_RESULT_AVAILABLE".equals(event.getEventType()) &&
                "analyse-1".equals(event.getAnalyseId())
        ));
        verify(eventPublisher).publishNotificationEvent(argThat(notification ->
                "LAB_RESULT_AVAILABLE".equals(notification.getEventType()) &&
                "patient-1".equals(notification.getPatientId()) &&
                "EMAIL".equals(notification.getCanal())
        ));
    }
}
