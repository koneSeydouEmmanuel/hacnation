package com.hacnation.common.events;

public class QueueEvent extends BaseEvent {
    private String patientId;
    private String rdvId;
    private String service;
    private Integer position;
    private String statut;

    public QueueEvent() {}

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getRdvId() { return rdvId; }
    public void setRdvId(String rdvId) { this.rdvId = rdvId; }
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public static QueueEvent checkedIn(String patientId, String rdvId, String service, Integer position) {
        QueueEvent event = new QueueEvent();
        event.setEventType("PATIENT_CHECKED_IN");
        event.setPatientId(patientId);
        event.setRdvId(rdvId);
        event.setService(service);
        event.setPosition(position);
        event.setStatut("EN_ATTENTE");
        return event;
    }

    public static QueueEvent called(String patientId, String service) {
        QueueEvent event = new QueueEvent();
        event.setEventType("PATIENT_CALLED");
        event.setPatientId(patientId);
        event.setService(service);
        event.setStatut("APPELE");
        return event;
    }
}
