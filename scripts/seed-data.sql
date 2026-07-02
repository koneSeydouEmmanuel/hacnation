INSERT INTO patient_identity_db.patients (id, nom, prenom, date_naissance, sexe, telephone, email, adresse, groupe_sanguin, statut, created_at, updated_at) VALUES
('P001', 'Kouadio', 'Yao', '1985-06-15', 'M', '0701020304', 'yao.kouadio@email.ci', 'Abidjan, Cocody', 'O+', 'ACTIF', NOW(), NOW()),
('P002', 'Traore', 'Aminata', '1990-03-22', 'F', '0705060708', 'aminata.traore@email.ci', 'Abidjan, Yopougon', 'A+', 'ACTIF', NOW(), NOW()),
('P003', 'Kone', 'Ibrahim', '1978-11-08', 'M', '0709101112', 'ibrahim.kone@email.ci', 'Abidjan, Marcory', 'B+', 'ACTIF', NOW(), NOW());

INSERT INTO dme_db.dossiers_medicaux (id, patient_id, antecedents, notes_cliniques, documents, created_at, updated_at) VALUES
('DME001', 'P001', '[{"type":"chirurgie","description":"Appendicectomie","date":"2010-03-12"},{"type":"allergie","description":"Penicilline"}]', '[]', '[]', NOW(), NOW()),
('DME002', 'P002', '[{"type":"chronique","description":"Diabete type 2","date":"2018-01-15"},{"type":"allergie","description":"Aucune"}]', '[]', '[]', NOW(), NOW()),
('DME003', 'P003', '[{"type":"chirurgie","description":"Fracture bras gauche","date":"2015-07-20"},{"type":"allergie","description":"Aucune"}]', '[]', '[]', NOW(), NOW());

INSERT INTO pharmacie_db.stock_medicaments (id, medicament_id, nom, lot, quantite, date_peremption, seuil_min, emplacement, created_at, updated_at) VALUES
('STK001', 'MED001', 'Paracetamol 500mg', 'LOT2026-001', 500, '2027-12-31', 50, 'Rayon A-1', NOW(), NOW()),
('STK002', 'MED001', 'Paracetamol 500mg', 'LOT2025-003', 200, '2026-08-15', 50, 'Rayon A-1', NOW(), NOW()),
('STK003', 'MED002', 'Amoxicilline 500mg', 'LOT2026-002', 300, '2027-06-30', 30, 'Rayon B-2', NOW(), NOW()),
('STK004', 'MED003', 'Omeprazole 20mg', 'LOT2026-004', 150, '2027-09-01', 20, 'Rayon C-1', NOW(), NOW()),
('STK005', 'MED004', 'Metformine 850mg', 'LOT2025-001', 400, '2027-03-15', 40, 'Rayon D-3', NOW(), NOW()),
('STK006', 'MED005', 'Ibuprofene 400mg', 'LOT2026-006', 5, '2026-09-30', 10, 'Rayon A-2', NOW(), NOW()),
('STK007', 'MED006', 'Ceftriaxone 1g injectable', 'LOT2026-007', 80, '2027-11-01', 10, 'Rayon B-1', NOW(), NOW()),
('STK008', 'MED007', 'Salbutamol 100mcg', 'LOT2026-008', 250, '2027-08-15', 25, 'Rayon D-1', NOW(), NOW());

INSERT INTO reporting_admin_db.services_medicaux (id, code, libelle, description, statut) VALUES
('SVC001', 'CONSULTATION', 'Consultation Generale', 'Consultation de medecine generale', 'ACTIF'),
('SVC002', 'PEDIATRIE', 'Pediatrie', 'Consultation pediatrique', 'ACTIF'),
('SVC003', 'CARDIOLOGIE', 'Cardiologie', 'Consultation de cardiologie', 'ACTIF'),
('SVC004', 'LABORATOIRE', 'Laboratoire', 'Laboratoire d''analyses medicales', 'ACTIF'),
('SVC005', 'PHARMACIE', 'Pharmacie', 'Pharmacie de l''etablissement', 'ACTIF'),
('SVC006', 'URGENCES', 'Urgences', 'Service d''urgences', 'ACTIF'),
('SVC007', 'MATERNITE', 'Maternite', 'Service de maternite', 'ACTIF');

