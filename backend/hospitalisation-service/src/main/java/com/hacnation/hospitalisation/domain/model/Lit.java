package com.hacnation.hospitalisation.domain.model;

import com.hacnation.common.enums.StatutLit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "lits", schema = "hospitalisation_db")
public class Lit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(nullable = false)
    private String service;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutLit statut = StatutLit.DISPONIBLE;

    private String patientId;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
    public StatutLit getStatut() { return statut; }
    public void setStatut(StatutLit statut) { this.statut = statut; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
}
