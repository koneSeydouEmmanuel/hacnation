package com.hacnation.patientidentity.integration;

import com.hacnation.common.dto.PatientDto;
import com.hacnation.patientidentity.application.service.CreatePatientUseCase;
import com.hacnation.patientidentity.application.service.SearchPatientUseCase;
import com.hacnation.patientidentity.application.service.UpdatePatientUseCase;
import com.hacnation.patientidentity.infrastructure.adapter.in.rest.PatientController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchPatientUseCase searchPatientUseCase;

    @MockBean
    private CreatePatientUseCase createPatientUseCase;

    @MockBean
    private UpdatePatientUseCase updatePatientUseCase;

    @Test
    void createPatient_shouldReturn201() throws Exception {
        PatientDto dto = new PatientDto();
        dto.setId("patient-1");
        dto.setNom("Doe");
        dto.setPrenom("John");
        dto.setStatut("ACTIF");

        when(createPatientUseCase.execute(any(PatientDto.class))).thenReturn(dto);

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\":\"Doe\",\"prenom\":\"John\",\"dateNaissance\":\"1990-01-01\",\"sexe\":\"M\",\"telephone\":\"+2250100000001\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void getPatient_shouldReturn200() throws Exception {
        PatientDto dto = new PatientDto();
        dto.setId("patient-1");
        dto.setNom("Doe");
        dto.setPrenom("John");

        when(searchPatientUseCase.getPatient("patient-1")).thenReturn(dto);

        mockMvc.perform(get("/api/patients/patient-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Doe"));
    }

    @Test
    void searchPatients_shouldReturn200() throws Exception {
        Page<PatientDto> emptyPage = new PageImpl<>(Collections.emptyList());
        when(searchPatientUseCase.searchPatients(anyString(), any())).thenReturn(emptyPage);

        mockMvc.perform(get("/api/patients?q=test"))
                .andExpect(status().isOk());
    }

    @Test
    void updatePatient_shouldReturn200() throws Exception {
        PatientDto dto = new PatientDto();
        dto.setId("patient-1");
        dto.setNom("Updated");

        when(updatePatientUseCase.updatePatient(eq("patient-1"), any(PatientDto.class))).thenReturn(dto);

        mockMvc.perform(put("/api/patients/patient-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\":\"Updated\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Updated"));
    }

    @Test
    void deletePatient_shouldReturn200() throws Exception {
        PatientDto dto = new PatientDto();
        dto.setId("patient-1");
        dto.setStatut("INACTIF");

        when(updatePatientUseCase.deletePatient("patient-1")).thenReturn(dto);

        mockMvc.perform(delete("/api/patients/patient-1"))
                .andExpect(status().isOk());
    }
}
