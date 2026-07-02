package com.hacnation.caisse.infrastructure.adapter.in.rest;

import java.math.BigDecimal;

public class PaiementRequest {

    private String factureId;
    private String patientId;
    private BigDecimal totalFacture;
    private String modePaiement;
    private String telephone;
    private BigDecimal montant;

    public PaiementRequest() {
    }

    public String getFactureId() { return factureId; }
    public void setFactureId(String factureId) { this.factureId = factureId; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public BigDecimal getTotalFacture() { return totalFacture; }
    public void setTotalFacture(BigDecimal totalFacture) { this.totalFacture = totalFacture; }
    public String getModePaiement() { return modePaiement; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }
}
