package com.hacnation.patientidentity.domain.port;

public interface PatientEventPublisherPort {

    void publishPatientCreated(String patientId, String nom, String prenom, String telephone);

    void publishPatientUpdated(String patientId, String nom, String prenom, String telephone);
}
