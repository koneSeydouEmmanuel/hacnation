package com.hacnation.common.events;

public class PharmacyEvent extends BaseEvent {
    private String ordonnanceId;
    private String prescriptionId;
    private String patientId;
    private String medicamentId;
    private Integer quantite;
    private String statut;

    public PharmacyEvent() {}

    public String getOrdonnanceId() { return ordonnanceId; }
    public void setOrdonnanceId(String ordonnanceId) { this.ordonnanceId = ordonnanceId; }
    public String getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(String prescriptionId) { this.prescriptionId = prescriptionId; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getMedicamentId() { return medicamentId; }
    public void setMedicamentId(String medicamentId) { this.medicamentId = medicamentId; }
    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public static PharmacyEvent dispensed(String ordonnanceId, String patientId, String medicamentId, Integer quantite) {
        PharmacyEvent event = new PharmacyEvent();
        event.setEventType("DRUG_DISPENSED");
        event.setOrdonnanceId(ordonnanceId);
        event.setPatientId(patientId);
        event.setMedicamentId(medicamentId);
        event.setQuantite(quantite);
        return event;
    }
}
