CREATE TABLE IF NOT EXISTS reporting_admin_db.parametrage_systeme (
    id          VARCHAR(36)   NOT NULL PRIMARY KEY,
    cle         VARCHAR(100)  NOT NULL UNIQUE,
    valeur      VARCHAR(500)  NOT NULL,
    description TEXT,
    modifiable  BOOLEAN       NOT NULL DEFAULT TRUE
);

INSERT INTO reporting_admin_db.parametrage_systeme (id, cle, valeur, description, modifiable)
VALUES
    (gen_random_uuid(), 'HORAIRES_OUVERTURE', '08:00-18:00', 'Horaires d''ouverture de l''etablissement', TRUE),
    (gen_random_uuid(), 'QUOTA_RDV_PAR_CRENEAU', '5', 'Nombre maximum de rendez-vous par creneau horaire', TRUE),
    (gen_random_uuid(), 'DUREE_CONSULTATION_DEFAUT', '30', 'Duree moyenne d''une consultation en minutes', TRUE),
    (gen_random_uuid(), 'DELAI_RAPPEL_RDV_HEURES', '24', 'Delai avant rendez-vous pour l''envoi du rappel (en heures)', TRUE),
    (gen_random_uuid(), 'SEUIL_ALERTE_STOCK', '10', 'Seuil minimum de stock declenchant une alerte', TRUE);