INSERT INTO reporting_admin_db.actes_medicaux (id, code, libelle, type, tarif_par_defaut) VALUES
('ACT001', 'CSG', 'Consultation Generaliste', 'CONSULTATION', 5000),
('ACT002', 'CSP', 'Consultation Specialiste', 'CONSULTATION', 10000),
('ACT003', 'NFS', 'Numeration Formule Sanguine', 'EXAMEN', 3500),
('ACT004', 'GLYC', 'Glycemie', 'EXAMEN', 2500),
('ACT005', 'SERO', 'Serologie', 'EXAMEN', 5000),
('ACT006', 'RADIO', 'Radiographie Standard', 'EXAMEN', 8000),
('ACT007', 'PANS', 'Pansement Simple', 'ACTE', 2000),
('ACT008', 'INJ', 'Injection', 'ACTE', 1500);

INSERT INTO reporting_admin_db.tarifs (id, acte_medical_id, service_id, prix, date_debut, date_fin) VALUES
('TRF001', 'ACT001', 'SVC001', 5000, '2026-01-01', NULL),
('TRF002', 'ACT002', 'SVC002', 10000, '2026-01-01', NULL),
('TRF003', 'ACT003', 'SVC004', 3500, '2026-01-01', NULL),
('TRF004', 'ACT004', 'SVC004', 2500, '2026-01-01', NULL),
('TRF005', 'ACT007', 'SVC001', 2000, '2026-01-01', NULL);

INSERT INTO reporting_admin_db.utilisateurs (id, username, password, roles, nom, prenom, telephone, email, statut, created_at) VALUES
('U001', 'admin', '$2a$10$placeholder', 'ADMIN', 'Doe', 'John', '0700000001', 'admin@hacnation.ci', 'ACTIF', NOW()),
('U002', 'medecin', '$2a$10$placeholder', 'MEDECIN', 'Konan', 'Serge', '0700000002', 'dr.konan@hacnation.ci', 'ACTIF', NOW()),
('U003', 'laborantin', '$2a$10$placeholder', 'LABORANTIN', 'Bamba', 'Fatou', '0700000003', 'labo@hacnation.ci', 'ACTIF', NOW()),
('U004', 'pharmacien', '$2a$10$placeholder', 'PHARMACIEN', 'Toure', 'Moussa', '0700000004', 'pharma@hacnation.ci', 'ACTIF', NOW()),
('U005', 'caissier', '$2a$10$placeholder', 'CAISSIER', 'Diallo', 'Awa', '0700000005', 'caisse@hacnation.ci', 'ACTIF', NOW()),
('U006', 'accueil', '$2a$10$placeholder', 'ACCUEIL', 'Coulibaly', 'Nahomie', '0700000006', 'accueil@hacnation.ci', 'ACTIF', NOW(),
('U007', 'direction', '$2a$10$placeholder', 'DIRECTION', 'Ouattara', 'Issa', '0700000007', 'direction@hacnation.ci', 'ACTIF', NOW());

INSERT INTO hospitalisation_db.lits (id, numero, service, statut, patient_id) VALUES
('LIT001', '101', 'MEDECINE', 'DISPONIBLE', NULL),
('LIT002', '102', 'MEDECINE', 'DISPONIBLE', NULL),
('LIT003', '103', 'MEDECINE', 'OCCUPE', 'P002'),
('LIT004', '201', 'MATERNITE', 'DISPONIBLE', NULL),
('LIT005', '202', 'MATERNITE', 'MAINTENANCE', NULL),
('LIT006', '301', 'CHIRURGIE', 'DISPONIBLE', NULL);
