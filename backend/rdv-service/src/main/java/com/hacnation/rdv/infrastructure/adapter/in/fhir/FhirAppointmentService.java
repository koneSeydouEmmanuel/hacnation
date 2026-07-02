package com.hacnation.rdv.infrastructure.adapter.in.fhir;

import com.hacnation.rdv.domain.model.RendezVous;
import com.hacnation.rdv.domain.port.RdvRepositoryPort;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FhirAppointmentService {

    private final RdvRepositoryPort rendezVousRepository;
    private final FhirAppointmentMapper mapper;

    public FhirAppointmentService(RdvRepositoryPort rendezVousRepository, FhirAppointmentMapper mapper) {
        this.rendezVousRepository = rendezVousRepository;
        this.mapper = mapper;
    }

    public Appointment readAppointment(String id) {
        return rendezVousRepository.findById(id)
                .map(mapper::toFhirAppointment)
                .orElse(null);
    }

    public Bundle searchAppointments(String patientId, String searchUrl) {
        List<RendezVous> rendezVousList = rendezVousRepository.findByPatientIdOrderByDateHeureDesc(patientId);
        return mapper.toFhirBundle(rendezVousList, searchUrl);
    }
}
