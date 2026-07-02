package com.hacnation.reportingadmin.infrastructure.adapter.inbound;

import com.hacnation.reportingadmin.domain.port.inbound.CrmUseCase;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crm")
public class CrmController {

    private final CrmUseCase crmUseCase;

    public CrmController(CrmUseCase crmUseCase) {
        this.crmUseCase = crmUseCase;
    }

    @GetMapping("/patients/{id}/interactions")
    public ResponseEntity<List<Map<String, Object>>> getInteractions(@PathVariable String id) {
        return ResponseEntity.ok(crmUseCase.getInteractionHistorique(id));
    }

    @GetMapping("/patients/{id}/fidelite")
    public ResponseEntity<Map<String, Object>> getFidelite(@PathVariable String id) {
        return ResponseEntity.ok(crmUseCase.getFidelitePatient(id));
    }
}
