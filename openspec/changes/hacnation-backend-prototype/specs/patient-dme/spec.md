## ADDED Requirements

### Requirement: Create patient
Le système SHALL permettre la création d'un patient avec les données administratives de base.

#### Scenario: Création patient réussie
- **WHEN** un utilisateur autorisé crée un patient avec nom, prenom, dateNaissance, sexe, telephone
- **THEN** le système retourne HTTP 201 avec l'ID du patient et publie un événement Kafka `patient-events`

#### Scenario: Champ obligatoire manquant
- **WHEN** un patient est créé sans le champ obligatoire nom
- **THEN** le système retourne HTTP 400 avec un message de validation

### Requirement: Search patient
Le système SHALL permettre la recherche de patients par nom, prénom, téléphone ou numéro de dossier.

#### Scenario: Recherche par nom
- **WHEN** une recherche est effectuée avec le paramètre `nom=Doe`
- **THEN** le système retourne la liste paginée des patients correspondants

#### Scenario: Recherche par téléphone
- **WHEN** une recherche est effectuée avec le paramètre `telephone=0701020304`
- **THEN** le système retourne le patient unique correspondant ou une liste vide

#### Scenario: Aucun résultat
- **WHEN** une recherche ne correspond à aucun patient
- **THEN** le système retourne une liste vide avec pagination

### Requirement: Update patient
Le système SHALL permettre la mise à jour des données administratives d'un patient existant.

#### Scenario: Mise à jour réussie
- **WHEN** les données d'un patient existant sont modifiées
- **THEN** le système retourne HTTP 200 avec le patient mis à jour et publie un événement Kafka

#### Scenario: Patient inexistant
- **WHEN** une mise à jour est tentée sur un ID patient inexistant
- **THEN** le système retourne HTTP 404

### Requirement: Get patient by ID
Le système SHALL permettre la récupération d'un patient par son identifiant unique.

#### Scenario: Patient trouvé
- **WHEN** une requête est faite sur `/api/patients/{id}` avec un ID valide
- **THEN** le système retourne HTTP 200 avec les données complètes du patient

### Requirement: Dossier Médical Électronique (DME)
Le système SHALL associer un dossier médical à chaque patient contenant les antécédents, notes cliniques et documents.

#### Scenario: Consultation du DME
- **WHEN** un médecin consulte le DME d'un patient via `GET /api/patients/{id}/dme`
- **THEN** le système retourne les antécédents, notes cliniques et documents associés

#### Scenario: Ajout d'un antécédent médical
- **WHEN** un médecin ajoute un antécédent médical via `POST /api/patients/{id}/dme/antecedents`
- **THEN** le système enregistre l'antécédent et retourne HTTP 201

#### Scenario: Ajout d'une note clinique
- **WHEN** un médecin ajoute une note clinique au DME
- **THEN** le système enregistre la note avec date et auteur

### Requirement: FHIR Patient resource
Le système SHALL exposer les données patient au format FHIR R4 via HAPI FHIR.

#### Scenario: Accès FHIR Patient
- **WHEN** une requête est faite sur `/fhir/Patient/{id}` avec le header `Accept: application/fhir+json`
- **THEN** le système retourne une ressource FHIR Patient valide au format JSON

#### Scenario: Recherche FHIR Patient
- **WHEN** une recherche FHIR est effectuée via `/fhir/Patient?name=Doe`
- **THEN** le système retourne un Bundle FHIR contenant les patients correspondants

### Requirement: Duplicate patient detection
Le système SHALL détecter les doublons potentiels lors de la création d'un patient.

#### Scenario: Doublon détecté
- **WHEN** un nouveau patient a le même nom, prénom et date de naissance qu'un patient existant
- **THEN** le système retourne HTTP 409 avec la liste des patients potentiellement en doublon

### Requirement: Audit trail for patient operations
Le système SHALL journaliser toutes les opérations CRUD sur les patients.

#### Scenario: Journalisation de création
- **WHEN** un patient est créé
- **THEN** une entrée de journal est créée avec userId, action, patientId, et timestamp
