## ADDED Requirements

### Requirement: Docker Compose one-command startup
Le système SHALL pouvoir être lancé intégralement avec la commande `docker compose up -d`.

#### Scenario: Démarrage complet
- **WHEN** `docker compose up -d` est exécuté à la racine du projet
- **THEN** tous les conteneurs démarrent : PostgreSQL, Redis, Kafka, Gateway, et les 8 services Spring Boot

#### Scenario: Health check après démarrage
- **WHEN** tous les conteneurs sont démarrés
- **THEN** le health check du Gateway sur `http://localhost:8080/actuator/health` retourne UP

### Requirement: PostgreSQL database
Le système SHALL fournir une instance PostgreSQL 16 avec 8 schémas distincts.

#### Scenario: Schémas créés automatiquement
- **WHEN** le conteneur PostgreSQL démarre
- **THEN** les 8 schémas sont créés via un script d'initialisation :
  patient_db, rdv_db, consultation_db, labo_pharma_db, facturation_db, admission_db, notif_report_db, gateway_db

### Requirement: Redis cache
Le système SHALL fournir une instance Redis 7 pour le cache et les données volatiles.

#### Scenario: Redis opérationnel
- **WHEN** le conteneur Redis démarre
- **THEN** les services peuvent s'y connecter pour le cache et la gestion de file d'attente temps réel

### Requirement: Apache Kafka event bus
Le système SHALL fournir Apache Kafka en mode KRaft (sans Zookeeper).

#### Scenario: Kafka opérationnel
- **WHEN** le conteneur Kafka démarre en mode KRaft
- **THEN** les 10 topics sont créés automatiquement et les services peuvent publier/consommer

### Requirement: Service configuration centralization
Le système SHALL centraliser la configuration des services via variables d'environnement.

#### Scenario: Configuration via .env
- **WHEN** les variables d'environnement sont définies dans un fichier .env
- **THEN** les services les utilisent pour la connexion aux bases de données, Kafka, Redis

### Requirement: Health check endpoints
Chaque service SHALL exposer un endpoint `/actuator/health` pour le monitoring.

#### Scenario: Health check par service
- **WHEN** une requête est faite sur `http://localhost:8081/actuator/health`
- **THEN** le service retourne UP ou DOWN avec les dépendances (DB, Kafka)

### Requirement: Structured logging
Les services SHALL produire des logs structurés au format JSON pour faciliter le debugging.

#### Scenario: Log structuré
- **WHEN** une action importante se produit (création patient, erreur, événement Kafka)
- **THEN** le log inclut : timestamp, service, niveau, message, et contexte (userId, patientId, etc.)

### Requirement: Graceful shutdown
Les services SHALL supporter un arrêt gracieux (graceful shutdown) de 30 secondes.

#### Scenario: Arrêt gracieux
- **WHEN** un signal SIGTERM est envoyé à un service
- **THEN** le service termine les requêtes en cours, flush les événements Kafka, et s'arrête proprement
