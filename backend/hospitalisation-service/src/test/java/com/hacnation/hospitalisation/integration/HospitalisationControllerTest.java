package com.hacnation.hospitalisation.integration;

import com.hacnation.common.enums.StatutLit;
import com.hacnation.hospitalisation.application.service.ManageHospitalisationUseCase;
import com.hacnation.hospitalisation.domain.model.Hospitalisation;
import com.hacnation.hospitalisation.domain.model.Lit;
import com.hacnation.hospitalisation.infrastructure.adapter.in.rest.HospitalisationController;
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

@WebMvcTest(HospitalisationController.class)
@AutoConfigureMockMvc(addFilters = false)
class HospitalisationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManageHospitalisationUseCase manageHospitalisationUseCase;

    @Test
    void admettre_shouldReturn200() throws Exception {
        Hospitalisation hospitalisation = new Hospitalisation();
        hospitalisation.setId("hosp-1");
        hospitalisation.setAdmissionId("adm-1");
        hospitalisation.setPatientId("patient-1");
        hospitalisation.setLitId("lit-1");
        hospitalisation.setMotif("Fievre");
        hospitalisation.setDateEntree(LocalDateTime.now());
        hospitalisation.setStatut("EN_COURS");

        when(manageHospitalisationUseCase.admettre(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(hospitalisation);

        mockMvc.perform(post("/api/hospitalisation/hospitalisations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"admissionId\":\"adm-1\",\"patientId\":\"patient-1\",\"service\":\"Medecine Generale\",\"motif\":\"Fievre\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("hosp-1"))
                .andExpect(jsonPath("$.patientId").value("patient-1"))
                .andExpect(jsonPath("$.statut").value("EN_COURS"));
    }

    @Test
    void sortir_shouldReturn200() throws Exception {
        Hospitalisation hospitalisation = new Hospitalisation();
        hospitalisation.setId("hosp-1");
        hospitalisation.setPatientId("patient-1");
        hospitalisation.setLitId("lit-1");
        hospitalisation.setDateEntree(LocalDateTime.now().minusDays(2));
        hospitalisation.setDateSortie(LocalDateTime.now());
        hospitalisation.setStatut("TERMINE");

        when(manageHospitalisationUseCase.sortir("hosp-1")).thenReturn(hospitalisation);

        mockMvc.perform(post("/api/hospitalisation/hospitalisations/hosp-1/sortie"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("hosp-1"))
                .andExpect(jsonPath("$.statut").value("TERMINE"));
    }

    @Test
    void getLits_shouldReturnList() throws Exception {
        Lit lit = new Lit();
        lit.setId("lit-1");
        lit.setNumero("LIT-101");
        lit.setService("Medecine Generale");
        lit.setStatut(StatutLit.DISPONIBLE);

        List<Lit> lits = Collections.singletonList(lit);

        when(manageHospitalisationUseCase.getLits("Medecine Generale")).thenReturn(lits);

        mockMvc.perform(get("/api/hospitalisation/lits")
                        .param("service", "Medecine Generale"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("lit-1"))
                .andExpect(jsonPath("$[0].statut").value("DISPONIBLE"));
    }

    @Test
    void getLitsDisponibles_shouldReturn200() throws Exception {
        when(manageHospitalisationUseCase.getLitsDisponibles("Medecine Generale")).thenReturn(5L);

        mockMvc.perform(get("/api/hospitalisation/lits/disponibles")
                        .param("service", "Medecine Generale"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.service").value("Medecine Generale"))
                .andExpect(jsonPath("$.litsDisponibles").value(5));
    }
}
