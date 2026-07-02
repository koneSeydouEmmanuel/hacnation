CREATE TABLE IF NOT EXISTS patient_identity_db.patients (
    id VARCHAR(36) PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    date_naissance DATE,
    sexe VARCHAR(10),
    telephone VARCHAR(20) UNIQUE,
    email VARCHAR(100),
    adresse VARCHAR(255),
    groupe_sanguin VARCHAR(5),
    statut VARCHAR(20) DEFAULT 'ACTIF',
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
