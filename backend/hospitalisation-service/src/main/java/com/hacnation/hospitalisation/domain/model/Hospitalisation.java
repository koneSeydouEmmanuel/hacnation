package com.hacnation.hospitalisation.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "hospitalisations", schema = "hospitalisation_db")
public class Hospitalisation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String admissionId;

    @Column(nullable = false)
    private String patientId;

    @Column(nullable = false)
    private String litId;

    @Column(nullable = false)
    private LocalDateTime dateEntree;

    private LocalDateTime dateSortie;

    private String motif;

    @Column(nullable = false)
    private String statut = "EN_COURS";

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getAdmissionId() { return admissionId; }
    public void setAdmissionId(String admissionId) { this.admissionId = admissionId; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getLitId() { return litId; }
    public void setLitId(String litId) { this.litId = litId; }
    public LocalDateTime getDateEntree() { return dateEntree; }
    public void setDateEntree(LocalDateTime dateEntree) { this.dateEntree = dateEntree; }
    public LocalDateTime getDateSortie() { return dateSortie; }
    public void setDateSortie(LocalDateTime dateSortie) { this.dateSortie = dateSortie; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
