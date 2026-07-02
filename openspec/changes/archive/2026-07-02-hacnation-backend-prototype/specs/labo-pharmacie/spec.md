## ADDED Requirements

### Requirement: Receive lab requests
Le système SHALL recevoir les demandes d'analyse émises par le service de consultation via Kafka.

#### Scenario: Réception demande d'analyse
- **WHEN** un événement `prescription-events` de type EXAMEN est consommé
- **THEN** le système crée une demande d'analyse avec statut EN_ATTENTE

#### Scenario: Consultation des demandes
- **WHEN** un laborantin consulte les analyses en attente via `GET /api/labo/demandes?statut=EN_ATTENTE`
- **THEN** le système retourne la liste paginée des demandes à traiter

### Requirement: Record lab results
Le système SHALL permettre au laborantin de saisir les résultats d'une analyse.

#### Scenario: Saisie résultats
- **WHEN** un laborantin soumet les résultats d'une analyse via `POST /api/labo/demandes/{id}/resultats`
- **THEN** le système enregistre les résultats, passe le statut à TERMINE, et publie un événement Kafka `lab-events`

#### Scenario: Résultats avec valeurs de référence
- **WHEN** les résultats incluent les valeurs normales de référence
- **THEN** le système marque les valeurs hors normes

### Requirement: Validate lab results
Le système SHALL permettre la validation des résultats par un biologiste ou responsable labo.

#### Scenario: Validation résultats
- **WHEN** un responsable valide les résultats via `POST /api/labo/demandes/{id}/valider`
- **THEN** le statut passe à VALIDE, le DME du patient est mis à jour, et une notification est envoyée

### Requirement: FHIR DiagnosticReport
Le système SHALL exposer les résultats d'analyse au format FHIR DiagnosticReport.

#### Scenario: FHIR DiagnosticReport
- **WHEN** une requête est faite sur `/fhir/DiagnosticReport?subject=Patient/{id}`
- **THEN** le système retourne les comptes rendus d'analyse au format FHIR

### Requirement: Receive pharmacy orders
Le système SHALL recevoir les ordonnances émises par le service de consultation via Kafka.

#### Scenario: Réception ordonnance
- **WHEN** un événement `prescription-events` de type MEDICAMENT est consommé
- **THEN** le système crée une ordonnance avec statut EN_ATTENTE

### Requirement: Dispense medication
Le système SHALL permettre au pharmacien de délivrer les médicaments prescrits.

#### Scenario: Délivrance médicament
- **WHEN** un pharmacien délivre les médicaments d'une ordonnance via `POST /api/pharma/ordonnances/{id}/delivrer`
- **THEN** le stock est décrémenté, le statut passe à DELIVREE, un événement `pharmacy-events` est publié

#### Scenario: Stock insuffisant
- **WHEN** un médicament prescrit n'est pas disponible en stock suffisant
- **THEN** le système retourne HTTP 409 avec les médicaments en rupture

### Requirement: Stock management with FEFO rule
Le système SHALL gérer le stock de médicaments selon la règle FEFO (First Expired, First Out).

#### Scenario: Délivrance FEFO
- **WHEN** un médicament est délivré et plusieurs lots existent
- **THEN** le système prélève automatiquement du lot ayant la date de péremption la plus proche

#### Scenario: Consultation stock
- **WHEN** le pharmacien consulte le stock via `GET /api/pharma/stock`
- **THEN** le système retourne le stock par médicament, lot, quantité, et date de péremption

### Requirement: Expiry alerts
Le système SHALL alerter lorsque des médicaments approchent de leur date de péremption.

#### Scenario: Alerte péremption proche
- **WHEN** un médicament a une date de péremption dans moins de 30 jours
- **THEN** le système génère une alerte visible dans le dashboard pharmacie

#### Scenario: Médicament périmé
- **WHEN** un médicament a dépassé sa date de péremption
- **THEN** le système le marque comme PERIME et bloque sa délivrance

### Requirement: Medication return to DME
Le système SHALL mettre à jour le DME du patient après délivrance de médicaments.

#### Scenario: Mise à jour DME
- **WHEN** des médicaments sont délivrés
- **THEN** un événement Kafka `pharmacy-events` est publié pour mise à jour du DME patient

### Requirement: Low stock alerts
Le système SHALL alerter lorsque le stock d'un médicament passe sous un seuil critique.

#### Scenario: Alerte stock bas
- **WHEN** le stock d'un médicament passe sous le seuil minimum défini
- **THEN** une notification est publiée via `notification-events`
