package com.hacnation.rdv.integration;

import com.hacnation.common.dto.RdvDto;
import com.hacnation.common.enums.StatutRdv;
import com.hacnation.rdv.application.service.CreateRdvUseCase;
import com.hacnation.rdv.application.service.GetCreneauxUseCase;
import com.hacnation.rdv.infrastructure.adapter.in.rest.RdvController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RdvController.class)
class RdvControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateRdvUseCase createRdvUseCase;

    @MockBean
    private GetCreneauxUseCase getCreneauxUseCase;

    @Test
    void createRdv_shouldReturn201() throws Exception {
        RdvDto rdv = new RdvDto();
        rdv.setId("rdv-1");
        rdv.setPatientId("patient-1");
        rdv.setService("CONSULTATION");
        rdv.setStatut(StatutRdv.EN_ATTENTE);

        when(createRdvUseCase.createRdv(anyString(), anyString(), anyString(), any(), anyString()))
                .thenReturn(rdv);

        mockMvc.perform(post("/api/rdv")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"patientId\":\"patient-1\",\"praticienId\":\"praticien-1\",\"service\":\"CONSULTATION\",\"dateHeure\":\"2026-07-03T10:00:00\",\"motif\":\"Consultation\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void getRdv_shouldReturn200() throws Exception {
        RdvDto rdv = new RdvDto();
        rdv.setId("rdv-1");
        rdv.setPatientId("patient-1");
        rdv.setStatut(StatutRdv.EN_ATTENTE);

        when(createRdvUseCase.getRdv("rdv-1")).thenReturn(rdv);

        mockMvc.perform(get("/api/rdv/rdv-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("rdv-1"));
    }

    @Test
    void getRdvsByPatient_shouldReturn200() throws Exception {
        when(createRdvUseCase.getRdvsByPatient("patient-1"))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/rdv?patientId=patient-1"))
                .andExpect(status().isOk());
    }

    @Test
    void getQrCode_shouldReturn200() throws Exception {
        RdvDto rdv = new RdvDto();
        rdv.setId("rdv-1");
        rdv.setQrCode("base64qr");

        when(createRdvUseCase.getRdv("rdv-1")).thenReturn(rdv);

        mockMvc.perform(get("/api/rdv/rdv-1/qr"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.qrCode").value("base64qr"));
    }
}
