package com.hacnation.pharmacie.unit;

import com.hacnation.common.enums.StatutPrescription;
import com.hacnation.common.events.NotificationEvent;
import com.hacnation.common.events.PharmacyEvent;
import com.hacnation.common.exception.BusinessException;
import com.hacnation.common.exception.ResourceNotFoundException;
import com.hacnation.pharmacie.application.service.DeliverMedicationUseCase;
import com.hacnation.pharmacie.domain.model.Ordonnance;
import com.hacnation.pharmacie.domain.model.StockMedicament;
import com.hacnation.pharmacie.domain.port.OrdonnanceRepositoryPort;
import com.hacnation.pharmacie.domain.port.PharmacyEventPublisherPort;
import com.hacnation.pharmacie.domain.port.StockRepositoryPort;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliverMedicationUseCaseTest {

    @Mock
    private OrdonnanceRepositoryPort ordonnanceRepository;

    @Mock
    private StockRepositoryPort stockRepository;

    @Mock
    private PharmacyEventPublisherPort eventPublisher;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private DeliverMedicationUseCase deliverMedicationUseCase;

    @Test
    @DisplayName("Should deliver medication, decrement stock, and publish events")
    void delivrerOrdonnance_shouldDecrementStockAndPublishEvents() throws Exception {
        Ordonnance ordonnance = new Ordonnance();
        ordonnance.setId("ord-1");
        ordonnance.setPrescriptionId("presc-1");
        ordonnance.setPatientId("patient-1");
        ordonnance.setMedicaments("[{\"medicamentId\":\"med-1\",\"quantite\":5}]");
        ordonnance.setStatut(StatutPrescription.EN_ATTENTE);

        when(ordonnanceRepository.findById("ord-1")).thenReturn(Optional.of(ordonnance));

        StockMedicament stock = new StockMedicament();
        stock.setId("stock-1");
        stock.setMedicamentId("med-1");
        stock.setNom("Paracetamol");
        stock.setLot("LOT-001");
        stock.setQuantite(10);
        stock.setDatePeremption(LocalDate.of(2027, 12, 31));

        when(stockRepository.findByMedicamentIdOrderByDatePeremptionAsc("med-1"))
                .thenReturn(List.of(stock));
        when(stockRepository.save(any(StockMedicament.class))).thenReturn(stock);

        Ordonnance savedOrdonnance = new Ordonnance();
        savedOrdonnance.setId("ord-1");
        savedOrdonnance.setPatientId("patient-1");
        savedOrdonnance.setStatut(StatutPrescription.TERMINEE);

        when(ordonnanceRepository.save(any(Ordonnance.class))).thenReturn(savedOrdonnance);

        Ordonnance result = deliverMedicationUseCase.delivrerOrdonnance("ord-1", "pharm-1");

        assertNotNull(result);
        assertEquals(StatutPrescription.TERMINEE, result.getStatut());
        assertEquals(5, stock.getQuantite());
        verify(stockRepository).save(any(StockMedicament.class));
        verify(eventPublisher).publishPharmacyEvent(any(PharmacyEvent.class));
        verify(eventPublisher).publishNotificationEvent(any(NotificationEvent.class));
    }

    @Test
    @DisplayName("Should throw BusinessException when stock is insufficient")
    void delivrerOrdonnance_shouldThrowException_whenInsufficientStock() throws Exception {
        Ordonnance ordonnance = new Ordonnance();
        ordonnance.setId("ord-1");
        ordonnance.setPrescriptionId("presc-1");
        ordonnance.setPatientId("patient-1");
        ordonnance.setMedicaments("[{\"medicamentId\":\"med-1\",\"quantite\":15}]");
        ordonnance.setStatut(StatutPrescription.EN_ATTENTE);

        when(ordonnanceRepository.findById("ord-1")).thenReturn(Optional.of(ordonnance));

        StockMedicament stock = new StockMedicament();
        stock.setId("stock-1");
        stock.setMedicamentId("med-1");
        stock.setNom("Paracetamol");
        stock.setLot("LOT-001");
        stock.setQuantite(10);
        stock.setDatePeremption(LocalDate.of(2027, 12, 31));

        when(stockRepository.findByMedicamentIdOrderByDatePeremptionAsc("med-1"))
                .thenReturn(List.of(stock));

        BusinessException exception = assertThrows(BusinessException.class, () ->
                deliverMedicationUseCase.delivrerOrdonnance("ord-1", "pharm-1"));

        assertEquals(409, exception.getStatus());
        assertTrue(exception.getMessage().contains("Rupture de stock"));
        verify(ordonnanceRepository, never()).save(any(Ordonnance.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when ordonnance not found")
    void delivrerOrdonnance_shouldThrowException_whenNotFound() {
        when(ordonnanceRepository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                deliverMedicationUseCase.delivrerOrdonnance("nonexistent", "pharm-1"));

        verify(stockRepository, never()).save(any(StockMedicament.class));
        verify(eventPublisher, never()).publishPharmacyEvent(any());
    }

    @Test
    @DisplayName("Should return page of pending ordonnances")
    void getOrdonnancesEnAttente_shouldReturnPage() {
        Page<Ordonnance> emptyPage = new PageImpl<>(Collections.emptyList());
        when(ordonnanceRepository.findByStatutOrderByCreatedAtAsc(any(), any()))
                .thenReturn(emptyPage);

        Page<Ordonnance> result = deliverMedicationUseCase.getOrdonnancesEnAttente(PageRequest.of(0, 10));

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return ordonnance by id when found")
    void getOrdonnanceById_shouldReturnOrdonnance() {
        Ordonnance ordonnance = new Ordonnance();
        ordonnance.setId("ord-1");
        ordonnance.setPatientId("patient-1");

        when(ordonnanceRepository.findById("ord-1")).thenReturn(Optional.of(ordonnance));

        Ordonnance result = deliverMedicationUseCase.getOrdonnanceById("ord-1");

        assertNotNull(result);
        assertEquals("ord-1", result.getId());
    }
}
