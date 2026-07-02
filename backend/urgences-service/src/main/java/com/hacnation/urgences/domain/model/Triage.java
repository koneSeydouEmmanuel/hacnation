package com.hacnation.urgences.domain.model;

import com.hacnation.common.enums.NiveauTriage;
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
@Table(name = "triages", schema = "urgences_db")
public class Triage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String admissionId;

    @Column(nullable = false)
    private String patientId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NiveauTriage niveauGravite;

    @Column(columnDefinition = "TEXT")
    private String constantes;

    private String motif;

    private String orientation;

    @Column(nullable = false)
    private LocalDateTime dateTriage;

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
    public NiveauTriage getNiveauGravite() { return niveauGravite; }
    public void setNiveauGravite(NiveauTriage niveauGravite) { this.niveauGravite = niveauGravite; }
    public String getConstantes() { return constantes; }
    public void setConstantes(String constantes) { this.constantes = constantes; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    public String getOrientation() { return orientation; }
    public void setOrientation(String orientation) { this.orientation = orientation; }
    public LocalDateTime getDateTriage() { return dateTriage; }
    public void setDateTriage(LocalDateTime dateTriage) { this.dateTriage = dateTriage; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
