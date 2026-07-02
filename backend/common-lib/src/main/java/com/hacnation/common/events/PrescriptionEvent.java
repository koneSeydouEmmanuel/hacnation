package com.hacnation.common.events;

public class PrescriptionEvent extends BaseEvent {
    private String prescriptionId;
    private String consultationId;
    private String patientId;
    private String type;
    private String statut;

    public PrescriptionEvent() {}

    public String getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(String prescriptionId) { this.prescriptionId = prescriptionId; }
    public String getConsultationId() { return consultationId; }
    public void setConsultationId(String consultationId) { this.consultationId = consultationId; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public static PrescriptionEvent created(String prescriptionId, String consultationId, String patientId, String type) {
        PrescriptionEvent event = new PrescriptionEvent();
        event.setEventType("PRESCRIPTION_CREATED");
        event.setPrescriptionId(prescriptionId);
        event.setConsultationId(consultationId);
        event.setPatientId(patientId);
        event.setType(type);
        return event;
    }
}
