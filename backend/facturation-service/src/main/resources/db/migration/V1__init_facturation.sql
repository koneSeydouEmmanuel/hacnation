CREATE SCHEMA IF NOT EXISTS facturation_db;

CREATE TABLE facturation_db.factures (
    id VARCHAR(255) NOT NULL,
    patient_id VARCHAR(255) NOT NULL,
    consultation_id VARCHAR(255),
    lignes TEXT,
    total NUMERIC(38,2) DEFAULT 0,
    statut VARCHAR(255) DEFAULT 'EN_ATTENTE',
    mode_paiement VARCHAR(255),
    date_creation TIMESTAMP,
    date_paiement TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE INDEX idx_factures_patient_id ON facturation_db.factures(patient_id);
CREATE INDEX idx_factures_statut ON facturation_db.factures(statut);
CREATE INDEX idx_factures_date_creation ON facturation_db.factures(date_creation);
