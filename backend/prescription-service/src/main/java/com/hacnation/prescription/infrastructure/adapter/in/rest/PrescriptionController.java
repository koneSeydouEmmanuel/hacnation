package com.hacnation.prescription.infrastructure.adapter.in.rest;

import com.hacnation.common.dto.PrescriptionDto;
import com.hacnation.common.enums.StatutPrescription;
import com.hacnation.common.enums.TypePrescription;
import com.hacnation.common.exception.BusinessException;
import com.hacnation.prescription.application.service.CreatePrescriptionUseCase;
import java.util.List;
import java.util.Map;
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
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final CreatePrescriptionUseCase createPrescriptionUseCase;

    public PrescriptionController(CreatePrescriptionUseCase createPrescriptionUseCase) {
        this.createPrescriptionUseCase = createPrescriptionUseCase;
    }

    @PostMapping
    public ResponseEntity<PrescriptionDto> createPrescription(@RequestBody Map<String, Object> body) {
        String consultationId = (String) body.get("consultationId");
        String typeStr = (String) body.get("type");
        @SuppressWarnings("unchecked")
        Map<String, Object> details = (Map<String, Object>) body.get("details");
        String patientId = (String) body.get("patientId");

        TypePrescription type;
        try {
            type = TypePrescription.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Type de prescription invalide: " + typeStr);
        }

        PrescriptionDto dto = createPrescriptionUseCase.createPrescription(consultationId, type, details, patientId);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public ResponseEntity<List<PrescriptionDto>> getPrescriptions(
            @RequestParam(required = false) String consultationId) {
        if (consultationId == null) {
            return ResponseEntity.badRequest().build();
        }
        List<PrescriptionDto> prescriptions = createPrescriptionUseCase.getPrescriptionsByConsultation(consultationId);
        return ResponseEntity.ok(prescriptions);
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<PrescriptionDto> updateStatus(@PathVariable String id,
                                                         @RequestBody Map<String, String> body) {
        String statutStr = body.get("statut");
        StatutPrescription newStatut;
        try {
            newStatut = StatutPrescription.valueOf(statutStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Statut de prescription invalide: " + statutStr);
        }

        PrescriptionDto dto = createPrescriptionUseCase.updatePrescriptionStatus(id, newStatut);
        return ResponseEntity.ok(dto);
    }
}
