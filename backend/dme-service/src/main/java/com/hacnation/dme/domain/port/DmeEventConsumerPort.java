package com.hacnation.dme.domain.port;

public interface DmeEventConsumerPort {

    void handlePatientCreated(String patientId);

    void handleConsultationTerminated(String patientId, String consultationId, String medecinId, String statut);

    void handleLabResultAvailable(String patientId, String analyseId, String prescriptionId, String statut);

    void handleDrugDispensed(String patientId, String ordonnanceId, String prescriptionId, String medicamentId, Integer quantite, String statut);
}
