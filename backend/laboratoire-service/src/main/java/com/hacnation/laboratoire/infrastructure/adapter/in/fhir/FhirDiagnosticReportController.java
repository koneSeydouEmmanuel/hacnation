package com.hacnation.laboratoire.infrastructure.adapter.in.fhir;

import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fhir")
public class FhirDiagnosticReportController {

    private final FhirDiagnosticReportService fhirDiagnosticReportService;

    public FhirDiagnosticReportController(FhirDiagnosticReportService fhirDiagnosticReportService) {
        this.fhirDiagnosticReportService = fhirDiagnosticReportService;
    }

    @GetMapping(value = "/DiagnosticReport/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/fhir+json"})
    public ResponseEntity<DiagnosticReport> readReport(@PathVariable String id) {
        DiagnosticReport report = fhirDiagnosticReportService.readReport(id);
        if (report == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(report);
    }

    @GetMapping(value = "/DiagnosticReport", produces = {MediaType.APPLICATION_JSON_VALUE, "application/fhir+json"})
    public ResponseEntity<Bundle> searchReports(
            @RequestParam String subject,
            HttpServletRequest request) {
        String patientId = extractPatientId(subject);
        if (patientId == null) {
            return ResponseEntity.badRequest().build();
        }
        String searchUrl = request.getRequestURL().toString()
                + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        Bundle bundle = fhirDiagnosticReportService.getReportsForPatient(patientId, searchUrl);
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
              {"type": "DiagnosticReport", "interaction": [
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
