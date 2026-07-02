CREATE SCHEMA IF NOT EXISTS notification_db;

CREATE TABLE IF NOT EXISTS notification_db.notifications (
    id              VARCHAR(36)  NOT NULL PRIMARY KEY,
    patient_id      VARCHAR(36)  NOT NULL,
    canal           VARCHAR(50)  NOT NULL,
    contenu         TEXT         NOT NULL,
    destinataire    VARCHAR(255) NOT NULL,
    statut          VARCHAR(50)  NOT NULL DEFAULT 'ENVOYE',
    date_envoi      TIMESTAMP    NOT NULL,
    created_at      TIMESTAMP
);
