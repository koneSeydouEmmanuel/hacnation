package com.hacnation.laboratoire.infrastructure.adapter.in.fhir;

import com.hacnation.laboratoire.domain.model.DemandeAnalyse;
import com.hacnation.laboratoire.domain.port.DemandeAnalyseRepositoryPort;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FhirDiagnosticReportService {

    private final DemandeAnalyseRepositoryPort demandeAnalyseRepository;
    private final FhirDiagnosticReportMapper mapper;

    public FhirDiagnosticReportService(DemandeAnalyseRepositoryPort demandeAnalyseRepository,
                                        FhirDiagnosticReportMapper mapper) {
        this.demandeAnalyseRepository = demandeAnalyseRepository;
        this.mapper = mapper;
    }

    public DiagnosticReport readReport(String id) {
        return demandeAnalyseRepository.findById(id)
                .map(mapper::toFhirDiagnosticReport)
                .orElse(null);
    }

    public Bundle getReportsForPatient(String patientId, String searchUrl) {
        List<DemandeAnalyse> analyses = demandeAnalyseRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
        return mapper.toFhirBundle(analyses, searchUrl);
    }
}
