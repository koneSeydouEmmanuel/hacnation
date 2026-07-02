package com.hacnation.bloc.integration;

import com.hacnation.bloc.domain.model.InterventionChirurgicale;
import com.hacnation.bloc.domain.port.inbound.BlocUseCase;
import com.hacnation.bloc.infrastructure.adapter.inbound.BlocController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlocController.class)
@AutoConfigureMockMvc(addFilters = false)
class BlocControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlocUseCase blocUseCase;

    @Test
    void programmerIntervention_shouldReturn200() throws Exception {
        InterventionChirurgicale intervention = new InterventionChirurgicale();
        intervention.setId("interv-1");
        intervention.setPatientId("patient-1");
        intervention.setTypeIntervention("Appendicectomie");
        intervention.setChirurgienId("chir-1");
        intervention.setSalle("BLOC-A");
        intervention.setDateHeure(LocalDateTime.of(2026, 7, 3, 8, 0));
        intervention.setDureeEstimee(60);
        intervention.setStatut("PROGRAMMEE");

        when(blocUseCase.programmerIntervention(anyString(), anyString(), anyString(), anyString(), any(), any()))
                .thenReturn(intervention);

        mockMvc.perform(post("/api/bloc/interventions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"patientId\":\"patient-1\",\"typeIntervention\":\"Appendicectomie\",\"chirurgienId\":\"chir-1\",\"salle\":\"BLOC-A\",\"dateHeure\":\"2026-07-03T08:00:00\",\"dureeEstimee\":60}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("interv-1"))
                .andExpect(jsonPath("$.typeIntervention").value("Appendicectomie"))
                .andExpect(jsonPath("$.statut").value("PROGRAMMEE"));
    }

    @Test
    void getPlanningSalle_shouldReturnList() throws Exception {
        InterventionChirurgicale intervention = new InterventionChirurgicale();
        intervention.setId("interv-1");
        intervention.setPatientId("patient-1");
        intervention.setTypeIntervention("Appendicectomie");
        intervention.setSalle("BLOC-A");
        intervention.setStatut("PROGRAMMEE");

        List<InterventionChirurgicale> interventions = Collections.singletonList(intervention);

        when(blocUseCase.getPlanningSalle(eq("BLOC-A"), any())).thenReturn(interventions);

        mockMvc.perform(get("/api/bloc/interventions")
                        .param("salle", "BLOC-A")
                        .param("date", "2026-07-03"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("interv-1"))
                .andExpect(jsonPath("$[0].salle").value("BLOC-A"));
    }

    @Test
    void updateStatutIntervention_shouldReturn200() throws Exception {
        InterventionChirurgicale intervention = new InterventionChirurgicale();
        intervention.setId("interv-1");
        intervention.setPatientId("patient-1");
        intervention.setTypeIntervention("Appendicectomie");
        intervention.setSalle("BLOC-A");
        intervention.setStatut("EN_COURS");

        when(blocUseCase.updateStatutIntervention("interv-1", "EN_COURS")).thenReturn(intervention);

        mockMvc.perform(put("/api/bloc/interventions/interv-1/statut")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"statut\":\"EN_COURS\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("interv-1"))
                .andExpect(jsonPath("$.statut").value("EN_COURS"));
    }
}
