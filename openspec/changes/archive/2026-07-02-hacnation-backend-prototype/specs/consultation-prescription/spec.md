## ADDED Requirements

### Requirement: Create consultation
Le système SHALL permettre à un médecin de créer une consultation pour un patient.

#### Scenario: Consultation créée
- **WHEN** un médecin crée une consultation avec patientId, constantes (TA, pouls, température, poids), motif
- **THEN** le système retourne HTTP 201, publie un événement Kafka `consultation-events`, et met à jour le DME du patient

#### Scenario: Consultation liée à un RDV
- **WHEN** une consultation est créée avec un rdvId valide
- **THEN** le RDV est automatiquement marqué comme HONORE

### Requirement: Record vital signs
Le système SHALL permettre la saisie des constantes vitales lors de la consultation.

#### Scenario: Saisie des constantes
- **WHEN** le médecin saisit tension arterielle, pouls, température, poids, taille, SpO2
- **THEN** ces valeurs sont enregistrées dans la consultation et exposées en Observation FHIR

### Requirement: Record diagnosis and clinical report
Le système SHALL permettre la saisie du diagnostic et du compte rendu de consultation.

#### Scenario: Saisie diagnostic
- **WHEN** le médecin saisit un diagnostic principal et un compte rendu
- **THEN** le système enregistre ces informations et les associe à la consultation

#### Scenario: Consultation sans diagnostic
- **WHEN** une consultation est créée sans diagnostic (en cours)
- **THEN** le système accepte avec le statut EN_COURS

### Requirement: Prescribe laboratory exam
Le système SHALL permettre au médecin de prescrire des examens de laboratoire.

#### Scenario: Prescription d'examen
- **WHEN** le médecin prescrit un ou plusieurs examens (ex: NFS, glycémie, sérologie)
- **THEN** le système crée une prescription de type EXAMEN, publie un événement Kafka `prescription-events`, et le laboratoire est notifié

#### Scenario: Prescription sans consultation
- **WHEN** une prescription est créée sans consultationId valide
- **THEN** le système retourne HTTP 400

### Requirement: Prescribe medication
Le système SHALL permettre au médecin de prescrire des médicaments.

#### Scenario: Prescription médicamenteuse
- **WHEN** le médecin prescrit un ou plusieurs médicaments avec posologie et durée
- **THEN** le système crée une prescription de type MEDICAMENT, publie un événement Kafka, et la pharmacie est notifiée

#### Scenario: Prescription mixte
- **WHEN** une consultation génère à la fois des examens et des médicaments
- **THEN** le système crée les prescriptions correspondantes avec le bon type

### Requirement: Track prescription status
Le système SHALL suivre le statut de chaque prescription (EN_ATTENTE, EN_COURS, TERMINEE, ANNULEE).

#### Scenario: Statut prescription
- **WHEN** le médecin consulte les prescriptions d'une consultation via `GET /api/consultations/{id}/prescriptions`
- **THEN** le système retourne toutes les prescriptions avec leur statut actuel

### Requirement: FHIR Observation for vital signs
Le système SHALL exposer les constantes vitales au format FHIR Observation.

#### Scenario: FHIR Observation
- **WHEN** une requête est faite sur `/fhir/Observation?subject=Patient/{id}`
- **THEN** le système retourne un Bundle FHIR contenant les observations du patient

### Requirement: FHIR ServiceRequest for prescriptions
Le système SHALL exposer les prescriptions d'examens au format FHIR ServiceRequest.

#### Scenario: FHIR ServiceRequest
- **WHEN** une requête est faite sur `/fhir/ServiceRequest?subject=Patient/{id}`
- **THEN** le système retourne les prescriptions d'examens au format FHIR

### Requirement: Complete consultation
Le système SHALL permettre de marquer une consultation comme terminée.

#### Scenario: Consultation terminée
- **WHEN** le médecin finalise une consultation
- **THEN** le statut passe à TERMINEE, une notification est publiée pour la facturation, et le compte rendu est sauvegardé dans le DME
