package com.hacnation.pharmacie.domain.port;

import com.hacnation.pharmacie.domain.model.StockMedicament;
import java.time.LocalDate;
import java.util.List;

public interface StockRepositoryPort {

    List<StockMedicament> findAll();

    StockMedicament save(StockMedicament stock);

    List<StockMedicament> findByMedicamentIdOrderByDatePeremptionAsc(String medicamentId);

    List<StockMedicament> findByQuantiteLessThanEqual(Integer seuil);

    List<StockMedicament> findByDatePeremptionBefore(LocalDate date);
}
