package com.hacnation.soins.integration;

import com.hacnation.soins.domain.model.SoinInfirmier;
import com.hacnation.soins.domain.port.inbound.SoinsUseCase;
import com.hacnation.soins.infrastructure.adapter.inbound.SoinsController;
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

@WebMvcTest(SoinsController.class)
@AutoConfigureMockMvc(addFilters = false)
class SoinsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SoinsUseCase soinsUseCase;

    @Test
    void creerPlanSoins_shouldReturn200() throws Exception {
        SoinInfirmier soin = new SoinInfirmier();
        soin.setId("soin-1");
        soin.setHospitalisationId("hosp-1");
        soin.setPatientId("patient-1");
        soin.setTypeSoin("PANSEMENT");
        soin.setDescription("Pansement plaie");
        soin.setFrequence("2x/jour");
        soin.setInstructions("Utiliser compresses steriles");
        soin.setStatut("EN_ATTENTE");
        soin.setCreatedAt(LocalDateTime.now());

        when(soinsUseCase.creerPlanSoins(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(soin);

        mockMvc.perform(post("/api/soins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"hospitalisationId\":\"hosp-1\",\"patientId\":\"patient-1\",\"typeSoin\":\"PANSEMENT\",\"description\":\"Pansement plaie\",\"frequence\":\"2x/jour\",\"instructions\":\"Utiliser compresses steriles\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("soin-1"))
                .andExpect(jsonPath("$.typeSoin").value("PANSEMENT"))
                .andExpect(jsonPath("$.statut").value("EN_ATTENTE"));
    }

    @Test
    void getSoinsPatient_shouldReturnList() throws Exception {
        SoinInfirmier soin = new SoinInfirmier();
        soin.setId("soin-1");
        soin.setPatientId("patient-1");
        soin.setTypeSoin("INJECTION");
        soin.setStatut("ADMINISTRE");

        List<SoinInfirmier> soins = Collections.singletonList(soin);

        when(soinsUseCase.getSoinsPatient("patient-1")).thenReturn(soins);

        mockMvc.perform(get("/api/soins")
                        .param("patientId", "patient-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("soin-1"))
                .andExpect(jsonPath("$[0].statut").value("ADMINISTRE"));
    }

    @Test
    void administrerSoin_shouldReturn200() throws Exception {
        SoinInfirmier soin = new SoinInfirmier();
        soin.setId("soin-1");
        soin.setPatientId("patient-1");
        soin.setTypeSoin("PANSEMENT");
        soin.setStatut("ADMINISTRE");
        soin.setInfirmierId("inf-1");
        soin.setDateAdministration(LocalDateTime.now());

        when(soinsUseCase.administrerSoin("soin-1", "inf-1")).thenReturn(soin);

        mockMvc.perform(post("/api/soins/soin-1/administrer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"infirmierId\":\"inf-1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("soin-1"))
                .andExpect(jsonPath("$.statut").value("ADMINISTRE"))
                .andExpect(jsonPath("$.infirmierId").value("inf-1"));
    }

    @Test
    void nonAdministre_shouldReturn200() throws Exception {
        SoinInfirmier soin = new SoinInfirmier();
        soin.setId("soin-1");
        soin.setPatientId("patient-1");
        soin.setTypeSoin("PANSEMENT");
        soin.setStatut("NON_ADMINISTRE");
        soin.setMotifNonAdministration("Patient refuse");

        when(soinsUseCase.nonAdministre("soin-1", "Patient refuse")).thenReturn(soin);

        mockMvc.perform(post("/api/soins/soin-1/non-administre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"motif\":\"Patient refuse\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("soin-1"))
                .andExpect(jsonPath("$.statut").value("NON_ADMINISTRE"))
                .andExpect(jsonPath("$.motifNonAdministration").value("Patient refuse"));
    }
}
