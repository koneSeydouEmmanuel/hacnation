package com.hacnation.dme.integration;

import com.hacnation.dme.application.service.GetDmeUseCase;
import com.hacnation.dme.infrastructure.adapter.in.rest.DmeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DmeController.class)
class DmeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetDmeUseCase getDmeUseCase;

    @Test
    void getDme_shouldReturn200() throws Exception {
        Map<String, Object> dmeMap = new LinkedHashMap<>();
        dmeMap.put("id", "dme-1");
        dmeMap.put("patientId", "patient-1");
        dmeMap.put("antecedents", java.util.Collections.emptyList());

        when(getDmeUseCase.execute("patient-1")).thenReturn(dmeMap);

        mockMvc.perform(get("/api/patients/patient-1/dme"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("dme-1"));
    }

    @Test
    void addAntecedent_shouldReturn201() throws Exception {
        Map<String, Object> dmeMap = new LinkedHashMap<>();
        dmeMap.put("id", "dme-1");
        dmeMap.put("patientId", "patient-1");

        when(getDmeUseCase.addAntecedent(eq("patient-1"), anyMap())).thenReturn(dmeMap);

        mockMvc.perform(post("/api/patients/patient-1/dme/antecedents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\":\"Allergie\",\"description\":\"Penicilline\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void addNoteClinique_shouldReturn201() throws Exception {
        Map<String, Object> dmeMap = new LinkedHashMap<>();
        dmeMap.put("id", "dme-1");

        when(getDmeUseCase.addNoteClinique(eq("patient-1"), anyMap())).thenReturn(dmeMap);

        mockMvc.perform(post("/api/patients/patient-1/dme/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\":\"Observation\",\"contenu\":\"Patient stable\"}"))
                .andExpect(status().isCreated());
    }
}
