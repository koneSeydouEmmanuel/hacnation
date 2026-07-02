package com.hacnation.consultation.infrastructure.adapter.in.fhir;

import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fhir")
public class FhirObservationController {

    private final FhirObservationService fhirObservationService;

    public FhirObservationController(FhirObservationService fhirObservationService) {
        this.fhirObservationService = fhirObservationService;
    }

    @GetMapping(value = "/Observation", produces = {MediaType.APPLICATION_JSON_VALUE, "application/fhir+json"})
    public ResponseEntity<Bundle> searchObservations(
            @RequestParam String subject,
            HttpServletRequest request) {
        String patientId = extractPatientId(subject);
        if (patientId == null) {
            return ResponseEntity.badRequest().build();
        }
        String searchUrl = request.getRequestURL().toString()
                + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        Bundle bundle = fhirObservationService.getObservationsForPatient(patientId, searchUrl);
        return ResponseEntity.ok(bundle);
    }

    @GetMapping(value = "/metadata", produces = {MediaType.APPLICATION_JSON_VALUE, "application/fhir+json"})
    public ResponseEntity<String> metadata() {
        String capabilityStatement = """
        {
          "resourceType": "CapabilityStatement",
          "status": "active",
          "date": "2026-07-02",
          "publisher": "HACNATION",
          "kind": "instance",
          "software": {
            "name": "HACNATION FHIR Server",
            "version": "1.0.0"
          },
          "fhirVersion": "4.0.1",
          "format": ["json", "application/fhir+json"],
          "rest": [{
            "mode": "server",
            "resource": [
              {"type": "Observation", "interaction": [
                {"code": "read"}, {"code": "search-type"}
              ]}
            ]
          }]
        }
        """;
        return ResponseEntity.ok(capabilityStatement);
    }

    private String extractPatientId(String subject) {
        if (subject == null || subject.isBlank()) {
            return null;
        }
        if (subject.startsWith("Patient/")) {
            return subject.substring(8);
        }
        return subject;
    }
}
