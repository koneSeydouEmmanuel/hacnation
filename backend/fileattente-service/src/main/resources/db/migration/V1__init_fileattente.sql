CREATE TABLE IF NOT EXISTS file_attente_db.file_attente (
    id VARCHAR(36) PRIMARY KEY,
    patient_id VARCHAR(36) NOT NULL,
    rdv_id VARCHAR(36),
    patient_nom VARCHAR(100),
    patient_prenom VARCHAR(100),
    service VARCHAR(100),
    position INTEGER,
    statut VARCHAR(20) DEFAULT 'EN_ATTENTE',
    heure_arrivee TIMESTAMP,
    temps_estime INTEGER,
    created_at TIMESTAMP
);
