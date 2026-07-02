package com.hacnation.pharmacie.infrastructure.adapter.out.jpa;

import com.hacnation.pharmacie.domain.model.StockMedicament;
import com.hacnation.pharmacie.domain.port.StockRepositoryPort;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockJpaRepository extends JpaRepository<StockMedicament, String>, StockRepositoryPort {

    List<StockMedicament> findByMedicamentIdOrderByDatePeremptionAsc(String medicamentId);

    List<StockMedicament> findByQuantiteLessThanEqual(Integer seuil);

    List<StockMedicament> findByDatePeremptionBefore(LocalDate date);
}
