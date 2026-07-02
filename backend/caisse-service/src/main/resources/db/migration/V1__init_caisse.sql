CREATE SCHEMA IF NOT EXISTS caisse_db;

CREATE TABLE caisse_db.paiements (
    id VARCHAR(255) NOT NULL,
    facture_id VARCHAR(255) NOT NULL,
    montant NUMERIC(38,2) NOT NULL,
    mode_paiement VARCHAR(255),
    reference VARCHAR(255),
    telephone VARCHAR(255),
    date_paiement TIMESTAMP,
    statut VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE INDEX idx_paiements_facture_id ON caisse_db.paiements(facture_id);
CREATE INDEX idx_paiements_date ON caisse_db.paiements(date_paiement);
