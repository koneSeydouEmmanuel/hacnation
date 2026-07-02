package com.hacnation.reportingadmin.integration;

import com.hacnation.reportingadmin.domain.model.ParametrageSysteme;
import com.hacnation.reportingadmin.domain.model.Utilisateur;
import com.hacnation.reportingadmin.domain.port.inbound.AdminUseCase;
import com.hacnation.reportingadmin.infrastructure.adapter.inbound.AdminController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminUseCase adminUseCase;

    @Test
    void getAllUtilisateurs_shouldReturn200() throws Exception {
        when(adminUseCase.getAllUtilisateurs()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/admin/utilisateurs"))
                .andExpect(status().isOk());
    }

    @Test
    void createUtilisateur_shouldReturn200() throws Exception {
        Utilisateur user = new Utilisateur();
        user.setId("user-1");
        user.setUsername("dr.kone");

        when(adminUseCase.createUtilisateur(any(Utilisateur.class))).thenReturn(user);

        mockMvc.perform(post("/api/admin/utilisateurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"dr.kone\",\"nom\":\"Kone\",\"prenom\":\"Dr\",\"password\":\"secret\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("dr.kone"));
    }

    @Test
    void deleteUtilisateur_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/admin/utilisateurs/user-1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllServices_shouldReturn200() throws Exception {
        when(adminUseCase.getAllServices()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/admin/services"))
                .andExpect(status().isOk());
    }

    @Test
    void getAuditTrail_shouldReturn200() throws Exception {
        when(adminUseCase.getAuditTrailFiltre(any(), any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/admin/audit"))
                .andExpect(status().isOk());
    }

    @Test
    void getHealth_shouldReturn200() throws Exception {
        when(adminUseCase.getHealthStatus()).thenReturn(java.util.Map.of("status", "UP"));

        mockMvc.perform(get("/api/admin/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }
}
