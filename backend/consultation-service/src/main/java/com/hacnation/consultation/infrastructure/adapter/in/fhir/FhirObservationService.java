package com.hacnation.consultation.infrastructure.adapter.in.fhir;

import com.hacnation.consultation.domain.model.Consultation;
import com.hacnation.consultation.domain.port.ConsultationRepositoryPort;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FhirObservationService {

    private final ConsultationRepositoryPort consultationRepository;
    private final FhirObservationMapper mapper;

    public FhirObservationService(ConsultationRepositoryPort consultationRepository, FhirObservationMapper mapper) {
        this.consultationRepository = consultationRepository;
        this.mapper = mapper;
    }

    public Bundle getObservationsForPatient(String patientId, String searchUrl) {
        List<Consultation> consultations = consultationRepository
                .findByPatientIdOrderByDateDesc(patientId, PageRequest.of(0, 100))
                .getContent();
        return mapper.toFhirBundle(consultations, searchUrl);
    }
}
