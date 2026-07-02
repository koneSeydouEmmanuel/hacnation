package com.hacnation.common.events;

public class PatientEvent extends BaseEvent {
    private String patientId;
    private String nom;
    private String prenom;
    private String telephone;
    private String payload;

    public PatientEvent() {}

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }

    public static PatientEvent created(String patientId, String nom, String prenom, String telephone) {
        PatientEvent event = new PatientEvent();
        event.setEventType("PATIENT_CREATED");
        event.setPatientId(patientId);
        event.setNom(nom);
        event.setPrenom(prenom);
        event.setTelephone(telephone);
        return event;
    }

    public static PatientEvent updated(String patientId, String nom, String prenom, String telephone) {
        PatientEvent event = new PatientEvent();
        event.setEventType("PATIENT_UPDATED");
        event.setPatientId(patientId);
        event.setNom(nom);
        event.setPrenom(prenom);
        event.setTelephone(telephone);
        return event;
    }
}
