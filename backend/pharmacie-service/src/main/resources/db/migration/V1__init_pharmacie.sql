CREATE TABLE IF NOT EXISTS pharmacie_db.ordonnances (
    id VARCHAR(36) PRIMARY KEY,
    prescription_id VARCHAR(36) NOT NULL,
    patient_id VARCHAR(36) NOT NULL,
    medicaments TEXT,
    statut VARCHAR(20) DEFAULT 'EN_ATTENTE',
    pharmacien_id VARCHAR(36),
    date_delivrance TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS pharmacie_db.stock_medicaments (
    id VARCHAR(36) PRIMARY KEY,
    medicament_id VARCHAR(36) NOT NULL,
    nom VARCHAR(200) NOT NULL,
    lot VARCHAR(50) NOT NULL,
    quantite INTEGER NOT NULL,
    date_peremption DATE NOT NULL,
    seuil_min INTEGER DEFAULT 10,
    emplacement VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
