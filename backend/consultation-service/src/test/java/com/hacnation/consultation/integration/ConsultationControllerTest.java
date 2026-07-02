package com.hacnation.consultation.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacnation.common.dto.ConsultationDto;
import com.hacnation.common.enums.StatutConsultation;
import com.hacnation.consultation.application.service.CreateConsultationUseCase;
import com.hacnation.consultation.application.service.TerminateConsultationUseCase;
import com.hacnation.consultation.infrastructure.adapter.in.rest.ConsultationController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConsultationController.class)
class ConsultationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateConsultationUseCase createConsultationUseCase;

    @MockBean
    private TerminateConsultationUseCase terminateConsultationUseCase;

    @Test
    void createConsultation_shouldReturn201() throws Exception {
        ConsultationDto dto = new ConsultationDto();
        dto.setId("consult-1");
        dto.setPatientId("patient-1");
        dto.setMedecinId("medecin-1");
        dto.setStatut(StatutConsultation.EN_COURS);

        when(createConsultationUseCase.createConsultation(anyString(), anyString(), anyString(), anyMap(), anyString()))
                .thenReturn(dto);

        mockMvc.perform(post("/api/consultations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"patientId\":\"patient-1\",\"rdvId\":\"rdv-1\",\"medecinId\":\"medecin-1\",\"constantes\":{\"tension\":\"120/80\"},\"motif\":\"Consultation\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void getConsultation_shouldReturn200() throws Exception {
        ConsultationDto dto = new ConsultationDto();
        dto.setId("consult-1");
        dto.setPatientId("patient-1");

        when(createConsultationUseCase.getConsultation("consult-1")).thenReturn(dto);

        mockMvc.perform(get("/api/consultations/consult-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("consult-1"));
    }

    @Test
    void terminerConsultation_shouldReturn200() throws Exception {
        ConsultationDto dto = new ConsultationDto();
        dto.setId("consult-1");
        dto.setStatut(StatutConsultation.TERMINEE);

        when(terminateConsultationUseCase.terminerConsultation("consult-1")).thenReturn(dto);

        mockMvc.perform(post("/api/consultations/consult-1/terminer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("TERMINEE"));
    }
}
