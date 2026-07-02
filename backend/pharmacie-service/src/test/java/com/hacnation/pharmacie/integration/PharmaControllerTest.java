package com.hacnation.pharmacie.integration;

import com.hacnation.pharmacie.application.service.DeliverMedicationUseCase;
import com.hacnation.pharmacie.application.service.ManageStockUseCase;
import com.hacnation.pharmacie.domain.model.Ordonnance;
import com.hacnation.pharmacie.domain.model.StockMedicament;
import com.hacnation.pharmacie.infrastructure.adapter.in.rest.PharmaController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PharmaController.class)
class PharmaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliverMedicationUseCase deliverMedicationUseCase;

    @MockBean
    private ManageStockUseCase manageStockUseCase;

    @Test
    void getStock_shouldReturn200() throws Exception {
        when(manageStockUseCase.getStock()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/pharma/stock"))
                .andExpect(status().isOk());
    }

    @Test
    void addStock_shouldReturn200() throws Exception {
        StockMedicament stock = new StockMedicament();
        stock.setId("stock-1");
        stock.setMedicamentId("med-1");
        stock.setNom("Paracetamol");
        stock.setQuantite(100);

        when(manageStockUseCase.addStock(anyString(), anyString(), anyString(), anyInt(), any(), any(), anyString()))
                .thenReturn(stock);

        mockMvc.perform(post("/api/pharma/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"medicamentId\":\"med-1\",\"nom\":\"Paracetamol\",\"lot\":\"LOT-001\",\"quantite\":100,\"datePeremption\":\"2027-12-31\",\"seuilMin\":10,\"emplacement\":\"Armoire A\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Paracetamol"));
    }

    @Test
    void getOrdonnances_shouldReturn200() throws Exception {
        when(deliverMedicationUseCase.getOrdonnancesEnAttente(any()))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/pharma/ordonnances"))
                .andExpect(status().isOk());
    }
}
