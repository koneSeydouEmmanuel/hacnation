package com.hacnation.facturation.infrastructure.adapter.in.rest;

import com.hacnation.facturation.application.service.GenerateInvoiceUseCase;
import com.hacnation.facturation.application.service.GenerateInvoiceUseCase.FactureDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/facturation")
public class FacturationController {

    private final GenerateInvoiceUseCase generateInvoiceUseCase;

    public FacturationController(GenerateInvoiceUseCase generateInvoiceUseCase) {
        this.generateInvoiceUseCase = generateInvoiceUseCase;
    }

    @PostMapping("/generer")
    public ResponseEntity<FactureDto> genererFacture(@RequestBody GenererFactureRequest request) {
        List<Map<String, Object>> actes = request.getActes().stream()
                .map(acte -> {
                    Map<String, Object> map = new java.util.LinkedHashMap<>();
                    map.put("description", acte.getDescription());
                    map.put("quantite", acte.getQuantite());
                    map.put("prixUnitaire", acte.getPrixUnitaire());
                    return map;
                })
                .collect(Collectors.toList());

        FactureDto facture = generateInvoiceUseCase.genererFacture(request.getConsultationId(),
                request.getPatientId(), actes);
        return ResponseEntity.ok(facture);
    }

    @GetMapping("/factures/{id}")
    public ResponseEntity<FactureDto> getFacture(@PathVariable String id) {
        return ResponseEntity.ok(generateInvoiceUseCase.getFacture(id));
    }

    @GetMapping(value = "/factures", params = "patientId")
    public ResponseEntity<Page<FactureDto>> getFacturesByPatient(@RequestParam String patientId, Pageable pageable) {
        return ResponseEntity.ok(generateInvoiceUseCase.getFacturesByPatient(patientId, pageable));
    }

    @GetMapping(value = "/factures", params = {"dateDebut", "dateFin"})
    public ResponseEntity<List<FactureDto>> getFacturesByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        return ResponseEntity.ok(generateInvoiceUseCase.getFacturesByPeriode(dateDebut, dateFin));
    }
}
