package com.hacnation.consultation.unit;

import com.hacnation.common.dto.ConsultationDto;
import com.hacnation.common.enums.StatutConsultation;
import com.hacnation.common.events.ConsultationEvent;
import com.hacnation.common.exception.ResourceNotFoundException;
import com.hacnation.consultation.application.service.CreateConsultationUseCase;
import com.hacnation.consultation.application.service.TerminateConsultationUseCase;
import com.hacnation.consultation.domain.model.Consultation;
import com.hacnation.consultation.domain.port.ConsultationEventPublisherPort;
import com.hacnation.consultation.domain.port.ConsultationRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TerminateConsultationUseCaseTest {

    @Mock
    private ConsultationRepositoryPort consultationRepository;

    @Mock
    private ConsultationEventPublisherPort eventPublisher;

    @Mock
    private CreateConsultationUseCase createConsultationUseCase;

    @InjectMocks
    private TerminateConsultationUseCase terminateConsultationUseCase;

    @Test
    @DisplayName("Should set TERMINEE status and publish event when consultation found")
    void terminerConsultation_shouldSetTermineeAndPublishEvent() {
        Consultation consultation = new Consultation();
        consultation.setId("consult-1");
        consultation.setPatientId("patient-1");
        consultation.setRdvId("rdv-1");
        consultation.setMedecinId("medecin-1");
        consultation.setStatut(StatutConsultation.EN_COURS);
        consultation.setDate(LocalDateTime.now());

        when(consultationRepository.findById("consult-1")).thenReturn(Optional.of(consultation));

        Consultation savedConsultation = new Consultation();
        savedConsultation.setId("consult-1");
        savedConsultation.setPatientId("patient-1");
        savedConsultation.setMedecinId("medecin-1");
        savedConsultation.setStatut(StatutConsultation.TERMINEE);

        when(consultationRepository.save(any(Consultation.class))).thenReturn(savedConsultation);

        ConsultationDto dto = new ConsultationDto();
        dto.setId("consult-1");
        dto.setPatientId("patient-1");
        dto.setStatut(StatutConsultation.TERMINEE);

        when(createConsultationUseCase.toDto(any(Consultation.class))).thenReturn(dto);

        ConsultationDto result = terminateConsultationUseCase.terminerConsultation("consult-1");

        assertNotNull(result);
        assertEquals(StatutConsultation.TERMINEE, result.getStatut());
        assertEquals("consult-1", result.getId());
        verify(eventPublisher).publishConsultationEvent(any(ConsultationEvent.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when consultation not found")
    void terminerConsultation_shouldThrowException_whenNotFound() {
        when(consultationRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                terminateConsultationUseCase.terminerConsultation("nonexistent"));

        verify(consultationRepository, never()).save(any(Consultation.class));
        verify(eventPublisher, never()).publishConsultationEvent(any());
    }

    @Test
    @DisplayName("Should set TERMINEE status on the consultation entity before saving")
    void terminerConsultation_shouldSetStatutOnEntity() {
        Consultation consultation = new Consultation();
        consultation.setId("consult-1");
        consultation.setPatientId("patient-1");
        consultation.setStatut(StatutConsultation.EN_COURS);

        when(consultationRepository.findById("consult-1")).thenReturn(Optional.of(consultation));

        Consultation saved = new Consultation();
        saved.setId("consult-1");
        saved.setPatientId("patient-1");
        saved.setStatut(StatutConsultation.TERMINEE);

        when(consultationRepository.save(any(Consultation.class))).thenReturn(saved);

        ConsultationDto dto = new ConsultationDto();
        dto.setId("consult-1");
        dto.setStatut(StatutConsultation.TERMINEE);

        when(createConsultationUseCase.toDto(any(Consultation.class))).thenReturn(dto);

        terminateConsultationUseCase.terminerConsultation("consult-1");

        assertEquals(StatutConsultation.TERMINEE, consultation.getStatut());
    }
}
