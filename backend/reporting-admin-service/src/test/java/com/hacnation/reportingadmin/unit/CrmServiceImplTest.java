package com.hacnation.reportingadmin.unit;

import com.hacnation.reportingadmin.application.service.CrmServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CrmServiceImplTest {

    @InjectMocks
    private CrmServiceImpl crmService;

    @Test
    @DisplayName("Should return interaction history for a patient")
    void getInteractionHistorique_shouldReturnInteractionsList() {
        List<Map<String, Object>> result = crmService.getInteractionHistorique("patient-1");

        assertNotNull(result);
        assertEquals(10, result.size());

        Map<String, Object> firstInteraction = result.get(0);
        assertEquals("RDV_CREE", firstInteraction.get("type"));
        assertEquals("Rendez-vous cree", firstInteraction.get("description"));
        assertEquals("Cardiologie", firstInteraction.get("lieu"));
        assertEquals("CONFIRME", firstInteraction.get("statut"));
        assertNotNull(firstInteraction.get("dateHeure"));

        Map<String, Object> consultation = result.get(3);
        assertEquals("CONSULTATION", consultation.get("type"));
        assertEquals("Consultation avec Dr. Diallo", consultation.get("description"));
        assertEquals("TERMINE", consultation.get("statut"));

        Map<String, Object> facture = result.get(6);
        assertEquals("FACTURE_CREE", facture.get("type"));
        assertEquals("PAYEE", facture.get("statut"));
    }

    @Test
    @DisplayName("Should return fidelite info with patient loyalty data")
    void getFidelitePatient_shouldReturnLoyaltyData() {
        Map<String, Object> result = crmService.getFidelitePatient("patient-1");

        assertNotNull(result);
        assertEquals("patient-1", result.get("patientId"));
        assertEquals(12, result.get("nombreVisites"));
        assertEquals(18, result.get("ancienneteMois"));
        assertEquals("2025-06-20T09:15:00", result.get("derniereVisite"));
        assertEquals(850, result.get("pointsFidelite"));
        assertEquals("OR", result.get("niveauFidelite"));

        @SuppressWarnings("unchecked")
        Map<String, Object> avantages = (Map<String, Object>) result.get("avantages");
        assertNotNull(avantages);
        assertEquals("10%", avantages.get("reductionConsultation"));
        assertEquals(true, avantages.get("prioriteFileAttente"));
        assertEquals(true, avantages.get("accesRapportsSMS"));
        assertEquals("PLATINE (a 1000 points)", avantages.get("prochainNiveau"));
    }

    @Test
    @DisplayName("Should return different interaction lists for different patients")
    void getInteractionHistorique_shouldReturnConsistentResults() {
        List<Map<String, Object>> result1 = crmService.getInteractionHistorique("patient-A");
        List<Map<String, Object>> result2 = crmService.getInteractionHistorique("patient-B");

        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(result1.size(), result2.size());
    }

    @Test
    @DisplayName("Should return fidelite data with avantages structure for different patients")
    void getFidelitePatient_shouldReturnConsistentStructure() {
        Map<String, Object> result = crmService.getFidelitePatient("patient-2");

        assertNotNull(result);
        assertEquals("patient-2", result.get("patientId"));
        assertNotNull(result.get("avantages"));
        assertTrue(result.containsKey("nombreVisites"));
        assertTrue(result.containsKey("pointsFidelite"));
        assertTrue(result.containsKey("niveauFidelite"));
    }
}
