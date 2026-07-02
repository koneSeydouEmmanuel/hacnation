ALTER TABLE laboratoire_db.demandes_analyse ADD COLUMN IF NOT EXISTS medecin_prescripteur_id VARCHAR(36);
ALTER TABLE laboratoire_db.demandes_analyse ADD COLUMN IF NOT EXISTS fichiers TEXT;
