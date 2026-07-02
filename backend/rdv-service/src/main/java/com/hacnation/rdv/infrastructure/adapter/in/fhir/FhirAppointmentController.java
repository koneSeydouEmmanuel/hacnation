package com.hacnation.rdv.infrastructure.adapter.in.fhir;

import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fhir")
public class FhirAppointmentController {

    private final FhirAppointmentService fhirAppointmentService;

    public FhirAppointmentController(FhirAppointmentService fhirAppointmentService) {
        this.fhirAppointmentService = fhirAppointmentService;
    }

    @GetMapping(value = "/Appointment/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/fhir+json"})
    public ResponseEntity<Appointment> readAppointment(@PathVariable String id) {
        Appointment appointment = fhirAppointmentService.readAppointment(id);
        if (appointment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(appointment);
    }

    @GetMapping(value = "/Appointment", produces = {MediaType.APPLICATION_JSON_VALUE, "application/fhir+json"})
    public ResponseEntity<Bundle> searchAppointments(
            @RequestParam String patient,
            HttpServletRequest request) {
        String searchUrl = request.getRequestURL().toString()
                + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        Bundle bundle = fhirAppointmentService.searchAppointments(patient, searchUrl);
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
              {"type": "Appointment", "interaction": [
                {"code": "read"}, {"code": "search-type"}
              ]}
            ]
          }]
        }
        """;
        return ResponseEntity.ok(capabilityStatement);
    }
}
