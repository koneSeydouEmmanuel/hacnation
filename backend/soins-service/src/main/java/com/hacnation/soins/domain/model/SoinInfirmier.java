package com.hacnation.soins.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "soins_infirmiers", schema = "soins_db")
public class SoinInfirmier {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String hospitalisationId;

    @Column(nullable = false)
    private String patientId;

    @Column(nullable = false)
    private String typeSoin;

    private String description;

    private String frequence;

    private String instructions;

    @Column(nullable = false)
    private String statut = "EN_ATTENTE";

    private String infirmierId;

    private LocalDateTime datePlanifiee;

    private LocalDateTime dateAdministration;

    private String motifNonAdministration;

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

    public String getHospitalisationId() {
        return hospitalisationId;
    }

    public void setHospitalisationId(String hospitalisationId) {
        this.hospitalisationId = hospitalisationId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getTypeSoin() {
        return typeSoin;
    }

    public void setTypeSoin(String typeSoin) {
        this.typeSoin = typeSoin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrequence() {
        return frequence;
    }

    public void setFrequence(String frequence) {
        this.frequence = frequence;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getInfirmierId() {
        return infirmierId;
    }

    public void setInfirmierId(String infirmierId) {
        this.infirmierId = infirmierId;
    }

    public LocalDateTime getDatePlanifiee() {
        return datePlanifiee;
    }

    public void setDatePlanifiee(LocalDateTime datePlanifiee) {
        this.datePlanifiee = datePlanifiee;
    }

    public LocalDateTime getDateAdministration() {
        return dateAdministration;
    }

    public void setDateAdministration(LocalDateTime dateAdministration) {
        this.dateAdministration = dateAdministration;
    }

    public String getMotifNonAdministration() {
        return motifNonAdministration;
    }

    public void setMotifNonAdministration(String motifNonAdministration) {
        this.motifNonAdministration = motifNonAdministration;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
