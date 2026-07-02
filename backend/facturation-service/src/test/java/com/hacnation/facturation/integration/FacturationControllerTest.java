package com.hacnation.facturation.integration;

import com.hacnation.facturation.application.service.GenerateInvoiceUseCase;
import com.hacnation.facturation.application.service.GenerateInvoiceUseCase.FactureDto;
import com.hacnation.facturation.infrastructure.adapter.in.rest.FacturationController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacturationController.class)
class FacturationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenerateInvoiceUseCase generateInvoiceUseCase;

    @Test
    void genererFacture_shouldReturn200() throws Exception {
        FactureDto dto = new FactureDto();
        dto.setId("facture-1");
        dto.setPatientId("patient-1");
        dto.setTotal(new BigDecimal("5000"));

        when(generateInvoiceUseCase.genererFacture(anyString(), anyString(), anyList()))
                .thenReturn(dto);

        mockMvc.perform(post("/api/facturation/generer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"consultationId\":\"consult-1\",\"patientId\":\"patient-1\",\"actes\":[{\"description\":\"Consultation\",\"quantite\":1,\"prixUnitaire\":5000}]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(5000));
    }

    @Test
    void getFacture_shouldReturn200() throws Exception {
        FactureDto dto = new FactureDto();
        dto.setId("facture-1");
        dto.setTotal(new BigDecimal("5000"));

        when(generateInvoiceUseCase.getFacture("facture-1")).thenReturn(dto);

        mockMvc.perform(get("/api/facturation/factures/facture-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("facture-1"));
    }
}
