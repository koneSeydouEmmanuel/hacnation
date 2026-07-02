package com.hacnation.fileattente.integration;

import com.hacnation.common.dto.FileAttenteDto;
import com.hacnation.common.enums.StatutFileAttente;
import com.hacnation.fileattente.application.service.CallNextUseCase;
import com.hacnation.fileattente.application.service.CheckInUseCase;
import com.hacnation.fileattente.infrastructure.adapter.in.rest.QueueController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QueueController.class)
class QueueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CheckInUseCase checkInUseCase;

    @MockBean
    private CallNextUseCase callNextUseCase;

    @Test
    void checkin_shouldReturn200() throws Exception {
        FileAttenteDto dto = new FileAttenteDto();
        dto.setId("entry-1");
        dto.setPatientId("patient-1");
        dto.setService("CONSULTATION");
        dto.setPosition(1);
        dto.setStatut(StatutFileAttente.EN_ATTENTE);

        when(checkInUseCase.execute(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(dto);

        mockMvc.perform(post("/api/queue/checkin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"patientId\":\"patient-1\",\"rdvId\":\"rdv-1\",\"service\":\"CONSULTATION\",\"patientNom\":\"Doe\",\"patientPrenom\":\"John\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value("patient-1"));
    }

    @Test
    void checkinQr_shouldReturn200() throws Exception {
        FileAttenteDto dto = new FileAttenteDto();
        dto.setId("entry-1");
        dto.setPatientId("patient-1");

        when(checkInUseCase.checkinByQr(anyString())).thenReturn(dto);

        mockMvc.perform(post("/api/queue/checkin-qr")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"qrData\":\"RDV:rdv-1:patient-1:Doe:John:2026-07-02:CONSULTATION\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void getPosition_shouldReturn200() throws Exception {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("patientId", "patient-1");
        info.put("position", 3);

        when(checkInUseCase.getPositionInfo("patient-1")).thenReturn(info);

        mockMvc.perform(get("/api/queue/patient-1/position"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.position").value(3));
    }

    @Test
    void getFileAttente_shouldReturn200() throws Exception {
        when(checkInUseCase.getFileAttente("CONSULTATION"))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/queue?service=CONSULTATION"))
                .andExpect(status().isOk());
    }
}
