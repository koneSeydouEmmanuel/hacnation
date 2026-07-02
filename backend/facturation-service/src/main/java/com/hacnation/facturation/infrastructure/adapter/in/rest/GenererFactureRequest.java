package com.hacnation.facturation.infrastructure.adapter.in.rest;

import java.math.BigDecimal;
import java.util.List;

public class GenererFactureRequest {

    private String patientId;
    private String consultationId;
    private List<ActeRequest> actes;

    public GenererFactureRequest() {
    }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getConsultationId() { return consultationId; }
    public void setConsultationId(String consultationId) { this.consultationId = consultationId; }
    public List<ActeRequest> getActes() { return actes; }
    public void setActes(List<ActeRequest> actes) { this.actes = actes; }

    public static class ActeRequest {
        private String description;
        private int quantite;
        private BigDecimal prixUnitaire;

        public ActeRequest() {
        }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public int getQuantite() { return quantite; }
        public void setQuantite(int quantite) { this.quantite = quantite; }
        public BigDecimal getPrixUnitaire() { return prixUnitaire; }
        public void setPrixUnitaire(BigDecimal prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    }
}
