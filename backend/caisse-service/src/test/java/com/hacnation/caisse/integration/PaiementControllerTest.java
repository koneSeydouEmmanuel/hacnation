package com.hacnation.caisse.integration;

import com.hacnation.caisse.application.service.ProcessPaymentUseCase;
import com.hacnation.caisse.infrastructure.adapter.in.rest.PaiementController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaiementController.class)
class PaiementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProcessPaymentUseCase processPaymentUseCase;

    @Test
    void payer_shouldReturn200() throws Exception {
        Map<String, Object> receipt = new LinkedHashMap<>();
        receipt.put("factureId", "facture-1");
        receipt.put("reference", "TXN-ABC12345");
        receipt.put("statut", "SUCCES");

        when(processPaymentUseCase.payer(anyString(), anyString(), any(), anyString(), anyString(), any()))
                .thenReturn(receipt);

        mockMvc.perform(post("/api/caisse/payer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"factureId\":\"facture-1\",\"patientId\":\"patient-1\",\"totalFacture\":5000,\"modePaiement\":\"ESPECES\",\"telephone\":\"+2250100000001\",\"montant\":5000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("SUCCES"));
    }

    @Test
    void deletePaiement_shouldReturn204_whenNotPayee() throws Exception {
        doNothing().when(processPaymentUseCase).supprimerPaiement("paiement-1");

        mockMvc.perform(delete("/api/caisse/paiement-1"))
                .andExpect(status().isNoContent());
    }
}
