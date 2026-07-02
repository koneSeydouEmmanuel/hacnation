package com.hacnation.common.events;

public class AdmissionEvent extends BaseEvent {
    private String admissionId;
    private String patientId;
    private String type;
    private String service;
    private String statut;

    public AdmissionEvent() {}

    public String getAdmissionId() { return admissionId; }
    public void setAdmissionId(String admissionId) { this.admissionId = admissionId; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public static AdmissionEvent created(String admissionId, String patientId, String type, String service) {
        AdmissionEvent event = new AdmissionEvent();
        event.setEventType("ADMISSION_CREATED");
        event.setAdmissionId(admissionId);
        event.setPatientId(patientId);
        event.setType(type);
        event.setService(service);
        return event;
    }
}
