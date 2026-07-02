package com.hacnation.caisse.unit;

import com.hacnation.common.exception.BusinessException;
import com.hacnation.caisse.application.service.ProcessPaymentUseCase;
import com.hacnation.caisse.domain.model.Paiement;
import com.hacnation.caisse.domain.port.PaiementRepositoryPort;
import com.hacnation.caisse.domain.port.PaymentEventPublisherPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessPaymentUseCaseTest {

    @Mock
    private PaiementRepositoryPort paiementRepository;

    @Mock
    private PaymentEventPublisherPort eventPublisher;

    @InjectMocks
    private ProcessPaymentUseCase processPaymentUseCase;

    @Test
    void payer_especes_shouldCreatePayment() {
        Map<String, Object> receipt = processPaymentUseCase.payer(
                "facture-1", "patient-1", new BigDecimal("5000"),
                "ESPECES", "+2250100000001", new BigDecimal("5000"));

        assertNotNull(receipt);
        assertEquals("facture-1", receipt.get("factureId"));
        assertEquals("SUCCES", receipt.get("statut"));
        assertNotNull(receipt.get("reference"));
        verify(paiementRepository).save(any(Paiement.class));
        verify(eventPublisher).publishBillingEvent(any());
        verify(eventPublisher).publishNotificationEvent(any());
    }

    @Test
    void payer_mobileMoney_shouldCreatePaymentWithDelay() {
        Map<String, Object> receipt = processPaymentUseCase.payer(
                "facture-1", "patient-1", new BigDecimal("5000"),
                "ORANGE_MONEY", "+2250100000001", new BigDecimal("5000"));

        assertNotNull(receipt);
        assertEquals("ORANGE_MONEY", receipt.get("modePaiement"));
        assertEquals("SUCCES", receipt.get("statut"));
    }

    @Test
    void verifierPaiementNonModifiable_shouldThrow409_whenPayee() {
        Paiement paiement = new Paiement();
        paiement.setId("paiement-1");
        paiement.setStatut("SUCCES");

        when(paiementRepository.findById("paiement-1")).thenReturn(Optional.of(paiement));

        BusinessException exception = assertThrows(BusinessException.class, () ->
                processPaymentUseCase.verifierPaiementNonModifiable("paiement-1"));

        assertEquals(409, exception.getStatus());
    }

    @Test
    void getPaiementsByFactureId_shouldReturnList() {
        when(paiementRepository.findByFactureId("facture-1"))
                .thenReturn(java.util.Collections.emptyList());

        var paiements = processPaymentUseCase.getPaiementsByFactureId("facture-1");

        assertNotNull(paiements);
        assertTrue(paiements.isEmpty());
    }
}
