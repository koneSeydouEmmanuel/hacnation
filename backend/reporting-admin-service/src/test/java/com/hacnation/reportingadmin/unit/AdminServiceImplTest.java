package com.hacnation.reportingadmin.unit;

import com.hacnation.reportingadmin.application.service.AdminServiceImpl;
import com.hacnation.reportingadmin.domain.model.ActeMedical;
import com.hacnation.reportingadmin.domain.model.ServiceMedical;
import com.hacnation.reportingadmin.domain.model.Tarif;
import com.hacnation.reportingadmin.domain.model.Utilisateur;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

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
    void getAllUtilisateurs_shouldReturnList() {
        when(utilisateurRepository.findAll()).thenReturn(Collections.emptyList());

        List<Utilisateur> result = adminService.getAllUtilisateurs();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void createUtilisateur_shouldSave() {
        Utilisateur user = new Utilisateur();
        user.setUsername("dr.kone");
        user.setNom("Kone");
        user.setPrenom("Dr");

        Utilisateur saved = new Utilisateur();
        saved.setId("user-1");
        saved.setUsername("dr.kone");
        saved.setPassword("$2a$10$hashed_placeholder_password");

        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(saved);

        Utilisateur result = adminService.createUtilisateur(user);

        assertNotNull(result);
        assertEquals("dr.kone", result.getUsername());
        verify(utilisateurRepository).save(any(Utilisateur.class));
    }

    @Test
    void createService_shouldSave() {
        ServiceMedical service = new ServiceMedical();
        service.setCode("MED_GEN");
        service.setLibelle("Medecine Generale");

        ServiceMedical saved = new ServiceMedical();
        saved.setId("svc-1");
        saved.setCode("MED_GEN");
        saved.setLibelle("Medecine Generale");

        when(serviceMedicalRepository.save(any(ServiceMedical.class))).thenReturn(saved);

        ServiceMedical result = adminService.createService(service);

        assertNotNull(result);
        assertEquals("MED_GEN", result.getCode());
    }

    @Test
    void createActe_shouldSave() {
        ActeMedical acte = new ActeMedical();
        acte.setCode("CONS_GEN");
        acte.setLibelle("Consultation Generale");

        ActeMedical saved = new ActeMedical();
        saved.setId("acte-1");
        saved.setCode("CONS_GEN");
        saved.setLibelle("Consultation Generale");

        when(acteMedicalRepository.save(any(ActeMedical.class))).thenReturn(saved);

        ActeMedical result = adminService.createActe(acte);

        assertNotNull(result);
        assertEquals("CONS_GEN", result.getCode());
    }

    @Test
    void createTarif_shouldSave() {
        Tarif tarif = new Tarif();
        tarif.setActeMedicalId("acte-1");
        tarif.setServiceId("svc-1");

        Tarif saved = new Tarif();
        saved.setId("tarif-1");
        saved.setActeMedicalId("acte-1");
        saved.setServiceId("svc-1");

        when(tarifRepository.save(any(Tarif.class))).thenReturn(saved);

        Tarif result = adminService.createTarif(tarif);

        assertNotNull(result);
        assertEquals("acte-1", result.getActeMedicalId());
    }
}
