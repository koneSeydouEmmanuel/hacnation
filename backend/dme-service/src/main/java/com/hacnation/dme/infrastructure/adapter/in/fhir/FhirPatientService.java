package com.hacnation.dme.infrastructure.adapter.in.fhir;

import com.hacnation.dme.domain.port.PatientReadPort;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FhirPatientService {

    private final PatientReadPort patientReadPort;
    private final PatientFhirMapper mapper;

    public FhirPatientService(PatientReadPort patientReadPort, PatientFhirMapper mapper) {
        this.patientReadPort = patientReadPort;
        this.mapper = mapper;
    }

    public Patient readPatient(String id) {
        return patientReadPort.findById(id)
                .map(mapper::toFhirPatient)
                .orElse(null);
    }

    public Bundle searchPatients(String name, String searchUrl) {
        List<com.hacnation.dme.domain.model.Patient> entities;
        if (name != null && !name.isEmpty()) {
            entities = patientReadPort.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(
                    name, name, PageRequest.of(0, 50)).getContent();
        } else {
            entities = patientReadPort.findAll(PageRequest.of(0, 50)).getContent();
        }
        return mapper.toFhirBundle(entities, searchUrl);
    }
}
