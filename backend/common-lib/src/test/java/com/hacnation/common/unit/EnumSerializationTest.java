package com.hacnation.common.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacnation.common.enums.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnumSerializationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void statutRdv_shouldSerializeAndDeserialize() throws Exception {
        String json = objectMapper.writeValueAsString(StatutRdv.CONFIRME);
        StatutRdv value = objectMapper.readValue(json, StatutRdv.class);
        assertEquals(StatutRdv.CONFIRME, value);
    }

    @Test
    void statutConsultation_shouldSerializeAndDeserialize() throws Exception {
        String json = objectMapper.writeValueAsString(StatutConsultation.EN_COURS);
        StatutConsultation value = objectMapper.readValue(json, StatutConsultation.class);
        assertEquals(StatutConsultation.EN_COURS, value);
    }

    @Test
    void role_shouldSerializeAndDeserialize() throws Exception {
        String json = objectMapper.writeValueAsString(Role.MEDECIN);
        Role value = objectMapper.readValue(json, Role.class);
        assertEquals(Role.MEDECIN, value);
    }

    @Test
    void typePrescription_shouldSerializeAndDeserialize() throws Exception {
        String json = objectMapper.writeValueAsString(TypePrescription.EXAMEN);
        TypePrescription value = objectMapper.readValue(json, TypePrescription.class);
        assertEquals(TypePrescription.EXAMEN, value);
    }

    @Test
    void canalNotification_shouldSerializeAndDeserialize() throws Exception {
        String json = objectMapper.writeValueAsString(CanalNotification.SMS);
        CanalNotification value = objectMapper.readValue(json, CanalNotification.class);
        assertEquals(CanalNotification.SMS, value);
    }

    @Test
    void modePaiement_shouldContainExpectedValues() {
        ModePaiement[] values = ModePaiement.values();
        assertTrue(values.length >= 5);
    }

    @Test
    void statutFacture_shouldContainExpectedValues() {
        StatutFacture[] values = StatutFacture.values();
        assertTrue(values.length >= 3);
    }

    @Test
    void niveauTriage_shouldHaveFiveLevels() {
        NiveauTriage[] values = NiveauTriage.values();
        assertEquals(5, values.length);
    }

    @Test
    void typeAdmission_shouldContainExpectedValues() {
        assertNotNull(TypeAdmission.valueOf("CONSULTATION"));
        assertNotNull(TypeAdmission.valueOf("URGENCE"));
        assertNotNull(TypeAdmission.valueOf("HOSPITALISATION"));
    }
}
