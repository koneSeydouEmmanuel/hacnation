package com.hacnation.dme.infrastructure.adapter.in.rest;

import com.hacnation.dme.application.service.GetDmeUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/patients/{patientId}/dme")
public class DmeController {

    private final GetDmeUseCase getDmeUseCase;

    public DmeController(GetDmeUseCase getDmeUseCase) {
        this.getDmeUseCase = getDmeUseCase;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDme(@PathVariable String patientId) {
        Map<String, Object> dme = getDmeUseCase.execute(patientId);
        return ResponseEntity.ok(dme);
    }

    @PostMapping("/antecedents")
    public ResponseEntity<Map<String, Object>> addAntecedent(
            @PathVariable String patientId,
            @RequestBody Map<String, Object> antecedent) {
        Map<String, Object> dme = getDmeUseCase.addAntecedent(patientId, antecedent);
        return ResponseEntity.status(HttpStatus.CREATED).body(dme);
    }

    @PostMapping("/notes")
    public ResponseEntity<Map<String, Object>> addNoteClinique(
            @PathVariable String patientId,
            @RequestBody Map<String, Object> note) {
        Map<String, Object> dme = getDmeUseCase.addNoteClinique(patientId, note);
        return ResponseEntity.status(HttpStatus.CREATED).body(dme);
    }

    @PostMapping("/documents")
    public ResponseEntity<Map<String, Object>> addDocument(
            @PathVariable String patientId,
            @RequestBody Map<String, Object> document) {
        Map<String, Object> dme = getDmeUseCase.addDocument(patientId, document);
        return ResponseEntity.status(HttpStatus.CREATED).body(dme);
    }
}
