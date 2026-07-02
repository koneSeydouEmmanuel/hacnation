package com.hacnation.common.events;

import java.time.LocalDateTime;

public class ConsultationEvent extends BaseEvent {
    private String consultationId;
    private String patientId;
    private String medecinId;
    private String statut;
    private LocalDateTime date;

    public ConsultationEvent() {}

    public String getConsultationId() { return consultationId; }
    public void setConsultationId(String consultationId) { this.consultationId = consultationId; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getMedecinId() { return medecinId; }
    public void setMedecinId(String medecinId) { this.medecinId = medecinId; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public static ConsultationEvent created(String consultationId, String patientId, String medecinId) {
        ConsultationEvent event = new ConsultationEvent();
        event.setEventType("CONSULTATION_CREATED");
        event.setConsultationId(consultationId);
        event.setPatientId(patientId);
        event.setMedecinId(medecinId);
        return event;
    }

    public static ConsultationEvent terminated(String consultationId, String patientId) {
        ConsultationEvent event = new ConsultationEvent();
        event.setEventType("CONSULTATION_TERMINATED");
        event.setConsultationId(consultationId);
        event.setPatientId(patientId);
        event.setStatut("TERMINEE");
        return event;
    }
}
