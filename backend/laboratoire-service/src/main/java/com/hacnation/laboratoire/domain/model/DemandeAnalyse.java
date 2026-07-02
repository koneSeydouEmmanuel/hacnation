package com.hacnation.laboratoire.domain.model;

import com.hacnation.common.enums.StatutPrescription;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "demandes_analyse", schema = "laboratoire_db")
public class DemandeAnalyse {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String prescriptionId;

    @Column(nullable = false)
    private String patientId;

    private String typeAnalyse;

    @Enumerated(EnumType.STRING)
    private StatutPrescription statut = StatutPrescription.EN_ATTENTE;

    @Column(columnDefinition = "TEXT")
    private String resultats;

    private String laborantinId;

    private String validateurId;

    private String medecinPrescripteurId;

    @Column(columnDefinition = "TEXT")
    private String fichiers;

    private LocalDateTime datePrelevement;

    private LocalDateTime dateResultat;

    private LocalDateTime dateValidation;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getTypeAnalyse() {
        return typeAnalyse;
    }

    public void setTypeAnalyse(String typeAnalyse) {
        this.typeAnalyse = typeAnalyse;
    }

    public StatutPrescription getStatut() {
        return statut;
    }

    public void setStatut(StatutPrescription statut) {
        this.statut = statut;
    }

    public String getResultats() {
        return resultats;
    }

    public void setResultats(String resultats) {
        this.resultats = resultats;
    }

    public String getLaborantinId() {
        return laborantinId;
    }

    public void setLaborantinId(String laborantinId) {
        this.laborantinId = laborantinId;
    }

    public String getValidateurId() {
        return validateurId;
    }

    public void setValidateurId(String validateurId) {
        this.validateurId = validateurId;
    }

    public String getMedecinPrescripteurId() {
        return medecinPrescripteurId;
    }

    public void setMedecinPrescripteurId(String medecinPrescripteurId) {
        this.medecinPrescripteurId = medecinPrescripteurId;
    }

    public String getFichiers() {
        return fichiers;
    }

    public void setFichiers(String fichiers) {
        this.fichiers = fichiers;
    }

    public LocalDateTime getDatePrelevement() {
        return datePrelevement;
    }

    public void setDatePrelevement(LocalDateTime datePrelevement) {
        this.datePrelevement = datePrelevement;
    }

    public LocalDateTime getDateResultat() {
        return dateResultat;
    }

    public void setDateResultat(LocalDateTime dateResultat) {
        this.dateResultat = dateResultat;
    }

    public LocalDateTime getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(LocalDateTime dateValidation) {
        this.dateValidation = dateValidation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
