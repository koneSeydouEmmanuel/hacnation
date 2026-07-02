package com.hacnation.hospitalisation.infrastructure.adapter.in.rest;

import com.hacnation.hospitalisation.application.service.ManageHospitalisationUseCase;
import com.hacnation.hospitalisation.domain.model.Hospitalisation;
import com.hacnation.hospitalisation.domain.model.Lit;
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
@RequestMapping("/api/hospitalisation")
public class HospitalisationController {

    private final ManageHospitalisationUseCase manageHospitalisationUseCase;

    public HospitalisationController(ManageHospitalisationUseCase manageHospitalisationUseCase) {
        this.manageHospitalisationUseCase = manageHospitalisationUseCase;
    }

    @PostMapping("/hospitalisations")
    public ResponseEntity<Hospitalisation> admettre(@RequestBody Map<String, String> body) {
        String admissionId = body.get("admissionId");
        String patientId = body.get("patientId");
        String service = body.get("service");
        String motif = body.get("motif");

        Hospitalisation hospitalisation = manageHospitalisationUseCase.admettre(admissionId, patientId, service, motif);
        return ResponseEntity.ok(hospitalisation);
    }

    @PostMapping("/hospitalisations/{id}/sortie")
    public ResponseEntity<Hospitalisation> sortir(@PathVariable String id) {
        Hospitalisation hospitalisation = manageHospitalisationUseCase.sortir(id);
        return ResponseEntity.ok(hospitalisation);
    }

    @GetMapping("/lits")
    public ResponseEntity<List<Lit>> getLits(@RequestParam String service) {
        List<Lit> lits = manageHospitalisationUseCase.getLits(service);
        return ResponseEntity.ok(lits);
    }

    @GetMapping("/lits/disponibles")
    public ResponseEntity<Map<String, Object>> getLitsDisponibles(@RequestParam String service) {
        long count = manageHospitalisationUseCase.getLitsDisponibles(service);
        return ResponseEntity.ok(Map.of("service", service, "litsDisponibles", count));
    }
}
