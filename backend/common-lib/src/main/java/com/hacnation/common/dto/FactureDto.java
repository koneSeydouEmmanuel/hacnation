package com.hacnation.common.dto;

import com.hacnation.common.enums.StatutFacture;
import com.hacnation.common.enums.ModePaiement;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class FactureDto {
    private String id;
    private String patientId;
    private String consultationId;
    private List<LigneFactureDto> lignes;
    private BigDecimal total;
    private StatutFacture statut;
    private ModePaiement modePaiement;
    private LocalDateTime dateCreation;
    private LocalDateTime datePaiement;

    public static class LigneFactureDto {
        private String description;
        private Integer quantite;
        private BigDecimal prixUnitaire;
        private BigDecimal montant;

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Integer getQuantite() { return quantite; }
        public void setQuantite(Integer quantite) { this.quantite = quantite; }
        public BigDecimal getPrixUnitaire() { return prixUnitaire; }
        public void setPrixUnitaire(BigDecimal prixUnitaire) { this.prixUnitaire = prixUnitaire; }
        public BigDecimal getMontant() { return montant; }
        public void setMontant(BigDecimal montant) { this.montant = montant; }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getConsultationId() { return consultationId; }
    public void setConsultationId(String consultationId) { this.consultationId = consultationId; }
    public List<LigneFactureDto> getLignes() { return lignes; }
    public void setLignes(List<LigneFactureDto> lignes) { this.lignes = lignes; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public StatutFacture getStatut() { return statut; }
    public void setStatut(StatutFacture statut) { this.statut = statut; }
    public ModePaiement getModePaiement() { return modePaiement; }
    public void setModePaiement(ModePaiement modePaiement) { this.modePaiement = modePaiement; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public LocalDateTime getDatePaiement() { return datePaiement; }
    public void setDatePaiement(LocalDateTime datePaiement) { this.datePaiement = datePaiement; }
}
