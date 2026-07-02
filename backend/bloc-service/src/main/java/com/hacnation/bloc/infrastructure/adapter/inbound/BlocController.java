package com.hacnation.bloc.infrastructure.adapter.inbound;

import com.hacnation.bloc.domain.model.InterventionChirurgicale;
import com.hacnation.bloc.domain.port.inbound.BlocUseCase;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("/api/bloc")
public class BlocController {

    private final BlocUseCase blocUseCase;

    public BlocController(BlocUseCase blocUseCase) {
        this.blocUseCase = blocUseCase;
    }

    @PostMapping("/interventions")
    public ResponseEntity<InterventionChirurgicale> programmerIntervention(@RequestBody Map<String, Object> body) {
        String patientId = (String) body.get("patientId");
        String typeIntervention = (String) body.get("typeIntervention");
        String chirurgienId = (String) body.get("chirurgienId");
        String salle = (String) body.get("salle");
        LocalDateTime dateHeure = LocalDateTime.parse((String) body.get("dateHeure"));
        Integer dureeEstimee = body.get("dureeEstimee") != null
                ? ((Number) body.get("dureeEstimee")).intValue() : null;

        InterventionChirurgicale intervention = blocUseCase.programmerIntervention(
                patientId, typeIntervention, chirurgienId, salle, dateHeure, dureeEstimee);
        return ResponseEntity.ok(intervention);
    }

    @PutMapping("/interventions/{id}/statut")
    public ResponseEntity<InterventionChirurgicale> updateStatutIntervention(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        String statut = body.get("statut");
        InterventionChirurgicale intervention = blocUseCase.updateStatutIntervention(id, statut);
        return ResponseEntity.ok(intervention);
    }

    @GetMapping("/interventions")
    public ResponseEntity<List<InterventionChirurgicale>> getPlanningSalle(
            @RequestParam String salle,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<InterventionChirurgicale> interventions = blocUseCase.getPlanningSalle(salle, date);
        return ResponseEntity.ok(interventions);
    }
}
