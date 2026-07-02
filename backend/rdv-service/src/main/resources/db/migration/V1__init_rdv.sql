CREATE TABLE IF NOT EXISTS rdv_db.rendez_vous (
    id VARCHAR(36) PRIMARY KEY,
    patient_id VARCHAR(36) NOT NULL,
    praticien_id VARCHAR(36),
    service VARCHAR(100),
    date_heure TIMESTAMP NOT NULL,
    statut VARCHAR(20) DEFAULT 'EN_ATTENTE',
    qr_code TEXT,
    motif VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
