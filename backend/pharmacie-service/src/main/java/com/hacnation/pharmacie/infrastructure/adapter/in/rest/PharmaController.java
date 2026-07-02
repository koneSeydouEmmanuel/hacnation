package com.hacnation.pharmacie.infrastructure.adapter.in.rest;

import com.hacnation.pharmacie.application.service.DeliverMedicationUseCase;
import com.hacnation.pharmacie.application.service.ManageStockUseCase;
import com.hacnation.pharmacie.domain.model.Ordonnance;
import com.hacnation.pharmacie.domain.model.StockMedicament;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pharma")
public class PharmaController {

    private final DeliverMedicationUseCase deliverMedicationUseCase;
    private final ManageStockUseCase manageStockUseCase;

    public PharmaController(DeliverMedicationUseCase deliverMedicationUseCase,
                            ManageStockUseCase manageStockUseCase) {
        this.deliverMedicationUseCase = deliverMedicationUseCase;
        this.manageStockUseCase = manageStockUseCase;
    }

    @GetMapping("/ordonnances")
    public ResponseEntity<Page<Ordonnance>> getOrdonnances(
            @RequestParam(defaultValue = "EN_ATTENTE") String statut,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<Ordonnance> ordonnancesPage = deliverMedicationUseCase.getOrdonnancesEnAttente(pageable);
        return ResponseEntity.ok(ordonnancesPage);
    }

    @GetMapping("/ordonnances/{id}")
    public ResponseEntity<Ordonnance> getOrdonnanceById(@PathVariable String id) {
        Ordonnance ordonnance = deliverMedicationUseCase.getOrdonnanceById(id);
        return ResponseEntity.ok(ordonnance);
    }

    @PostMapping("/ordonnances/{id}/delivrer")
    public ResponseEntity<Ordonnance> delivrerOrdonnance(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {

        String pharmacienId = body.get("pharmacienId");

        if (pharmacienId == null) {
            return ResponseEntity.badRequest().build();
        }

        Ordonnance updated = deliverMedicationUseCase.delivrerOrdonnance(id, pharmacienId);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/stock")
    public ResponseEntity<List<StockMedicament>> getStock() {
        List<StockMedicament> stock = manageStockUseCase.getStock();
        return ResponseEntity.ok(stock);
    }

    @PostMapping("/stock")
    public ResponseEntity<StockMedicament> addStock(@RequestBody Map<String, Object> body) {
        String medicamentId = (String) body.get("medicamentId");
        String nom = (String) body.get("nom");
        String lot = (String) body.get("lot");
        Integer quantite = body.get("quantite") instanceof Integer
                ? (Integer) body.get("quantite")
                : ((Number) body.get("quantite")).intValue();
        String datePeremptionStr = (String) body.get("datePeremption");
        Integer seuilMin = body.get("seuilMin") != null
                ? (body.get("seuilMin") instanceof Integer
                    ? (Integer) body.get("seuilMin")
                    : ((Number) body.get("seuilMin")).intValue())
                : null;
        String emplacement = (String) body.get("emplacement");

        if (medicamentId == null || nom == null || lot == null || quantite == null || datePeremptionStr == null) {
            return ResponseEntity.badRequest().build();
        }

        LocalDate datePeremption = LocalDate.parse(datePeremptionStr);

        StockMedicament stock = manageStockUseCase.addStock(medicamentId, nom, lot, quantite,
                datePeremption, seuilMin, emplacement);
        return ResponseEntity.ok(stock);
    }
}
