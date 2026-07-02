package com.hacnation.fileattente.infrastructure.adapter.out.jpa;

import com.hacnation.common.enums.StatutFileAttente;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "file_attente", schema = "file_attente_db")
public class FileAttenteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String patientId;

    private String rdvId;

    private String patientNom;

    private String patientPrenom;

    private String service;

    private Integer position;

    @Enumerated(EnumType.STRING)
    private StatutFileAttente statut = StatutFileAttente.EN_ATTENTE;

    private LocalDateTime heureArrivee;

    private Integer tempsEstime;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.heureArrivee == null) {
            this.heureArrivee = LocalDateTime.now();
        }
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

    public String getRdvId() {
        return rdvId;
    }

    public void setRdvId(String rdvId) {
        this.rdvId = rdvId;
    }

    public String getPatientNom() {
        return patientNom;
    }

    public void setPatientNom(String patientNom) {
        this.patientNom = patientNom;
    }

    public String getPatientPrenom() {
        return patientPrenom;
    }

    public void setPatientPrenom(String patientPrenom) {
        this.patientPrenom = patientPrenom;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public StatutFileAttente getStatut() {
        return statut;
    }

    public void setStatut(StatutFileAttente statut) {
        this.statut = statut;
    }

    public LocalDateTime getHeureArrivee() {
        return heureArrivee;
    }

    public void setHeureArrivee(LocalDateTime heureArrivee) {
        this.heureArrivee = heureArrivee;
    }

    public Integer getTempsEstime() {
        return tempsEstime;
    }

    public void setTempsEstime(Integer tempsEstime) {
        this.tempsEstime = tempsEstime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
