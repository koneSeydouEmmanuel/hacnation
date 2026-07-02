package com.hacnation.facturation.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacnation.facturation.application.service.GenerateInvoiceUseCase;
import com.hacnation.facturation.application.service.GenerateInvoiceUseCase.FactureDto;
import com.hacnation.facturation.domain.model.Facture;
import com.hacnation.facturation.domain.port.BillingEventPublisherPort;
import com.hacnation.facturation.domain.port.FactureRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenerateInvoiceUseCaseTest {

    @Mock
    private FactureRepositoryPort factureRepository;

    @Mock
    private BillingEventPublisherPort eventPublisher;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private GenerateInvoiceUseCase generateInvoiceUseCase;

    @Test
    void genererFacture_shouldGenerateWithCorrectTotal() throws Exception {
        Facture savedFacture = new Facture();
        savedFacture.setId("facture-1");
        savedFacture.setPatientId("patient-1");
        savedFacture.setConsultationId("consult-1");
        savedFacture.setLignes("[{\"description\":\"Consultation\",\"quantite\":1,\"prixUnitaire\":5000,\"montant\":5000}]");
        savedFacture.setTotal(new BigDecimal("5000"));

        when(factureRepository.save(any(Facture.class))).thenReturn(savedFacture);

        FactureDto result = generateInvoiceUseCase.genererFacture("consult-1", "patient-1",
                List.of(Map.of("description", "Consultation", "quantite", 1, "prixUnitaire", new BigDecimal("5000"))));

        assertNotNull(result);
        assertEquals("patient-1", result.getPatientId());
        assertEquals(new BigDecimal("5000"), result.getTotal());
        verify(eventPublisher).publishBillingEvent(any());
        verify(eventPublisher).publishNotificationEvent(any());
    }

    @Test
    void genererFacture_shouldCalculateTotalMultipleLines() throws Exception {
        Facture savedFacture = new Facture();
        savedFacture.setId("facture-2");
        savedFacture.setPatientId("patient-1");
        savedFacture.setConsultationId("consult-1");
        savedFacture.setLignes("[{}]");
        savedFacture.setTotal(new BigDecimal("15000"));

        when(factureRepository.save(any(Facture.class))).thenReturn(savedFacture);

        FactureDto result = generateInvoiceUseCase.genererFacture("consult-1", "patient-1",
                List.of(
                        Map.of("description", "Consultation", "quantite", 1, "prixUnitaire", new BigDecimal("5000")),
                        Map.of("description", "Radiographie", "quantite", 2, "prixUnitaire", new BigDecimal("5000"))
                ));

        assertNotNull(result);
        assertEquals(new BigDecimal("15000"), result.getTotal());
    }
}
