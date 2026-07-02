package com.hacnation.rdv.unit;

import com.hacnation.common.dto.RdvDto;
import com.hacnation.common.enums.StatutRdv;
import com.hacnation.common.exception.BusinessException;
import com.hacnation.rdv.application.service.CreateRdvUseCase;
import com.hacnation.rdv.application.service.QrCodeService;
import com.hacnation.rdv.domain.model.RendezVous;
import com.hacnation.rdv.domain.port.RdvEventPublisherPort;
import com.hacnation.rdv.domain.port.RdvRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRdvUseCaseTest {

    @Mock
    private RdvRepositoryPort rdvRepository;

    @Mock
    private QrCodeService qrCodeService;

    @Mock
    private RdvEventPublisherPort eventPublisher;

    @InjectMocks
    private CreateRdvUseCase createRdvUseCase;

    @BeforeEach
    void setUp() {
        when(qrCodeService.generateQrCode(anyString(), anyString())).thenReturn("base64qrcode");
    }

    @Test
    void createRdv_shouldCreateAndPublishEvent() {
        when(rdvRepository.findByPraticienIdAndDateHeureBetween(anyString(), any(), any()))
                .thenReturn(Collections.emptyList());

        RendezVous savedRdv = new RendezVous();
        savedRdv.setId("rdv-1");
        savedRdv.setPatientId("patient-1");
        savedRdv.setPraticienId("praticien-1");
        savedRdv.setService("CONSULTATION");
        savedRdv.setDateHeure(LocalDateTime.of(2026, 7, 3, 10, 0));
        savedRdv.setStatut(StatutRdv.EN_ATTENTE);
        savedRdv.setQrCode("base64qrcode");

        when(rdvRepository.save(any(RendezVous.class))).thenReturn(savedRdv);
        when(qrCodeService.generateQrCode("rdv-1", "patient-1")).thenReturn("base64qrcode");

        RdvDto result = createRdvUseCase.createRdv("patient-1", "praticien-1", "CONSULTATION",
                LocalDateTime.of(2026, 7, 3, 10, 0), "Consultation generale");

        assertNotNull(result);
        assertEquals("patient-1", result.getPatientId());
        assertEquals(StatutRdv.EN_ATTENTE, result.getStatut());
        verify(eventPublisher).publishRdvCreated(eq("rdv-1"), eq("patient-1"), eq("CONSULTATION"), any());
    }

    @Test
    void createRdv_shouldThrow409_whenSlotConflict() {
        RendezVous existingRdv = new RendezVous();
        existingRdv.setStatut(StatutRdv.EN_ATTENTE);

        when(rdvRepository.findByPraticienIdAndDateHeureBetween(anyString(), any(), any()))
                .thenReturn(java.util.List.of(existingRdv));

        BusinessException exception = assertThrows(BusinessException.class, () ->
                createRdvUseCase.createRdv("patient-1", "praticien-1", "CONSULTATION",
                        LocalDateTime.now(), "Consultation"));

        assertEquals(409, exception.getStatus());
        verify(rdvRepository, never()).save(any(RendezVous.class));
    }

    @Test
    void getRdv_shouldReturnRdv() {
        RendezVous rdv = new RendezVous();
        rdv.setId("rdv-1");
        rdv.setPatientId("patient-1");
        rdv.setService("CONSULTATION");
        rdv.setDateHeure(LocalDateTime.now());
        rdv.setStatut(StatutRdv.EN_ATTENTE);

        when(rdvRepository.findById("rdv-1")).thenReturn(Optional.of(rdv));

        RdvDto result = createRdvUseCase.getRdv("rdv-1");

        assertNotNull(result);
        assertEquals("rdv-1", result.getId());
    }

    @Test
    void cancelRdv_shouldSetStatutAnnule() {
        RendezVous rdv = new RendezVous();
        rdv.setId("rdv-1");
        rdv.setPatientId("patient-1");
        rdv.setStatut(StatutRdv.EN_ATTENTE);

        when(rdvRepository.findById("rdv-1")).thenReturn(Optional.of(rdv));
        RendezVous savedRdv = new RendezVous();
        savedRdv.setId("rdv-1");
        savedRdv.setPatientId("patient-1");
        savedRdv.setStatut(StatutRdv.ANNULE);
        when(rdvRepository.save(any(RendezVous.class))).thenReturn(savedRdv);

        RdvDto result = createRdvUseCase.cancelRdv("rdv-1");

        assertEquals(StatutRdv.ANNULE, result.getStatut());
        verify(eventPublisher).publishRdvStatusChanged(eq("rdv-1"), eq("patient-1"), eq("ANNULE"));
    }
}
