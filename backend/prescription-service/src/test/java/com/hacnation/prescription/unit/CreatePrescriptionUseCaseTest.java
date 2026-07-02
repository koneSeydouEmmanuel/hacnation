package com.hacnation.prescription.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacnation.common.dto.PrescriptionDto;
import com.hacnation.common.enums.StatutPrescription;
import com.hacnation.common.enums.TypePrescription;
import com.hacnation.prescription.application.service.CreatePrescriptionUseCase;
import com.hacnation.prescription.domain.model.Prescription;
import com.hacnation.prescription.domain.port.PrescriptionEventPublisherPort;
import com.hacnation.prescription.domain.port.PrescriptionRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreatePrescriptionUseCaseTest {

    @Mock
    private PrescriptionRepositoryPort prescriptionRepository;

    @Mock
    private PrescriptionEventPublisherPort eventPublisher;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private CreatePrescriptionUseCase createPrescriptionUseCase;

    @Test
    void createPrescription_examen_shouldPublishEvent() throws Exception {
        Prescription savedPrescription = new Prescription();
        savedPrescription.setId("presc-1");
        savedPrescription.setConsultationId("consult-1");
        savedPrescription.setType(TypePrescription.EXAMEN);
        savedPrescription.setDetails(objectMapper.writeValueAsString(Map.of("type_examen", "NFS")));
        savedPrescription.setStatut(StatutPrescription.EN_ATTENTE);

        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(savedPrescription);

        PrescriptionDto result = createPrescriptionUseCase.createPrescription(
                "consult-1", TypePrescription.EXAMEN,
                Map.of("type_examen", "NFS"), "patient-1");

        assertNotNull(result);
        assertEquals(TypePrescription.EXAMEN, result.getType());
        assertEquals(StatutPrescription.EN_ATTENTE, result.getStatut());
        verify(eventPublisher).publishPrescriptionEvent(any());
    }

    @Test
    void createPrescription_medicament_shouldPublishEvent() throws Exception {
        Prescription savedPrescription = new Prescription();
        savedPrescription.setId("presc-2");
        savedPrescription.setConsultationId("consult-1");
        savedPrescription.setType(TypePrescription.MEDICAMENT);
        savedPrescription.setDetails(objectMapper.writeValueAsString(Map.of("medicament", "Paracetamol")));
        savedPrescription.setStatut(StatutPrescription.EN_ATTENTE);

        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(savedPrescription);

        PrescriptionDto result = createPrescriptionUseCase.createPrescription(
                "consult-1", TypePrescription.MEDICAMENT,
                Map.of("medicament", "Paracetamol"), "patient-1");

        assertNotNull(result);
        assertEquals(TypePrescription.MEDICAMENT, result.getType());
        verify(eventPublisher).publishPrescriptionEvent(any());
    }

    @Test
    void updatePrescriptionStatus_shouldUpdateStatus() {
        Prescription prescription = new Prescription();
        prescription.setId("presc-1");
        prescription.setConsultationId("consult-1");
        prescription.setType(TypePrescription.EXAMEN);
        prescription.setDetails("{}");
        prescription.setStatut(StatutPrescription.EN_ATTENTE);

        Prescription updated = new Prescription();
        updated.setId("presc-1");
        updated.setConsultationId("consult-1");
        updated.setType(TypePrescription.EXAMEN);
        updated.setStatut(StatutPrescription.TERMINEE);

        when(prescriptionRepository.findById("presc-1")).thenReturn(Optional.of(prescription));
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(updated);

        PrescriptionDto result = createPrescriptionUseCase.updatePrescriptionStatus("presc-1", StatutPrescription.TERMINEE);

        assertEquals(StatutPrescription.TERMINEE, result.getStatut());
    }
}
