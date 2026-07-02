package com.hacnation.common.events;

import java.time.LocalDateTime;

public class RdvEvent extends BaseEvent {
    private String rdvId;
    private String patientId;
    private String service;
    private String statut;
    private LocalDateTime dateHeure;

    public RdvEvent() {}

    public String getRdvId() { return rdvId; }
    public void setRdvId(String rdvId) { this.rdvId = rdvId; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public LocalDateTime getDateHeure() { return dateHeure; }
    public void setDateHeure(LocalDateTime dateHeure) { this.dateHeure = dateHeure; }

    public static RdvEvent created(String rdvId, String patientId, String service, LocalDateTime dateHeure) {
        RdvEvent event = new RdvEvent();
        event.setEventType("RDV_CREATED");
        event.setRdvId(rdvId);
        event.setPatientId(patientId);
        event.setService(service);
        event.setDateHeure(dateHeure);
        return event;
    }

    public static RdvEvent statusChanged(String rdvId, String patientId, String statut) {
        RdvEvent event = new RdvEvent();
        event.setEventType("RDV_STATUS_CHANGED");
        event.setRdvId(rdvId);
        event.setPatientId(patientId);
        event.setStatut(statut);
        return event;
    }
}
