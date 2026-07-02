package com.hacnation.pharmacie.unit;

import com.hacnation.pharmacie.application.service.ManageStockUseCase;
import com.hacnation.pharmacie.domain.model.StockMedicament;
import com.hacnation.pharmacie.domain.port.PharmacyEventPublisherPort;
import com.hacnation.pharmacie.domain.port.StockRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManageStockUseCaseTest {

    @Mock
    private StockRepositoryPort stockRepository;

    @Mock
    private PharmacyEventPublisherPort eventPublisher;

    @InjectMocks
    private ManageStockUseCase manageStockUseCase;

    @Test
    void getStock_shouldReturnAllStock() {
        when(stockRepository.findAll()).thenReturn(Collections.emptyList());

        List<StockMedicament> stock = manageStockUseCase.getStock();

        assertNotNull(stock);
        assertTrue(stock.isEmpty());
    }

    @Test
    void addStock_shouldSaveAndReturnStock() {
        StockMedicament savedStock = new StockMedicament();
        savedStock.setId("stock-1");
        savedStock.setMedicamentId("med-1");
        savedStock.setNom("Paracetamol");
        savedStock.setLot("LOT-001");
        savedStock.setQuantite(100);
        savedStock.setDatePeremption(LocalDate.of(2027, 12, 31));
        savedStock.setSeuilMin(10);

        when(stockRepository.save(any(StockMedicament.class))).thenReturn(savedStock);

        StockMedicament result = manageStockUseCase.addStock("med-1", "Paracetamol", "LOT-001",
                100, LocalDate.of(2027, 12, 31), 10, "Armoire A");

        assertNotNull(result);
        assertEquals("med-1", result.getMedicamentId());
        assertEquals("Paracetamol", result.getNom());
        assertEquals(100, result.getQuantite());
        verify(stockRepository).save(any(StockMedicament.class));
    }

    @Test
    void checkFefoExpiry_shouldPublishEventsForExpired() {
        StockMedicament expired = new StockMedicament();
        expired.setMedicamentId("med-1");
        expired.setNom("Paracetamol");
        expired.setLot("LOT-001");
        expired.setQuantite(100);
        expired.setDatePeremption(LocalDate.of(2025, 1, 1));

        when(stockRepository.findByDatePeremptionBefore(any(LocalDate.class)))
                .thenReturn(List.of(expired));
        when(stockRepository.findByQuantiteLessThanEqual(5))
                .thenReturn(Collections.emptyList());

        manageStockUseCase.checkFefoExpiry();

        verify(eventPublisher, times(1)).publishPharmacyEvent(any());
    }

    @Test
    void checkFefoExpiry_shouldWarnLowStock() {
        when(stockRepository.findByDatePeremptionBefore(any(LocalDate.class)))
                .thenReturn(Collections.emptyList());
        when(stockRepository.findByQuantiteLessThanEqual(5))
                .thenReturn(Collections.emptyList());

        manageStockUseCase.checkFefoExpiry();

        verify(eventPublisher, never()).publishPharmacyEvent(any());
    }
}
