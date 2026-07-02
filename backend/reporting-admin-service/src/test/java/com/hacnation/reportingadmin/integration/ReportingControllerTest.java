package com.hacnation.reportingadmin.integration;

import com.hacnation.reportingadmin.domain.port.inbound.ReportingUseCase;
import com.hacnation.reportingadmin.infrastructure.adapter.inbound.ReportingController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportingController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReportingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportingUseCase reportingUseCase;

    @Test
    void dashboardFiles_shouldReturn200() throws Exception {
        Map<String, Object> dashboard = new LinkedHashMap<>();
        dashboard.put("patientsEnAttente", Map.of("Medecine Generale", 12));
        dashboard.put("tempsAttenteMoyen", 28);

        when(reportingUseCase.getDashboardFiles()).thenReturn(dashboard);

        mockMvc.perform(get("/api/reporting/dashboard/files"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tempsAttenteMoyen").value(28));
    }

    @Test
    void dashboardConsultations_shouldReturn200() throws Exception {
        Map<String, Object> dashboard = new LinkedHashMap<>();
        dashboard.put("nombreConsultations", 487);

        when(reportingUseCase.getDashboardConsultations(any(), any())).thenReturn(dashboard);

        mockMvc.perform(get("/api/reporting/dashboard/consultations")
                        .param("dateDebut", "2026-06-01")
                        .param("dateFin", "2026-06-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreConsultations").value(487));
    }

    @Test
    void dashboardRecettes_shouldReturn200() throws Exception {
        Map<String, Object> dashboard = new LinkedHashMap<>();
        dashboard.put("recettesTotales", 18500000);
        dashboard.put("tauxRecouvrement", 78.5);

        when(reportingUseCase.getDashboardRecettes(any(), any())).thenReturn(dashboard);

        mockMvc.perform(get("/api/reporting/dashboard/recettes")
                        .param("dateDebut", "2026-06-01")
                        .param("dateFin", "2026-06-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recettesTotales").value(18500000))
                .andExpect(jsonPath("$.tauxRecouvrement").value(78.5));
    }

    @Test
    void dashboardStocks_shouldReturn200() throws Exception {
        Map<String, Object> dashboard = new LinkedHashMap<>();
        dashboard.put("rupturesStock", 13);
        dashboard.put("valeurStock", 125000000);

        when(reportingUseCase.getDashboardStocks()).thenReturn(dashboard);

        mockMvc.perform(get("/api/reporting/dashboard/stocks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rupturesStock").value(13))
                .andExpect(jsonPath("$.valeurStock").value(125000000));
    }

    @Test
    void dashboardUrgences_shouldReturn200() throws Exception {
        Map<String, Object> dashboard = new LinkedHashMap<>();
        dashboard.put("admissionsUrgences", 34);
        dashboard.put("tempsPriseEnChargeMoyen", 18);

        when(reportingUseCase.getDashboardUrgences(any())).thenReturn(dashboard);

        mockMvc.perform(get("/api/reporting/dashboard/urgences")
                        .param("date", "2026-07-02"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.admissionsUrgences").value(34))
                .andExpect(jsonPath("$.tempsPriseEnChargeMoyen").value(18));
    }

    @Test
    void dashboardGlobal_shouldReturn200() throws Exception {
        Map<String, Object> files = Map.of("tempsAttenteMoyen", 28);
        Map<String, Object> consultations = Map.of("nombreConsultations", 487);
        Map<String, Object> recettes = Map.of("recettesTotales", 18500000);
        Map<String, Object> stocks = Map.of("rupturesStock", 13);
        Map<String, Object> urgences = Map.of("admissionsUrgences", 34);

        when(reportingUseCase.getDashboardFiles()).thenReturn(files);
        when(reportingUseCase.getDashboardConsultations(any(), any())).thenReturn(consultations);
        when(reportingUseCase.getDashboardRecettes(any(), any())).thenReturn(recettes);
        when(reportingUseCase.getDashboardStocks()).thenReturn(stocks);
        when(reportingUseCase.getDashboardUrgences(any())).thenReturn(urgences);

        mockMvc.perform(get("/api/reporting/dashboard/global"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.files.tempsAttenteMoyen").value(28))
                .andExpect(jsonPath("$.consultations.nombreConsultations").value(487))
                .andExpect(jsonPath("$.recettes.recettesTotales").value(18500000))
                .andExpect(jsonPath("$.stocks.rupturesStock").value(13))
                .andExpect(jsonPath("$.urgences.admissionsUrgences").value(34));
    }
}
