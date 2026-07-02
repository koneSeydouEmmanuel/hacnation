package com.hacnation.reportingadmin.unit;

import com.hacnation.common.exception.BusinessException;
import com.hacnation.reportingadmin.application.service.AdminServiceImpl;
import com.hacnation.reportingadmin.domain.model.ParametrageSysteme;
import com.hacnation.reportingadmin.domain.port.outbound.ActeMedicalRepositoryPort;
import com.hacnation.reportingadmin.domain.port.outbound.ParametrageRepositoryPort;
import com.hacnation.reportingadmin.domain.port.outbound.ServiceMedicalRepositoryPort;
import com.hacnation.reportingadmin.domain.port.outbound.TarifRepositoryPort;
import com.hacnation.reportingadmin.domain.port.outbound.UtilisateurRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParametrageSystemeTest {

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
    void createParametre_numeric_shouldValidate() {
        ParametrageSysteme param = new ParametrageSysteme();
        param.setCle("QUOTA_RDV_PAR_CRENEAU");
        param.setValeur("5");
        param.setModifiable(true);

        ParametrageSysteme saved = new ParametrageSysteme();
        saved.setId("param-1");
        saved.setCle("QUOTA_RDV_PAR_CRENEAU");
        saved.setValeur("5");

        when(parametrageRepository.save(any(ParametrageSysteme.class))).thenReturn(saved);

        ParametrageSysteme result = adminService.createParametre(param);

        assertNotNull(result);
        assertEquals("5", result.getValeur());
    }

    @Test
    void createParametre_nonNumericValue_shouldThrow400() {
        ParametrageSysteme param = new ParametrageSysteme();
        param.setCle("DUREE_CONSULTATION_DEFAUT");
        param.setValeur("abc");

        BusinessException exception = assertThrows(BusinessException.class, () ->
                adminService.createParametre(param));

        assertEquals(400, exception.getStatus());
        verify(parametrageRepository, never()).save(any());
    }

    @Test
    void updateParametre_notModifiable_shouldThrow403() {
        ParametrageSysteme existing = new ParametrageSysteme();
        existing.setId("param-1");
        existing.setCle("IMMUTABLE_PARAM");
        existing.setValeur("old");
        existing.setModifiable(false);

        ParametrageSysteme updated = new ParametrageSysteme();
        updated.setValeur("new");
        updated.setModifiable(false);

        when(parametrageRepository.findById("param-1")).thenReturn(Optional.of(existing));

        BusinessException exception = assertThrows(BusinessException.class, () ->
                adminService.updateParametre("param-1", updated));

        assertEquals(403, exception.getStatus());
    }

    @Test
    void getAllParametres_shouldReturnList() {
        when(parametrageRepository.findAll()).thenReturn(Collections.emptyList());

        var result = adminService.getAllParametres();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
