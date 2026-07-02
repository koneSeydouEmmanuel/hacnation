CREATE SCHEMA IF NOT EXISTS reporting_admin_db;

CREATE TABLE IF NOT EXISTS reporting_admin_db.utilisateurs (
    id          VARCHAR(36)   NOT NULL PRIMARY KEY,
    username    VARCHAR(100)  NOT NULL UNIQUE,
    password    VARCHAR(255)  NOT NULL,
    roles       VARCHAR(500)  NOT NULL,
    nom         VARCHAR(100)  NOT NULL,
    prenom      VARCHAR(100)  NOT NULL,
    telephone   VARCHAR(20),
    email       VARCHAR(255),
    statut      VARCHAR(50)   NOT NULL DEFAULT 'ACTIF',
    created_at  TIMESTAMP
);

CREATE TABLE IF NOT EXISTS reporting_admin_db.services_medicaux (
    id          VARCHAR(36)   NOT NULL PRIMARY KEY,
    code        VARCHAR(50)   NOT NULL UNIQUE,
    libelle     VARCHAR(255)  NOT NULL,
    description TEXT,
    statut      VARCHAR(50)   NOT NULL DEFAULT 'ACTIF'
);

CREATE TABLE IF NOT EXISTS reporting_admin_db.actes_medicaux (
    id                VARCHAR(36)    NOT NULL PRIMARY KEY,
    code              VARCHAR(50)    NOT NULL UNIQUE,
    libelle           VARCHAR(255)   NOT NULL,
    type              VARCHAR(100)   NOT NULL,
    tarif_par_defaut  NUMERIC(12,2)  NOT NULL
);

CREATE TABLE IF NOT EXISTS reporting_admin_db.tarifs (
    id              VARCHAR(36)    NOT NULL PRIMARY KEY,
    acte_medical_id VARCHAR(36)    NOT NULL,
    service_id      VARCHAR(36)    NOT NULL,
    prix            NUMERIC(12,2)  NOT NULL,
    date_debut      DATE           NOT NULL,
    date_fin        DATE
);
