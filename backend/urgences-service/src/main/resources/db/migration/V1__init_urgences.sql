CREATE SCHEMA IF NOT EXISTS urgences_db;

CREATE TABLE urgences_db.triages (
    id VARCHAR(255) NOT NULL,
    admission_id VARCHAR(255) NOT NULL,
    patient_id VARCHAR(255) NOT NULL,
    niveau_gravite VARCHAR(255) NOT NULL,
    constantes TEXT,
    motif VARCHAR(255),
    orientation VARCHAR(255),
    date_triage TIMESTAMP NOT NULL,
    created_at TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE INDEX idx_triages_admission ON urgences_db.triages(admission_id);
CREATE INDEX idx_triages_patient ON urgences_db.triages(patient_id);
CREATE INDEX idx_triages_gravite ON urgences_db.triages(niveau_gravite);
