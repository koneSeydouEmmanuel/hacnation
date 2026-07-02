package com.hacnation.accueil.integration;

import com.hacnation.accueil.application.service.RegisterAdmissionUseCase;
import com.hacnation.accueil.domain.model.Admission;
import com.hacnation.accueil.infrastructure.adapter.in.rest.AdmissionController;
import com.hacnation.common.enums.TypeAdmission;
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

@WebMvcTest(AdmissionController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdmissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegisterAdmissionUseCase registerAdmissionUseCase;

    @Test
    void createAdmission_shouldReturn200() throws Exception {
        Admission admission = new Admission();
        admission.setId("adm-1");
        admission.setPatientId("patient-1");
        admission.setType(TypeAdmission.CONSULTATION);
        admission.setService("Medecine Generale");
        admission.setMotif("Fievre");
        admission.setStatut("ENREGISTREE");
        admission.setDateAdmission(LocalDateTime.now());

        when(registerAdmissionUseCase.createAdmission(anyString(), any(), anyString(), anyString()))
                .thenReturn(admission);

        mockMvc.perform(post("/api/accueil/admissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"patientId\":\"patient-1\",\"type\":\"CONSULTATION\",\"service\":\"Medecine Generale\",\"motif\":\"Fievre\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("adm-1"))
                .andExpect(jsonPath("$.patientId").value("patient-1"))
                .andExpect(jsonPath("$.service").value("Medecine Generale"))
                .andExpect(jsonPath("$.statut").value("ENREGISTREE"));
    }

    @Test
    void createAdmission_shouldValidateRequiredFields() throws Exception {
        mockMvc.perform(post("/api/accueil/admissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"patientId\":\"\",\"type\":\"\",\"service\":\"\",\"motif\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAdmissionsByPatient_shouldReturnList() throws Exception {
        Admission admission = new Admission();
        admission.setId("adm-1");
        admission.setPatientId("patient-1");
        admission.setType(TypeAdmission.URGENCE);
        admission.setService("Urgences");
        admission.setStatut("ENREGISTREE");

        List<Admission> admissions = Collections.singletonList(admission);

        when(registerAdmissionUseCase.getAdmissionsByPatient("patient-1"))
                .thenReturn(admissions);

        mockMvc.perform(get("/api/accueil/admissions")
                        .param("patientId", "patient-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("adm-1"))
                .andExpect(jsonPath("$[0].service").value("Urgences"));
    }

    @Test
    void getAdmission_shouldReturn200() throws Exception {
        Admission admission = new Admission();
        admission.setId("adm-1");
        admission.setPatientId("patient-1");
        admission.setType(TypeAdmission.HOSPITALISATION);
        admission.setService("Cardiologie");
        admission.setStatut("ENREGISTREE");

        when(registerAdmissionUseCase.getAdmission("adm-1")).thenReturn(admission);

        mockMvc.perform(get("/api/accueil/admissions/adm-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("adm-1"))
                .andExpect(jsonPath("$.type").value("HOSPITALISATION"));
    }
}
