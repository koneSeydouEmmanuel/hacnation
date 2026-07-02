CREATE SCHEMA IF NOT EXISTS soins_db;

CREATE TABLE IF NOT EXISTS soins_db.soins_infirmiers (
    id              VARCHAR(36)  NOT NULL PRIMARY KEY,
    hospitalisation_id VARCHAR(36) NOT NULL,
    patient_id      VARCHAR(36)  NOT NULL,
    type_soin       VARCHAR(255) NOT NULL,
    description     TEXT,
    frequence       VARCHAR(100),
    instructions    TEXT,
    statut          VARCHAR(50)  NOT NULL DEFAULT 'EN_ATTENTE',
    infirmier_id    VARCHAR(36),
    date_planifiee  TIMESTAMP,
    date_administration TIMESTAMP,
    motif_non_administration VARCHAR(500),
    created_at      TIMESTAMP
);
