package com.hacnation.accueil.unit;

import com.hacnation.common.enums.TypeAdmission;
import com.hacnation.accueil.application.service.RegisterAdmissionUseCase;
import com.hacnation.accueil.domain.model.Admission;
import com.hacnation.accueil.domain.port.AdmissionEventPublisherPort;
import com.hacnation.accueil.domain.port.AdmissionRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterAdmissionUseCaseTest {

    @Mock
    private AdmissionRepositoryPort admissionRepository;

    @Mock
    private AdmissionEventPublisherPort eventPublisher;

    @InjectMocks
    private RegisterAdmissionUseCase registerAdmissionUseCase;

    @Test
    void createAdmission_consultation_shouldRegister() {
        Admission saved = new Admission();
        saved.setId("adm-1");
        saved.setPatientId("patient-1");
        saved.setType(TypeAdmission.CONSULTATION);
        saved.setService("Medecine Generale");
        saved.setMotif("Consultation de routine");
        saved.setStatut("ENREGISTREE");

        when(admissionRepository.save(any(Admission.class))).thenReturn(saved);

        Admission result = registerAdmissionUseCase.createAdmission(
                "patient-1", TypeAdmission.CONSULTATION, "Medecine Generale", "Consultation de routine");

        assertNotNull(result);
        assertEquals(TypeAdmission.CONSULTATION, result.getType());
        assertEquals("ENREGISTREE", result.getStatut());
        verify(eventPublisher).publishAdmissionEvent(any());
    }

    @Test
    void createAdmission_urgence_shouldRegister() {
        Admission saved = new Admission();
        saved.setId("adm-2");
        saved.setPatientId("patient-2");
        saved.setType(TypeAdmission.URGENCE);
        saved.setService("Urgences");
        saved.setStatut("ENREGISTREE");

        when(admissionRepository.save(any(Admission.class))).thenReturn(saved);

        Admission result = registerAdmissionUseCase.createAdmission(
                "patient-2", TypeAdmission.URGENCE, "Urgences", "Accident");

        assertNotNull(result);
        assertEquals(TypeAdmission.URGENCE, result.getType());
    }

    @Test
    void createAdmission_hospitalisation_shouldRegister() {
        Admission saved = new Admission();
        saved.setId("adm-3");
        saved.setPatientId("patient-3");
        saved.setType(TypeAdmission.HOSPITALISATION);
        saved.setService("Chirurgie");
        saved.setStatut("ENREGISTREE");

        when(admissionRepository.save(any(Admission.class))).thenReturn(saved);

        Admission result = registerAdmissionUseCase.createAdmission(
                "patient-3", TypeAdmission.HOSPITALISATION, "Chirurgie", "Operation planifiee");

        assertNotNull(result);
        assertEquals(TypeAdmission.HOSPITALISATION, result.getType());
    }
}
