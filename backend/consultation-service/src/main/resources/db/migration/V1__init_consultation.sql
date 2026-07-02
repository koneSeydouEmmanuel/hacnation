CREATE TABLE IF NOT EXISTS consultation_db.consultations (
    id VARCHAR(36) PRIMARY KEY,
    patient_id VARCHAR(36) NOT NULL,
    rdv_id VARCHAR(36),
    medecin_id VARCHAR(36),
    constantes TEXT,
    diagnostic VARCHAR(500),
    compte_rendu TEXT,
    statut VARCHAR(20) DEFAULT 'EN_COURS',
    date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
