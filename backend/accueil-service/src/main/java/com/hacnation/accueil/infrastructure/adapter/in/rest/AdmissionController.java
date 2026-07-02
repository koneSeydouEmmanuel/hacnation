package com.hacnation.accueil.infrastructure.adapter.in.rest;

import com.hacnation.common.enums.TypeAdmission;
import com.hacnation.accueil.application.service.RegisterAdmissionUseCase;
import com.hacnation.accueil.domain.model.Admission;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accueil")
public class AdmissionController {

    private final RegisterAdmissionUseCase registerAdmissionUseCase;

    public AdmissionController(RegisterAdmissionUseCase registerAdmissionUseCase) {
        this.registerAdmissionUseCase = registerAdmissionUseCase;
    }

    @PostMapping("/admissions")
    public ResponseEntity<Admission> createAdmission(@RequestBody Map<String, String> body) {
        String patientId = body.get("patientId");
        TypeAdmission type = TypeAdmission.valueOf(body.get("type"));
        String service = body.get("service");
        String motif = body.get("motif");

        Admission admission = registerAdmissionUseCase.createAdmission(patientId, type, service, motif);
        return ResponseEntity.ok(admission);
    }

    @GetMapping("/admissions")
    public ResponseEntity<List<Admission>> getAdmissionsByPatient(@RequestParam String patientId) {
        List<Admission> admissions = registerAdmissionUseCase.getAdmissionsByPatient(patientId);
        return ResponseEntity.ok(admissions);
    }

    @GetMapping("/admissions/{id}")
    public ResponseEntity<Admission> getAdmission(@PathVariable String id) {
        Admission admission = registerAdmissionUseCase.getAdmission(id);
        return ResponseEntity.ok(admission);
    }
}
