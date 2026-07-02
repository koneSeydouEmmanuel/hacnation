package com.hacnation.accueil.domain.model;

import com.hacnation.common.enums.TypeAdmission;
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
@Table(name = "admissions", schema = "accueil_db")
public class Admission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String patientId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeAdmission type;

    @Column(nullable = false)
    private String service;

    private String motif;

    private String statut;

    private LocalDateTime dateAdmission;

    @Column(nullable = false)
    private boolean modeDegrade = false;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public TypeAdmission getType() { return type; }
    public void setType(TypeAdmission type) { this.type = type; }
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public LocalDateTime getDateAdmission() { return dateAdmission; }
    public void setDateAdmission(LocalDateTime dateAdmission) { this.dateAdmission = dateAdmission; }
    public boolean isModeDegrade() { return modeDegrade; }
    public void setModeDegrade(boolean modeDegrade) { this.modeDegrade = modeDegrade; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
