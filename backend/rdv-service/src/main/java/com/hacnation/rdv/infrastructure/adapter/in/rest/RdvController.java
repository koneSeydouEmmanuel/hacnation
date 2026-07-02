package com.hacnation.rdv.infrastructure.adapter.in.rest;

import com.hacnation.common.dto.RdvDto;
import com.hacnation.rdv.application.service.CreateRdvUseCase;
import com.hacnation.rdv.application.service.GetCreneauxUseCase;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rdv")
public class RdvController {

    private final CreateRdvUseCase createRdvUseCase;
    private final GetCreneauxUseCase getCreneauxUseCase;

    public RdvController(CreateRdvUseCase createRdvUseCase,
                         GetCreneauxUseCase getCreneauxUseCase) {
        this.createRdvUseCase = createRdvUseCase;
        this.getCreneauxUseCase = getCreneauxUseCase;
    }

    @PostMapping
    public ResponseEntity<RdvDto> createRdv(@RequestBody Map<String, Object> body) {
        String patientId = (String) body.get("patientId");
        String praticienId = (String) body.get("praticienId");
        String service = (String) body.get("service");
        String dateHeureStr = (String) body.get("dateHeure");
        String motif = (String) body.get("motif");

        LocalDateTime dateHeure = LocalDateTime.parse(dateHeureStr);
        RdvDto rdv = createRdvUseCase.createRdv(patientId, praticienId, service, dateHeure, motif);
        return ResponseEntity.status(HttpStatus.CREATED).body(rdv);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RdvDto> getRdv(@PathVariable String id) {
        RdvDto rdv = createRdvUseCase.getRdv(id);
        return ResponseEntity.ok(rdv);
    }

    @GetMapping
    public ResponseEntity<List<RdvDto>> getRdvsByPatient(@RequestParam String patientId) {
        List<RdvDto> rdvs = createRdvUseCase.getRdvsByPatient(patientId);
        return ResponseEntity.ok(rdvs);
    }

    @GetMapping("/creneaux")
    public ResponseEntity<List<LocalTime>> getCreneaux(
            @RequestParam String service,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<LocalTime> creneaux = getCreneauxUseCase.execute(service, date);
        return ResponseEntity.ok(creneaux);
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<RdvDto> updateStatut(@PathVariable String id,
                                                @RequestBody Map<String, String> body) {
        String statut = body.get("statut");
        RdvDto rdv = createRdvUseCase.updateStatut(id, statut);
        return ResponseEntity.ok(rdv);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RdvDto> cancelRdv(@PathVariable String id) {
        RdvDto rdv = createRdvUseCase.cancelRdv(id);
        return ResponseEntity.ok(rdv);
    }

    @GetMapping("/{id}/qr")
    public ResponseEntity<Map<String, String>> getQrCode(@PathVariable String id) {
        RdvDto rdv = createRdvUseCase.getRdv(id);
        return ResponseEntity.ok(Map.of("qrCode", rdv.getQrCode() != null ? rdv.getQrCode() : ""));
    }
}
