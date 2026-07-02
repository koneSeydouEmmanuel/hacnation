CREATE SCHEMA IF NOT EXISTS hospitalisation_db;

CREATE TABLE hospitalisation_db.lits (
    id VARCHAR(255) NOT NULL,
    numero VARCHAR(255) NOT NULL UNIQUE,
    service VARCHAR(255) NOT NULL,
    statut VARCHAR(255) NOT NULL DEFAULT 'DISPONIBLE',
    patient_id VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE hospitalisation_db.hospitalisations (
    id VARCHAR(255) NOT NULL,
    admission_id VARCHAR(255) NOT NULL,
    patient_id VARCHAR(255) NOT NULL,
    lit_id VARCHAR(255) NOT NULL,
    date_entree TIMESTAMP NOT NULL,
    date_sortie TIMESTAMP,
    motif VARCHAR(255),
    statut VARCHAR(255) NOT NULL DEFAULT 'EN_COURS',
    created_at TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE INDEX idx_hosp_patient ON hospitalisation_db.hospitalisations(patient_id);
CREATE INDEX idx_hosp_lit ON hospitalisation_db.hospitalisations(lit_id);
CREATE INDEX idx_hosp_statut ON hospitalisation_db.hospitalisations(statut);
CREATE INDEX idx_lits_service ON hospitalisation_db.lits(service);
CREATE INDEX idx_lits_statut ON hospitalisation_db.lits(statut);
