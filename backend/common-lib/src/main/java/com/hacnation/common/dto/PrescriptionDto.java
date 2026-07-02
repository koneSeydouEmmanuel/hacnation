package com.hacnation.common.dto;

import com.hacnation.common.enums.StatutPrescription;
import com.hacnation.common.enums.TypePrescription;
import java.time.LocalDateTime;
import java.util.Map;

public class PrescriptionDto {
    private String id;
    private String consultationId;
    private TypePrescription type;
    private Map<String, Object> details;
    private StatutPrescription statut;
    private LocalDateTime dateCreation;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getConsultationId() { return consultationId; }
    public void setConsultationId(String consultationId) { this.consultationId = consultationId; }
    public TypePrescription getType() { return type; }
    public void setType(TypePrescription type) { this.type = type; }
    public Map<String, Object> getDetails() { return details; }
    public void setDetails(Map<String, Object> details) { this.details = details; }
    public StatutPrescription getStatut() { return statut; }
    public void setStatut(StatutPrescription statut) { this.statut = statut; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}
