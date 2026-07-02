package com.hacnation.common.events;

public class LabEvent extends BaseEvent {
    private String analyseId;
    private String prescriptionId;
    private String patientId;
    private String statut;

    public LabEvent() {}

    public String getAnalyseId() { return analyseId; }
    public void setAnalyseId(String analyseId) { this.analyseId = analyseId; }
    public String getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(String prescriptionId) { this.prescriptionId = prescriptionId; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public static LabEvent resultAvailable(String analyseId, String prescriptionId, String patientId) {
        LabEvent event = new LabEvent();
        event.setEventType("LAB_RESULT_AVAILABLE");
        event.setAnalyseId(analyseId);
        event.setPrescriptionId(prescriptionId);
        event.setPatientId(patientId);
        event.setStatut("TERMINE");
        return event;
    }
}
