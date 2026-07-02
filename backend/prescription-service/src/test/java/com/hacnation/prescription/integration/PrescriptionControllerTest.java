package com.hacnation.prescription.integration;

import com.hacnation.common.dto.PrescriptionDto;
import com.hacnation.common.enums.StatutPrescription;
import com.hacnation.common.enums.TypePrescription;
import com.hacnation.prescription.application.service.CreatePrescriptionUseCase;
import com.hacnation.prescription.infrastructure.adapter.in.rest.PrescriptionController;
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

@WebMvcTest(PrescriptionController.class)
class PrescriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreatePrescriptionUseCase createPrescriptionUseCase;

    @Test
    void createPrescription_shouldReturn201() throws Exception {
        PrescriptionDto dto = new PrescriptionDto();
        dto.setId("presc-1");
        dto.setConsultationId("consult-1");
        dto.setType(TypePrescription.EXAMEN);
        dto.setStatut(StatutPrescription.EN_ATTENTE);

        when(createPrescriptionUseCase.createPrescription(anyString(), any(), anyMap(), anyString()))
                .thenReturn(dto);

        mockMvc.perform(post("/api/prescriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"consultationId\":\"consult-1\",\"type\":\"EXAMEN\",\"details\":{\"type_examen\":\"NFS\"},\"patientId\":\"patient-1\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void getPrescriptions_shouldReturn200() throws Exception {
        when(createPrescriptionUseCase.getPrescriptionsByConsultation("consult-1"))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/prescriptions?consultationId=consult-1"))
                .andExpect(status().isOk());
    }
}
