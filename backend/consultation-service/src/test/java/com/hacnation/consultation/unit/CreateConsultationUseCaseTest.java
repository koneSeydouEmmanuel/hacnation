package com.hacnation.consultation.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacnation.common.dto.ConsultationDto;
import com.hacnation.common.enums.StatutConsultation;
import com.hacnation.consultation.application.service.CreateConsultationUseCase;
import com.hacnation.consultation.domain.model.Consultation;
import com.hacnation.consultation.domain.port.ConsultationEventPublisherPort;
import com.hacnation.consultation.domain.port.ConsultationRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateConsultationUseCaseTest {

    @Mock
    private ConsultationRepositoryPort consultationRepository;

    @Mock
    private ConsultationEventPublisherPort eventPublisher;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private CreateConsultationUseCase createConsultationUseCase;

    @Test
    void createConsultation_shouldCreateAndPublishEvent() throws Exception {
        Consultation savedConsultation = new Consultation();
        savedConsultation.setId("consult-1");
        savedConsultation.setPatientId("patient-1");
        savedConsultation.setRdvId("rdv-1");
        savedConsultation.setMedecinId("medecin-1");
        savedConsultation.setConstantes(objectMapper.writeValueAsString(Map.of("tension", "120/80")));
        savedConsultation.setDiagnostic("Consultation de routine");
        savedConsultation.setStatut(StatutConsultation.EN_COURS);
        savedConsultation.setDate(LocalDateTime.now());

        when(consultationRepository.save(any(Consultation.class))).thenReturn(savedConsultation);

        ConsultationDto result = createConsultationUseCase.createConsultation(
                "patient-1", "rdv-1", "medecin-1",
                Map.of("tension", "120/80"), "Consultation de routine");

        assertNotNull(result);
        assertEquals("patient-1", result.getPatientId());
        assertEquals(StatutConsultation.EN_COURS, result.getStatut());
        verify(eventPublisher).publishConsultationEvent(any());
    }

    @Test
    void getConsultation_shouldReturnDto() {
        Consultation consultation = new Consultation();
        consultation.setId("consult-1");
        consultation.setPatientId("patient-1");
        consultation.setRdvId("rdv-1");
        consultation.setMedecinId("medecin-1");
        consultation.setConstantes("{\"tension\":\"120/80\"}");
        consultation.setDiagnostic("Routine");
        consultation.setStatut(StatutConsultation.EN_COURS);
        consultation.setDate(LocalDateTime.now());

        when(consultationRepository.findById("consult-1")).thenReturn(java.util.Optional.of(consultation));

        ConsultationDto result = createConsultationUseCase.getConsultation("consult-1");

        assertNotNull(result);
        assertEquals("consult-1", result.getId());
        assertEquals("Routine", result.getDiagnostic());
    }
}
