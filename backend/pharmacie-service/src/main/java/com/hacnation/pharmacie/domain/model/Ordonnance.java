package com.hacnation.pharmacie.domain.model;

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
@Table(name = "ordonnances", schema = "pharmacie_db")
public class Ordonnance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String prescriptionId;

    @Column(nullable = false)
    private String patientId;

    @Column(columnDefinition = "TEXT")
    private String medicaments;

    @Enumerated(EnumType.STRING)
    private StatutPrescription statut = StatutPrescription.EN_ATTENTE;

    private String pharmacienId;

    private LocalDateTime dateDelivrance;

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

    public String getMedicaments() {
        return medicaments;
    }

    public void setMedicaments(String medicaments) {
        this.medicaments = medicaments;
    }

    public StatutPrescription getStatut() {
        return statut;
    }

    public void setStatut(StatutPrescription statut) {
        this.statut = statut;
    }

    public String getPharmacienId() {
        return pharmacienId;
    }

    public void setPharmacienId(String pharmacienId) {
        this.pharmacienId = pharmacienId;
    }

    public LocalDateTime getDateDelivrance() {
        return dateDelivrance;
    }

    public void setDateDelivrance(LocalDateTime dateDelivrance) {
        this.dateDelivrance = dateDelivrance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
