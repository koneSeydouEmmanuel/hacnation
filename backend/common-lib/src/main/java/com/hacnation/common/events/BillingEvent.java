package com.hacnation.common.events;

import java.math.BigDecimal;

public class BillingEvent extends BaseEvent {
    private String factureId;
    private String patientId;
    private BigDecimal montant;
    private String modePaiement;

    public BillingEvent() {}

    public String getFactureId() { return factureId; }
    public void setFactureId(String factureId) { this.factureId = factureId; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }
    public String getModePaiement() { return modePaiement; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }

    public static BillingEvent invoiceCreated(String factureId, String patientId, BigDecimal montant) {
        BillingEvent event = new BillingEvent();
        event.setEventType("INVOICE_CREATED");
        event.setFactureId(factureId);
        event.setPatientId(patientId);
        event.setMontant(montant);
        return event;
    }

    public static BillingEvent paymentReceived(String factureId, String patientId, BigDecimal montant, String modePaiement) {
        BillingEvent event = new BillingEvent();
        event.setEventType("PAYMENT_RECEIVED");
        event.setFactureId(factureId);
        event.setPatientId(patientId);
        event.setMontant(montant);
        event.setModePaiement(modePaiement);
        return event;
    }
}
