## ADDED Requirements

### Requirement: Generate invoice
Le système SHALL générer automatiquement une facture à partir des actes d'une consultation.

#### Scenario: Facture générée après consultation
- **WHEN** un événement `consultation-events` est reçu avec statut TERMINE
- **THEN** le système consolide les actes (consultation, examens, médicaments) et génère une facture avec statut EN_ATTENTE

#### Scenario: Consultation de facture
- **WHEN** un utilisateur consulte une facture via `GET /api/facturation/factures/{id}`
- **THEN** le système retourne le détail de la facture avec les lignes d'actes et le total

### Requirement: Invoice itemization
Le système SHALL détailler chaque acte facturable dans les lignes de facture.

#### Scenario: Lignes de facture
- **WHEN** une facture est générée
- **THEN** chaque ligne contient : description de l'acte, quantité, prix unitaire, montant

### Requirement: Consolidate multiple acts
Le système SHALL consolider tous les actes d'un patient en une seule facture.

#### Scenario: Regroupement d'actes
- **WHEN** un patient a une consultation, des examens de laboratoire et des médicaments
- **THEN** tous ces actes sont regroupés dans une seule facture avec le total consolidé

### Requirement: Simulate Mobile Money payment
Le système SHALL simuler un paiement par Mobile Money (Orange Money, MTN Money, Wave).

#### Scenario: Paiement Orange Money simulé
- **WHEN** un paiement est initié via `POST /api/facturation/payer` avec mode=ORANGE_MONEY et numeroTelephone
- **THEN** le système simule le paiement, marque la facture comme PAYEE, et publie un événement `billing-events`

#### Scenario: Paiement MTN Money simulé
- **WHEN** un paiement est initié avec mode=MTN_MONEY
- **THEN** le système simule le paiement avec succès et retourne une référence de transaction

#### Scenario: Paiement Wave simulé
- **WHEN** un paiement est initié avec mode=WAVE
- **THEN** le système simule le paiement et retourne une confirmation

### Requirement: Generate receipt
Le système SHALL générer un reçu après un paiement réussi.

#### Scenario: Reçu après paiement
- **WHEN** un paiement est effectué avec succès
- **THEN** le système retourne un reçu avec : numéro de facture, date, montant, mode de paiement, référence transaction

### Requirement: Payment history
Le système SHALL permettre la consultation de l'historique des paiements.

#### Scenario: Historique par patient
- **WHEN** l'historique est consulté via `GET /api/facturation/paiements?patientId={id}`
- **THEN** le système retourne la liste des paiements du patient avec dates et montants

#### Scenario: Historique par période
- **WHEN** l'historique est consulté via `GET /api/facturation/paiements?dateDebut=X&dateFin=Y`
- **THEN** le système retourne les paiements de la période spécifiée

### Requirement: Cash register summary
Le système SHALL fournir un résumé de caisse pour une période donnée.

#### Scenario: Résumé journalier
- **WHEN** un caissier demande le résumé de caisse du jour
- **THEN** le système retourne : nombre de factures, total des paiements, détail par mode de paiement

### Requirement: Invoice status tracking
Le système SHALL suivre le cycle de vie d'une facture (EN_ATTENTE, PAYEE, ANNULEE, PARTIELLE).

#### Scenario: Paiement partiel
- **WHEN** un paiement partiel est effectué sur une facture
- **THEN** le statut passe à PARTIELLE et le montant restant dû est mis à jour

### Requirement: Invoice notification
Le système SHALL notifier le patient lors de la génération d'une facture et après paiement.

#### Scenario: Notification facture
- **WHEN** une facture est générée
- **THEN** un événement `notification-events` est publié pour informer le patient

#### Scenario: Notification paiement
- **WHEN** un paiement est effectué
- **THEN** un événement de confirmation de paiement est publié
