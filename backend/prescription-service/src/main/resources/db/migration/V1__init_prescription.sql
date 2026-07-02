CREATE TABLE IF NOT EXISTS prescription_db.prescriptions (
    id VARCHAR(36) PRIMARY KEY,
    consultation_id VARCHAR(36) NOT NULL,
    type VARCHAR(20),
    details TEXT,
    statut VARCHAR(20) DEFAULT 'EN_ATTENTE',
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
