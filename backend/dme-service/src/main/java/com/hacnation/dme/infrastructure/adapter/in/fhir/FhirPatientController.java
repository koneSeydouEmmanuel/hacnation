package com.hacnation.dme.infrastructure.adapter.in.fhir;

import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fhir")
public class FhirPatientController {

    private final FhirPatientService fhirPatientService;

    public FhirPatientController(FhirPatientService fhirPatientService) {
        this.fhirPatientService = fhirPatientService;
    }

    @GetMapping(value = "/Patient/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/fhir+json"})
    public ResponseEntity<Patient> readPatient(@PathVariable String id) {
        Patient patient = fhirPatientService.readPatient(id);
        if (patient == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(patient);
    }

    @GetMapping(value = "/Patient", produces = {MediaType.APPLICATION_JSON_VALUE, "application/fhir+json"})
    public ResponseEntity<Bundle> searchPatients(
            @RequestParam(required = false) String name,
            HttpServletRequest request) {
        Bundle bundle = fhirPatientService.searchPatients(name, request.getRequestURL().toString()
                + (request.getQueryString() != null ? "?" + request.getQueryString() : ""));
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
              {"type": "Patient", "interaction": [
                {"code": "read"}, {"code": "search-type"}
              ]}
            ]
          }]
        }
        """;
        return ResponseEntity.ok(capabilityStatement);
    }
}
