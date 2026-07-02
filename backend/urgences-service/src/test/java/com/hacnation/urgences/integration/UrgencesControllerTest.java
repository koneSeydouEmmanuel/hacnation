package com.hacnation.urgences.integration;

import com.hacnation.common.enums.NiveauTriage;
import com.hacnation.urgences.application.service.ProcessTriageUseCase;
import com.hacnation.urgences.domain.model.Triage;
import com.hacnation.urgences.infrastructure.adapter.in.rest.UrgencesController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UrgencesController.class)
@AutoConfigureMockMvc(addFilters = false)
class UrgencesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProcessTriageUseCase processTriageUseCase;

    @Test
    void trier_shouldReturn200() throws Exception {
        Triage triage = new Triage();
        triage.setId("triage-1");
        triage.setAdmissionId("adm-1");
        triage.setPatientId("patient-1");
        triage.setNiveauGravite(NiveauTriage.NIVEAU_3_URGENT);
        triage.setConstantes("TA=120/80, FC=80, Temp=37.5");
        triage.setMotif("Douleur abdominale");
        triage.setOrientation("Urgences");
        triage.setDateTriage(LocalDateTime.now());

        when(processTriageUseCase.trier(anyString(), anyString(), any(), anyString(), anyString(), anyString()))
                .thenReturn(triage);

        mockMvc.perform(post("/api/urgences/triage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"admissionId\":\"adm-1\",\"patientId\":\"patient-1\",\"niveauGravite\":\"NIVEAU_3_URGENT\",\"constantes\":\"TA=120/80\",\"motif\":\"Douleur abdominale\",\"orientation\":\"Urgences\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("triage-1"))
                .andExpect(jsonPath("$.patientId").value("patient-1"))
                .andExpect(jsonPath("$.niveauGravite").value("NIVEAU_3_URGENT"));
    }

    @Test
    void trier_critique_shouldReturn200() throws Exception {
        Triage triage = new Triage();
        triage.setId("triage-2");
        triage.setAdmissionId("adm-2");
        triage.setPatientId("patient-2");
        triage.setNiveauGravite(NiveauTriage.NIVEAU_1_CRITIQUE);
        triage.setMotif("Arret cardiaque");
        triage.setOrientation("Reanimation");
        triage.setDateTriage(LocalDateTime.now());

        when(processTriageUseCase.trier(anyString(), anyString(), any(), anyString(), anyString(), anyString()))
                .thenReturn(triage);

        mockMvc.perform(post("/api/urgences/triage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"admissionId\":\"adm-2\",\"patientId\":\"patient-2\",\"niveauGravite\":\"NIVEAU_1_CRITIQUE\",\"constantes\":\"\",\"motif\":\"Arret cardiaque\",\"orientation\":\"Reanimation\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.niveauGravite").value("NIVEAU_1_CRITIQUE"));
    }

    @Test
    void getFileAttente_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/urgences/file"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("File d'attente des urgences"));
    }
}
