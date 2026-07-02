CREATE SCHEMA IF NOT EXISTS bloc_db;

CREATE TABLE IF NOT EXISTS bloc_db.interventions_chirurgicales (
    id                 VARCHAR(36)  NOT NULL PRIMARY KEY,
    patient_id         VARCHAR(36)  NOT NULL,
    type_intervention  VARCHAR(255) NOT NULL,
    chirurgien_id      VARCHAR(36)  NOT NULL,
    anesthesiste_id    VARCHAR(36),
    salle              VARCHAR(100) NOT NULL,
    date_heure         TIMESTAMP    NOT NULL,
    duree_estimee      INTEGER,
    statut             VARCHAR(50)  NOT NULL DEFAULT 'PROGRAMMEE',
    notes              TEXT,
    created_at         TIMESTAMP
);
