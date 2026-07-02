CREATE TABLE IF NOT EXISTS dme_db.dossiers_medicaux (
    id VARCHAR(36) PRIMARY KEY,
    patient_id VARCHAR(36) NOT NULL UNIQUE,
    antecedents TEXT,
    notes_cliniques TEXT,
    documents TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
