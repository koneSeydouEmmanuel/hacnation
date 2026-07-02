CREATE TABLE IF NOT EXISTS laboratoire_db.demandes_analyse (
    id VARCHAR(36) PRIMARY KEY,
    prescription_id VARCHAR(36) NOT NULL,
    patient_id VARCHAR(36) NOT NULL,
    type_analyse VARCHAR(50),
    statut VARCHAR(20) DEFAULT 'EN_ATTENTE',
    resultats TEXT,
    laborantin_id VARCHAR(36),
    validateur_id VARCHAR(36),
    date_prelevement TIMESTAMP,
    date_resultat TIMESTAMP,
    date_validation TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
