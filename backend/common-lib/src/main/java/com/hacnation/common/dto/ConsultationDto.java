package com.hacnation.common.dto;

import com.hacnation.common.enums.StatutConsultation;
import java.time.LocalDateTime;
import java.util.Map;

public class ConsultationDto {
    private String id;
    private String patientId;
    private String rdvId;
    private String medecinId;
    private Map<String, String> constantes;
    private String diagnostic;
    private String compteRendu;
    private StatutConsultation statut;
    private LocalDateTime date;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getRdvId() { return rdvId; }
    public void setRdvId(String rdvId) { this.rdvId = rdvId; }
    public String getMedecinId() { return medecinId; }
    public void setMedecinId(String medecinId) { this.medecinId = medecinId; }
    public Map<String, String> getConstantes() { return constantes; }
    public void setConstantes(Map<String, String> constantes) { this.constantes = constantes; }
    public String getDiagnostic() { return diagnostic; }
    public void setDiagnostic(String diagnostic) { this.diagnostic = diagnostic; }
    public String getCompteRendu() { return compteRendu; }
    public void setCompteRendu(String compteRendu) { this.compteRendu = compteRendu; }
    public StatutConsultation getStatut() { return statut; }
    public void setStatut(StatutConsultation statut) { this.statut = statut; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}
