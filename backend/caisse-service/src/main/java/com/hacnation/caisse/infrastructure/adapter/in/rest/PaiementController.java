package com.hacnation.caisse.infrastructure.adapter.in.rest;

import com.hacnation.caisse.application.service.ProcessPaymentUseCase;
import com.hacnation.caisse.domain.model.Paiement;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/caisse")
public class PaiementController {

    private final ProcessPaymentUseCase processPaymentUseCase;

    public PaiementController(ProcessPaymentUseCase processPaymentUseCase) {
        this.processPaymentUseCase = processPaymentUseCase;
    }

    @PostMapping("/payer")
    public ResponseEntity<Map<String, Object>> payer(@RequestBody PaiementRequest request) {
        Map<String, Object> receipt = processPaymentUseCase.payer(
                request.getFactureId(),
                request.getPatientId(),
                request.getTotalFacture(),
                request.getModePaiement(),
                request.getTelephone(),
                request.getMontant());
        return ResponseEntity.ok(receipt);
    }

    @GetMapping(value = "/paiements", params = "factureId")
    public ResponseEntity<List<Paiement>> getPaiementsByFactureId(@RequestParam String factureId) {
        return ResponseEntity.ok(processPaymentUseCase.getPaiementsByFactureId(factureId));
    }

    @GetMapping(value = "/paiements", params = {"dateDebut", "dateFin"})
    public ResponseEntity<List<Paiement>> getPaiementsByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        return ResponseEntity.ok(processPaymentUseCase.getPaiementsByPeriode(dateDebut, dateFin));
    }

    @GetMapping("/resume")
    public ResponseEntity<Map<String, Object>> getResumeCaisse(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(processPaymentUseCase.getResumeCaisse(date));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerPaiement(@PathVariable String id) {
        processPaymentUseCase.supprimerPaiement(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifierPaiement(@PathVariable String id, @RequestBody PaiementRequest request) {
        processPaymentUseCase.verifierPaiementNonModifiable(id);
        processPaymentUseCase.payer(
                request.getFactureId(),
                request.getPatientId(),
                request.getTotalFacture(),
                request.getModePaiement(),
                request.getTelephone(),
                request.getMontant());
        return ResponseEntity.ok().build();
    }
}
