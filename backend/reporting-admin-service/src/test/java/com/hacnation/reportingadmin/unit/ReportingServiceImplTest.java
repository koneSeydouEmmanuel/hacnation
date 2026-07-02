package com.hacnation.reportingadmin.unit;

import com.hacnation.reportingadmin.application.service.ReportingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReportingServiceImplTest {

    @InjectMocks
    private ReportingServiceImpl reportingService;

    @Test
    void getDashboardFiles_shouldReturnWaitingQueueData() {
        Map<String, Object> result = reportingService.getDashboardFiles();

        assertNotNull(result);
        assertTrue(result.containsKey("patientsEnAttente"));
        assertTrue(result.containsKey("tempsAttenteMoyen"));
        assertTrue(result.containsKey("tempsAttenteMax"));
        assertEquals(28, result.get("tempsAttenteMoyen"));
        assertEquals(95, result.get("tempsAttenteMax"));

        @SuppressWarnings("unchecked")
        Map<String, Integer> patientsParService = (Map<String, Integer>) result.get("patientsEnAttente");
        assertNotNull(patientsParService);
        assertTrue(patientsParService.containsKey("Medecine Generale"));
        assertTrue(patientsParService.containsKey("Pediatrie"));
    }

    @Test
    void getDashboardConsultations_shouldReturnConsultationData() {
        LocalDate dateDebut = LocalDate.of(2026, 6, 1);
        LocalDate dateFin = LocalDate.of(2026, 6, 30);

        Map<String, Object> result = reportingService.getDashboardConsultations(dateDebut, dateFin);

        assertNotNull(result);
        assertEquals(487, result.get("nombreConsultations"));
        assertTrue(result.containsKey("parMedecin"));
        assertTrue(result.containsKey("parService"));

        @SuppressWarnings("unchecked")
        Map<String, Integer> parMedecin = (Map<String, Integer>) result.get("parMedecin");
        assertTrue(parMedecin.containsKey("Dr. Kone"));
        assertEquals(85, parMedecin.get("Dr. Kone"));

        @SuppressWarnings("unchecked")
        Map<String, Integer> parService = (Map<String, Integer>) result.get("parService");
        assertTrue(parService.containsKey("Medecine Generale"));
        assertEquals(156, parService.get("Medecine Generale"));
    }

    @Test
    void getDashboardRecettes_shouldReturnRevenueData() {
        LocalDate dateDebut = LocalDate.of(2026, 6, 1);
        LocalDate dateFin = LocalDate.of(2026, 6, 30);

        Map<String, Object> result = reportingService.getDashboardRecettes(dateDebut, dateFin);

        assertNotNull(result);
        assertEquals(18500000, result.get("recettesTotales"));
        assertTrue(result.containsKey("parModePaiement"));
        assertEquals(142, result.get("facturesEnAttente"));
        assertEquals(78.5, result.get("tauxRecouvrement"));

        @SuppressWarnings("unchecked")
        Map<String, Object> parModePaiement = (Map<String, Object>) result.get("parModePaiement");
        assertTrue(parModePaiement.containsKey("ESPECES"));
        assertTrue(parModePaiement.containsKey("ORANGE_MONEY"));
        assertTrue(parModePaiement.containsKey("WAVE"));
    }

    @Test
    void getDashboardStocks_shouldReturnStockData() {
        Map<String, Object> result = reportingService.getDashboardStocks();

        assertNotNull(result);
        assertEquals(13, result.get("rupturesStock"));
        assertEquals(47, result.get("prochesPeremption"));
        assertEquals(125000000, result.get("valeurStock"));
    }

    @Test
    void getDashboardUrgences_shouldReturnEmergencyData() {
        LocalDate date = LocalDate.of(2026, 7, 2);

        Map<String, Object> result = reportingService.getDashboardUrgences(date);

        assertNotNull(result);
        assertEquals(34, result.get("admissionsUrgences"));
        assertEquals(18, result.get("tempsPriseEnChargeMoyen"));

        @SuppressWarnings("unchecked")
        Map<String, Integer> parNiveauTriage = (Map<String, Integer>) result.get("parNiveauTriage");
        assertNotNull(parNiveauTriage);
        assertTrue(parNiveauTriage.containsKey("NIVEAU_1_CRITIQUE"));
        assertEquals(5, parNiveauTriage.get("NIVEAU_1_CRITIQUE"));
        assertTrue(parNiveauTriage.containsKey("NIVEAU_5_NON_URGENT"));
        assertEquals(2, parNiveauTriage.get("NIVEAU_5_NON_URGENT"));
    }
}
