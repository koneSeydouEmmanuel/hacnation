package com.hacnation.dme.domain.model;

import java.time.LocalDateTime;

public class DossierMedical {

    private String id;
    private String patientId;
    private String antecedents;
    private String notesCliniques;
    private String documents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public DossierMedical() {
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

    public String getAntecedents() {
        return antecedents;
    }

    public void setAntecedents(String antecedents) {
        this.antecedents = antecedents;
    }

    public String getNotesCliniques() {
        return notesCliniques;
    }

    public void setNotesCliniques(String notesCliniques) {
        this.notesCliniques = notesCliniques;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
