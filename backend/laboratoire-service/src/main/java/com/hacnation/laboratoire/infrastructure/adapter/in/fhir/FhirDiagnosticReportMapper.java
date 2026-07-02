package com.hacnation.laboratoire.infrastructure.adapter.in.fhir;

import com.hacnation.common.enums.StatutPrescription;
import com.hacnation.laboratoire.domain.model.DemandeAnalyse;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
public class FhirDiagnosticReportMapper {

    public DiagnosticReport toFhirDiagnosticReport(DemandeAnalyse entity) {
        DiagnosticReport report = new DiagnosticReport();
        report.setId(entity.getId());

        report.setStatus(mapStatut(entity.getStatut()));

        if (entity.getTypeAnalyse() != null) {
            CodeableConcept code = new CodeableConcept();
            code.setText(entity.getTypeAnalyse());
            report.setCode(code);
        }

        report.setSubject(new Reference("Patient/" + entity.getPatientId()));

        if (entity.getValidateurId() != null) {
            Reference validatorRef = new Reference("Practitioner/" + entity.getValidateurId());
            report.addPerformer(validatorRef);
        }

        if (entity.getDateResultat() != null) {
            report.setIssued(Date.from(entity.getDateResultat().atZone(ZoneId.systemDefault()).toInstant()));
        } else {
            report.setIssued(Date.from(entity.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));
        }

        if (entity.getDatePrelevement() != null) {
            report.setEffective(new DateTimeType(
                    Date.from(entity.getDatePrelevement().atZone(ZoneId.systemDefault()).toInstant())));
        }

        if (entity.getResultats() != null) {
            report.setConclusion(entity.getResultats());
        }

        if (entity.getLaborantinId() != null) {
            Reference performerRef = new Reference("Practitioner/" + entity.getLaborantinId());
            report.addPerformer(performerRef);
        }

        return report;
    }

    public Bundle toFhirBundle(List<DemandeAnalyse> entities, String searchUrl) {
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.SEARCHSET);
        bundle.setTotal(entities.size());

        Bundle.BundleLinkComponent selfLink = bundle.addLink();
        selfLink.setRelation("self");
        selfLink.setUrl(searchUrl);

        for (DemandeAnalyse entity : entities) {
            bundle.addEntry()
                    .setResource(toFhirDiagnosticReport(entity))
                    .setFullUrl("DiagnosticReport/" + entity.getId());
        }

        return bundle;
    }

    private DiagnosticReport.DiagnosticReportStatus mapStatut(StatutPrescription statut) {
        if (statut == null) {
            return DiagnosticReport.DiagnosticReportStatus.UNKNOWN;
        }
        switch (statut) {
            case EN_ATTENTE:
                return DiagnosticReport.DiagnosticReportStatus.REGISTERED;
            case EN_COURS:
                return DiagnosticReport.DiagnosticReportStatus.PARTIAL;
            case TERMINEE:
                return DiagnosticReport.DiagnosticReportStatus.FINAL;
            case ANNULEE:
                return DiagnosticReport.DiagnosticReportStatus.CANCELLED;
            default:
                return DiagnosticReport.DiagnosticReportStatus.UNKNOWN;
        }
    }
}
