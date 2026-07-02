package com.hacnation.bloc.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "interventions_chirurgicales", schema = "bloc_db")
public class InterventionChirurgicale {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String patientId;

    @Column(nullable = false)
    private String typeIntervention;

    @Column(nullable = false)
    private String chirurgienId;

    private String anesthesisteId;

    @Column(nullable = false)
    private String salle;

    @Column(nullable = false)
    private LocalDateTime dateHeure;

    private Integer dureeEstimee;

    @Column(nullable = false)
    private String statut = "PROGRAMMEE";

    @Column(columnDefinition = "TEXT")
    private String notes;

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

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getTypeIntervention() {
        return typeIntervention;
    }

    public void setTypeIntervention(String typeIntervention) {
        this.typeIntervention = typeIntervention;
    }

    public String getChirurgienId() {
        return chirurgienId;
    }

    public void setChirurgienId(String chirurgienId) {
        this.chirurgienId = chirurgienId;
    }

    public String getAnesthesisteId() {
        return anesthesisteId;
    }

    public void setAnesthesisteId(String anesthesisteId) {
        this.anesthesisteId = anesthesisteId;
    }

    public String getSalle() {
        return salle;
    }

    public void setSalle(String salle) {
        this.salle = salle;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    public Integer getDureeEstimee() {
        return dureeEstimee;
    }

    public void setDureeEstimee(Integer dureeEstimee) {
        this.dureeEstimee = dureeEstimee;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
