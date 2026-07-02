## ADDED Requirements

### Requirement: WhatsApp notification simulation
Le système SHALL simuler l'envoi de notifications WhatsApp.

#### Scenario: Notification WhatsApp
- **WHEN** un événement `notification-events` avec canal=WHATSAPP est consommé
- **THEN** le système log l'envoi simulé avec le numéro destinataire et le contenu du message

### Requirement: SMS notification simulation
Le système SHALL simuler l'envoi de notifications SMS.

#### Scenario: Notification SMS
- **WHEN** un événement `notification-events` avec canal=SMS est consommé
- **THEN** le système log l'envoi simulé avec le numéro destinataire et le contenu

### Requirement: Email notification simulation
Le système SHALL simuler l'envoi de notifications par email.

#### Scenario: Notification Email
- **WHEN** un événement `notification-events` avec canal=EMAIL est consommé
- **THEN** le système log l'envoi simulé avec l'adresse email destinataire, sujet et corps

### Requirement: Push notification simulation
Le système SHALL simuler l'envoi de notifications push mobiles.

#### Scenario: Notification Push
- **WHEN** un événement `notification-events` avec canal=PUSH est consommé
- **THEN** le système log la notification push simulée avec le device token et le contenu

### Requirement: Notification history
Le système SHALL conserver l'historique de toutes les notifications envoyées.

#### Scenario: Historique notifications patient
- **WHEN** l'historique est consulté via `GET /api/notifications?patientId={id}`
- **THEN** le système retourne la liste des notifications avec canal, date, statut, et contenu

### Requirement: Waiting queue dashboard
Le système SHALL fournir des indicateurs sur les files d'attente.

#### Scenario: Dashboard files d'attente
- **WHEN** la direction consulte `GET /api/reporting/dashboard/files`
- **THEN** le système retourne : nombre de patients en attente par service, temps d'attente moyen et maximum

### Requirement: Consultation dashboard
Le système SHALL fournir des indicateurs sur l'activité de consultation.

#### Scenario: Dashboard consultations
- **WHEN** la direction consulte `GET /api/reporting/dashboard/consultations`
- **THEN** le système retourne : nombre de consultations par jour/semaine, par médecin, par service

### Requirement: Revenue dashboard
Le système SHALL fournir des indicateurs financiers.

#### Scenario: Dashboard recettes
- **WHEN** la direction consulte `GET /api/reporting/dashboard/recettes`
- **THEN** le système retourne : recettes par jour, par mode de paiement, factures en attente, taux de recouvrement

### Requirement: Stock dashboard
Le système SHALL fournir des indicateurs sur les stocks de pharmacie.

#### Scenario: Dashboard stocks
- **WHEN** la direction consulte `GET /api/reporting/dashboard/stocks`
- **THEN** le système retourne : médicaments en rupture, médicaments proches de la péremption, valeur du stock

### Requirement: Emergency dashboard
Le système SHALL fournir des indicateurs sur l'activité des urgences.

#### Scenario: Dashboard urgences
- **WHEN** la direction consulte `GET /api/reporting/dashboard/urgences`
- **THEN** le système retourne : nombre d'admissions urgences, répartition par niveau de triage, temps de prise en charge moyen

### Requirement: User management (CRUD)
Le système SHALL permettre à un administrateur de gérer les utilisateurs.

#### Scenario: Création utilisateur
- **WHEN** un admin crée un utilisateur avec username, password, role, et informations personnelles
- **THEN** le système retourne HTTP 201 et l'utilisateur peut s'authentifier

#### Scenario: Liste utilisateurs
- **WHEN** un admin consulte la liste des utilisateurs
- **THEN** le système retourne la liste paginée avec rôles et statut (ACTIF, INACTIF)

### Requirement: Role management
Le système SHALL permettre la gestion des rôles et permissions.

#### Scenario: Attribution de rôle
- **WHEN** un admin attribue un ou plusieurs rôles à un utilisateur
- **THEN** les rôles sont mis à jour et prendront effet à la prochaine authentification

### Requirement: Service catalog management
Le système SHALL permettre la gestion des services médicaux de l'établissement.

#### Scenario: CRUD service
- **WHEN** un admin crée ou modifie un service (ex: Cardiologie, Pédiatrie, Urgences)
- **THEN** le service est disponible pour les RDV et admissions

### Requirement: Pricing management
Le système SHALL permettre la gestion des tarifs des actes médicaux.

#### Scenario: Définition tarif
- **WHEN** un admin définit le tarif d'un acte (consultation, examen, médicament)
- **THEN** le tarif est utilisé pour la génération des factures

### Requirement: Medical act catalog
Le système SHALL permettre la gestion du catalogue des actes médicaux.

#### Scenario: CRUD acte médical
- **WHEN** un admin crée un acte médical avec code, libellé, type, tarif par défaut
- **THEN** l'acte est disponible pour les prescriptions et facturations

### Requirement: CRM - Patient interaction history
Le système SHALL tracer l'historique des interactions avec chaque patient.

#### Scenario: Historique interactions
- **WHEN** le CRM est consulté via `GET /api/crm/patients/{id}/interactions`
- **THEN** le système retourne : RDV, consultations, appels, notifications, et dates associées

### Requirement: Audit trail
Le système SHALL journaliser les actions importantes dans un audit trail.

#### Scenario: Consultation audit trail
- **WHEN** un admin consulte l'audit trail via `GET /api/admin/audit`
- **THEN** le système retourne les entrées filtrées par action, utilisateur, date, et type d'opération

### Requirement: System health monitoring
Le système SHALL exposer des endpoints de health check pour le monitoring.

#### Scenario: Health check agrégé
- **WHEN** un health check est consulté via `GET /api/admin/health`
- **THEN** le système retourne le statut de tous les services, Kafka, PostgreSQL, et Redis
