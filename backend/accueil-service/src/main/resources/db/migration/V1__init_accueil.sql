CREATE SCHEMA IF NOT EXISTS accueil_db;

CREATE TABLE accueil_db.admissions (
    id VARCHAR(255) NOT NULL,
    patient_id VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    service VARCHAR(255) NOT NULL,
    motif VARCHAR(255),
    statut VARCHAR(255),
    date_admission TIMESTAMP,
    mode_degrade BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE INDEX idx_admissions_patient_id ON accueil_db.admissions(patient_id);
CREATE INDEX idx_admissions_date ON accueil_db.admissions(date_admission);
