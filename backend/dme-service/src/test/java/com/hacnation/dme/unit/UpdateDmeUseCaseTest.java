package com.hacnation.dme.unit;

import com.hacnation.dme.application.service.GetDmeUseCase;
import com.hacnation.dme.application.service.UpdateDmeUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateDmeUseCaseTest {

    @Mock
    private GetDmeUseCase getDmeUseCase;

    @InjectMocks
    private UpdateDmeUseCase updateDmeUseCase;

    @Test
    @DisplayName("Should add consultation note to DME")
    void addNoteCliniqueFromConsultation_shouldDelegateToGetDmeUseCase() {
        updateDmeUseCase.addNoteCliniqueFromConsultation("patient-1", "consult-1", "medecin-1", "TERMINEE");

        verify(getDmeUseCase).addNoteClinique(eq("patient-1"), argThat(note ->
                "CONSULTATION".equals(note.get("type")) &&
                "consult-1".equals(note.get("consultationId")) &&
                "medecin-1".equals(note.get("medecinId")) &&
                "TERMINEE".equals(note.get("statut"))
        ));
    }

    @Test
    @DisplayName("Should add lab result document to DME")
    void addDocumentFromLab_shouldDelegateToGetDmeUseCase() {
        updateDmeUseCase.addDocumentFromLab("patient-1", "analyse-1", "presc-1", "VALIDE");

        verify(getDmeUseCase).addDocument(eq("patient-1"), argThat(doc ->
                "LAB_RESULT".equals(doc.get("type")) &&
                "analyse-1".equals(doc.get("analyseId")) &&
                "presc-1".equals(doc.get("prescriptionId")) &&
                "VALIDE".equals(doc.get("statut"))
        ));
    }

    @Test
    @DisplayName("Should add pharmacy dispensation note to DME")
    void addNoteFromPharmacy_shouldDelegateToGetDmeUseCase() {
        updateDmeUseCase.addNoteFromPharmacy("patient-1", "ord-1", "presc-1",
                "med-1", 10, "DELIVRE");

        verify(getDmeUseCase).addNoteClinique(eq("patient-1"), argThat(note ->
                "DRUG_DISPENSED".equals(note.get("type")) &&
                "ord-1".equals(note.get("ordonnanceId")) &&
                "presc-1".equals(note.get("prescriptionId")) &&
                "med-1".equals(note.get("medicamentId")) &&
                Integer.valueOf(10).equals(note.get("quantite")) &&
                "DELIVRE".equals(note.get("statut"))
        ));
    }

    @Test
    @DisplayName("Should create DME for a new patient")
    void createDmeForPatient_shouldDelegateToGetDmeUseCase() {
        updateDmeUseCase.createDmeForPatient("patient-1");

        verify(getDmeUseCase).createDme("patient-1");
    }
}
