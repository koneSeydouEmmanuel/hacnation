package com.hacnation.laboratoire.integration;

import com.hacnation.common.enums.StatutPrescription;
import com.hacnation.laboratoire.application.service.ProcessAnalysisUseCase;
import com.hacnation.laboratoire.application.service.ValidateResultUseCase;
import com.hacnation.laboratoire.domain.model.DemandeAnalyse;
import com.hacnation.laboratoire.infrastructure.adapter.in.rest.LaboController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LaboController.class)
class LaboControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProcessAnalysisUseCase processAnalysisUseCase;

    @MockBean
    private ValidateResultUseCase validateResultUseCase;

    @Test
    void getDemandes_shouldReturn200() throws Exception {
        when(processAnalysisUseCase.getDemandesEnAttente(any()))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/labo/demandes"))
                .andExpect(status().isOk());
    }

    @Test
    void getDemandeById_shouldReturn200() throws Exception {
        DemandeAnalyse demande = new DemandeAnalyse();
        demande.setId("analyse-1");
        demande.setStatut(StatutPrescription.EN_ATTENTE);

        when(processAnalysisUseCase.getDemandeById("analyse-1")).thenReturn(demande);

        mockMvc.perform(get("/api/labo/demandes/analyse-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("analyse-1"));
    }

    @Test
    void saisirResultats_shouldReturn200() throws Exception {
        DemandeAnalyse demande = new DemandeAnalyse();
        demande.setId("analyse-1");
        demande.setStatut(StatutPrescription.EN_COURS);

        when(processAnalysisUseCase.saisirResultats(anyString(), anyString(), anyString()))
                .thenReturn(demande);

        mockMvc.perform(post("/api/labo/demandes/analyse-1/resultats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"resultats\":\"{\\\"resultat\\\":\\\"Normal\\\"}\",\"laborantinId\":\"labo-1\"}"))
                .andExpect(status().isOk());
    }
}
