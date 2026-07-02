## ADDED Requirements

### Requirement: Patient admission
Le système SHALL permettre l'admission d'un patient dans l'établissement.

#### Scenario: Admission patient
- **WHEN** un agent d'accueil crée une admission avec patientId, type (CONSULTATION, URGENCE, HOSPITALISATION), service
- **THEN** le système retourne HTTP 201, publie un événement Kafka `admission-events`, et le patient est enregistré

#### Scenario: Admission avec orientation
- **WHEN** une admission est créée avec un service d'orientation spécifique
- **THEN** le système affecte le patient au service correspondant

### Requirement: Hospitalization management
Le système SHALL gérer les séjours d'hospitalisation : admission, suivi, sortie.

#### Scenario: Admission en hospitalisation
- **WHEN** un patient est admis en hospitalisation via `POST /api/admission/hospitalisations`
- **THEN** un lit est attribué, le séjour est créé avec statut EN_COURS, et le lit est marqué comme OCCUPE

#### Scenario: Sortie d'hospitalisation
- **WHEN** un patient sort d'hospitalisation via `POST /api/admission/hospitalisations/{id}/sortie`
- **THEN** le lit est libéré, le séjour est clôturé, et un événement est publié pour la facturation

#### Scenario: Aucun lit disponible
- **WHEN** une admission en hospitalisation est demandée mais aucun lit n'est disponible
- **THEN** le système retourne HTTP 409 avec message "Aucun lit disponible"

### Requirement: Bed management
Le système SHALL gérer l'état des lits (DISPONIBLE, OCCUPE, MAINTENANCE, RESERVE).

#### Scenario: État des lits
- **WHEN** le personnel consulte l'état des lits via `GET /api/admission/lits?service=MEDECINE`
- **THEN** le système retourne la liste des lits avec leur statut et le patient associé si occupé

#### Scenario: Lit en maintenance
- **WHEN** un lit est mis en maintenance via `PUT /api/admission/lits/{id}/statut`
- **THEN** le lit n'est plus disponible pour attribution

### Requirement: Emergency triage
Le système SHALL permettre le triage des patients aux urgences selon des échelles standardisées.

#### Scenario: Triage patient urgences
- **WHEN** un patient est trié aux urgences avec un niveau de gravité (1-5, échelle CIMU ou ESI)
- **THEN** le système enregistre le niveau de triage et l'oriente vers la file d'attente appropriée

#### Scenario: Triage niveau critique
- **WHEN** un patient est trié avec un niveau de gravité 1 (critique/extrême urgence)
- **THEN** le système le place en tête de la file d'attente urgences

### Requirement: Nursing care plan
Le système SHALL permettre la création et le suivi d'un plan de soins infirmiers.

#### Scenario: Plan de soins
- **WHEN** un infirmier crée un plan de soins pour un patient hospitalisé
- **THEN** le système enregistre les soins prescrits avec fréquence, posologie, et instructions

#### Scenario: Administration de soin
- **WHEN** un infirmier enregistre l'administration d'un soin
- **THEN** le système trace l'acte avec date, heure, infirmier, et observations

#### Scenario: Soin non administré
- **WHEN** un soin planifié n'est pas administré
- **THEN** le système permet de documenter le motif (refus patient, contre-indication, etc.)

### Requirement: Surgery block scheduling
Le système SHALL permettre la programmation des interventions chirurgicales.

#### Scenario: Programmation intervention
- **WHEN** une intervention est programmée avec patientId, type, chirurgien, dateHeure, salle
- **THEN** le système crée l'intervention avec statut PROGRAMMEE et vérifie les conflits de salle

#### Scenario: Conflit de salle
- **WHEN** une intervention est programmée sur une salle déjà occupée au même créneau
- **THEN** le système retourne HTTP 409

#### Scenario: Statut intervention
- **WHEN** une intervention passe de PROGRAMMEE à EN_COURS puis TERMINEE
- **THEN** le système met à jour le statut et trace chaque changement

### Requirement: Offline mode simulation
Le système SHALL simuler un mode dégradé pour les zones d'accueil/urgences.

#### Scenario: Sauvegarde brouillon offline
- **WHEN** une admission est créée avec le flag modeDegrade=true
- **THEN** le système sauvegarde l'admission en brouillon et la synchronise quand la connectivité est rétablie
