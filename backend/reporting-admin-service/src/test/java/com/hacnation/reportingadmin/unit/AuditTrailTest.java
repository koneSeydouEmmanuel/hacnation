package com.hacnation.reportingadmin.unit;

import com.hacnation.reportingadmin.application.service.AdminServiceImpl;
import com.hacnation.reportingadmin.domain.port.outbound.ActeMedicalRepositoryPort;
import com.hacnation.reportingadmin.domain.port.outbound.ParametrageRepositoryPort;
import com.hacnation.reportingadmin.domain.port.outbound.ServiceMedicalRepositoryPort;
import com.hacnation.reportingadmin.domain.port.outbound.TarifRepositoryPort;
import com.hacnation.reportingadmin.domain.port.outbound.UtilisateurRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuditTrailTest {

    @Mock
    private UtilisateurRepositoryPort utilisateurRepository;

    @Mock
    private ServiceMedicalRepositoryPort serviceMedicalRepository;

    @Mock
    private ActeMedicalRepositoryPort acteMedicalRepository;

    @Mock
    private TarifRepositoryPort tarifRepository;

    @Mock
    private ParametrageRepositoryPort parametrageRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    void getAuditTrail_shouldReturnEntries() {
        List<Map<String, Object>> audit = adminService.getAuditTrail();

        assertNotNull(audit);
        assertFalse(audit.isEmpty());
        assertEquals(12, audit.size());
    }

    @Test
    void getAuditTrailFiltre_filterByAction_shouldReturnFiltered() {
        List<Map<String, Object>> result = adminService.getAuditTrailFiltre(
                "LOGIN", null, null, null, null);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        result.forEach(entry -> assertEquals("LOGIN", entry.get("action")));
    }

    @Test
    void getAuditTrailFiltre_filterByUser_shouldReturnFiltered() {
        List<Map<String, Object>> result = adminService.getAuditTrailFiltre(
                null, "admin", null, null, null);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        result.forEach(entry -> assertEquals("admin", entry.get("utilisateur")));
    }

    @Test
    void getAuditTrailFiltre_filterByNonExistent_shouldReturnEmpty() {
        List<Map<String, Object>> result = adminService.getAuditTrailFiltre(
                "NONEXISTENT", null, null, null, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAuditTrailFiltre_filterByEmptyString_shouldReturnAll() {
        List<Map<String, Object>> result = adminService.getAuditTrailFiltre(
                "", "", null, null, "");

        assertNotNull(result);
        assertEquals(12, result.size());
    }
}
