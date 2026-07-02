package com.hacnation.common.dto;

import com.hacnation.common.enums.CanalNotification;
import java.time.LocalDateTime;

public class NotificationDto {
    private String id;
    private String patientId;
    private CanalNotification canal;
    private String contenu;
    private String statut;
    private LocalDateTime dateEnvoi;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public CanalNotification getCanal() { return canal; }
    public void setCanal(CanalNotification canal) { this.canal = canal; }
    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(LocalDateTime dateEnvoi) { this.dateEnvoi = dateEnvoi; }
}
