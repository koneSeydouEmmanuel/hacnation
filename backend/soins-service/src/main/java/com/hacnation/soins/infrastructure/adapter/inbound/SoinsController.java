package com.hacnation.soins.infrastructure.adapter.inbound;

import com.hacnation.soins.domain.model.SoinInfirmier;
import com.hacnation.soins.domain.port.inbound.SoinsUseCase;
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
@RequestMapping("/api/soins")
public class SoinsController {

    private final SoinsUseCase soinsUseCase;

    public SoinsController(SoinsUseCase soinsUseCase) {
        this.soinsUseCase = soinsUseCase;
    }

    @PostMapping
    public ResponseEntity<SoinInfirmier> creerPlanSoins(@RequestBody Map<String, String> body) {
        String hospitalisationId = body.get("hospitalisationId");
        String patientId = body.get("patientId");
        String typeSoin = body.get("typeSoin");
        String description = body.get("description");
        String frequence = body.get("frequence");
        String instructions = body.get("instructions");

        SoinInfirmier soin = soinsUseCase.creerPlanSoins(
                hospitalisationId, patientId, typeSoin, description, frequence, instructions);
        return ResponseEntity.ok(soin);
    }

    @PostMapping("/{id}/administrer")
    public ResponseEntity<SoinInfirmier> administrerSoin(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        String infirmierId = body.get("infirmierId");
        SoinInfirmier soin = soinsUseCase.administrerSoin(id, infirmierId);
        return ResponseEntity.ok(soin);
    }

    @PostMapping("/{id}/non-administre")
    public ResponseEntity<SoinInfirmier> nonAdministre(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        String motif = body.get("motif");
        SoinInfirmier soin = soinsUseCase.nonAdministre(id, motif);
        return ResponseEntity.ok(soin);
    }

    @GetMapping
    public ResponseEntity<List<SoinInfirmier>> getSoinsPatient(@RequestParam String patientId) {
        List<SoinInfirmier> soins = soinsUseCase.getSoinsPatient(patientId);
        return ResponseEntity.ok(soins);
    }
}
