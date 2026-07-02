package com.hacnation.common.dto;

import com.hacnation.common.enums.StatutRdv;
import java.time.LocalDateTime;

public class RdvDto {
    private String id;
    private String patientId;
    private String praticienId;
    private String service;
    private LocalDateTime dateHeure;
    private StatutRdv statut;
    private String qrCode;
    private String motif;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getPraticienId() { return praticienId; }
    public void setPraticienId(String praticienId) { this.praticienId = praticienId; }
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
    public LocalDateTime getDateHeure() { return dateHeure; }
    public void setDateHeure(LocalDateTime dateHeure) { this.dateHeure = dateHeure; }
    public StatutRdv getStatut() { return statut; }
    public void setStatut(StatutRdv statut) { this.statut = statut; }
    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
