package com.hacnation.urgences.infrastructure.adapter.in.rest;

import com.hacnation.common.enums.NiveauTriage;
import com.hacnation.urgences.application.service.ProcessTriageUseCase;
import com.hacnation.urgences.domain.model.Triage;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/urgences")
public class UrgencesController {

    private final ProcessTriageUseCase processTriageUseCase;

    public UrgencesController(ProcessTriageUseCase processTriageUseCase) {
        this.processTriageUseCase = processTriageUseCase;
    }

    @PostMapping("/triage")
    public ResponseEntity<Triage> trier(@RequestBody Map<String, String> body) {
        String admissionId = body.get("admissionId");
        String patientId = body.get("patientId");
        NiveauTriage niveauGravite = NiveauTriage.valueOf(body.get("niveauGravite"));
        String constantes = body.get("constantes");
        String motif = body.get("motif");
        String orientation = body.get("orientation");

        Triage triage = processTriageUseCase.trier(admissionId, patientId, niveauGravite, constantes, motif, orientation);
        return ResponseEntity.ok(triage);
    }

    @GetMapping("/file")
    public ResponseEntity<Map<String, String>> getFileAttente() {
        return ResponseEntity.ok(Map.of("message", "File d'attente des urgences"));
    }
}
