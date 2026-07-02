package com.hacnation.consultation.infrastructure.adapter.in.rest;

import com.hacnation.common.dto.ConsultationDto;
import com.hacnation.consultation.application.service.CreateConsultationUseCase;
import com.hacnation.consultation.application.service.TerminateConsultationUseCase;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    private final CreateConsultationUseCase createConsultationUseCase;
    private final TerminateConsultationUseCase terminateConsultationUseCase;

    public ConsultationController(CreateConsultationUseCase createConsultationUseCase,
                                  TerminateConsultationUseCase terminateConsultationUseCase) {
        this.createConsultationUseCase = createConsultationUseCase;
        this.terminateConsultationUseCase = terminateConsultationUseCase;
    }

    @PostMapping
    public ResponseEntity<ConsultationDto> createConsultation(@RequestBody Map<String, Object> body) {
        String patientId = (String) body.get("patientId");
        String rdvId = (String) body.get("rdvId");
        String medecinId = (String) body.get("medecinId");
        @SuppressWarnings("unchecked")
        Map<String, String> constantes = (Map<String, String>) body.get("constantes");
        String motif = (String) body.get("motif");

        ConsultationDto dto = createConsultationUseCase.createConsultation(patientId, rdvId, medecinId, constantes, motif);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultationDto> getConsultation(@PathVariable String id) {
        ConsultationDto dto = createConsultationUseCase.getConsultation(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<ConsultationDto>> getConsultations(
            @RequestParam(required = false) String patientId,
            @RequestParam(required = false) String medecinId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ConsultationDto> result;
        if (patientId != null) {
            result = createConsultationUseCase.getConsultationsByPatient(patientId, pageable);
        } else if (medecinId != null) {
            result = createConsultationUseCase.getConsultationsByMedecin(medecinId, pageable);
        } else {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultationDto> updateConsultation(@PathVariable String id,
                                                               @RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        Map<String, String> constantes = (Map<String, String>) body.get("constantes");
        String diagnostic = (String) body.get("diagnostic");
        String compteRendu = (String) body.get("compteRendu");

        ConsultationDto dto = createConsultationUseCase.updateConsultation(id, constantes, diagnostic, compteRendu);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/terminer")
    public ResponseEntity<ConsultationDto> terminerConsultation(@PathVariable String id) {
        ConsultationDto dto = terminateConsultationUseCase.terminerConsultation(id);
        return ResponseEntity.ok(dto);
    }
}
