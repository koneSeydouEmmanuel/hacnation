## Why

La clinique cible a besoin d'une plateforme de gestion sanitaire intégrée couvrant 21 modules fonctionnels (20 + 1 innovant). Le backend doit exposer des APIs REST documentées pour deux équipes frontend distinctes (React et Flutter), respecter une architecture microservices autour d'Apache Kafka, intégrer HAPI FHIR pour l'interopérabilité santé, et être déployable en une commande Docker. Le contexte ivoirien impose le support Mobile Money et les notifications WhatsApp/SMS.

## What Changes

- Création de 8 microservices Spring Boot 3.4 / Java 21 couvrant l'ensemble des 21 modules fonctionnels
- API Gateway avec authentification JWT et routage vers les services
- Event bus Apache Kafka avec 10 topics pour la communication inter-services
- Persistance polyglotte PostgreSQL (8 schémas) + Redis
- Intégration native HAPI FHIR (Patient, Appointment, Observation, MedicationRequest, ServiceRequest, DiagnosticReport)
- Bibliothèque partagée common-lib (DTOs, événements Kafka, énumérations, utilitaires)
- API documentée OpenAPI 3.0 auto-générée pour l'équipe frontend
- Infrastructure Docker Compose pour déploiement en une commande
- Simulation Mobile Money et notifications WhatsApp/SMS/Email

## Capabilities

### New Capabilities

- `api-gateway-auth`: API Gateway Spring Cloud, authentification JWT, routage, rate limiting, rôles RBAC (PATIENT, ACCUEIL, MEDECIN, LABORANTIN, PHARMACIEN, CAISSIER, DIRECTION, ADMIN)
- `patient-dme`: CRUD patient, recherche, dossier médical électronique, antécédents, notes cliniques, documents, ressource FHIR Patient native
- `rdv-fileattente`: Prise de rendez-vous, agenda, créneaux, génération QR code, check-in, file d'attente, appel patient, statuts, ressource FHIR Appointment
- `consultation-prescription`: Saisie constantes, diagnostic, compte rendu, prescription d'examens et médicaments, suivi prescriptions, ressources FHIR Observation et ServiceRequest
- `labo-pharmacie`: Demandes d'analyse, saisie et validation résultats, ordonnances, délivrance médicaments, stock avec règle FEFO, alertes péremption, ressource FHIR DiagnosticReport
- `facturation-paiement`: Génération facture, consolidation actes, simulation paiement Mobile Money (Orange Money, MTN Money, Wave), reçu, historique caisse
- `admission-etendue`: Admission patient, hospitalisation et gestion lits, urgences avec triage (échelles validées), soins infirmiers, bloc opératoire et programmation chirurgicale
- `notification-reporting-admin`: Simulation notifications WhatsApp/SMS/Email/Push, tableaux de bord (files, consultations, recettes, stocks), CRUD utilisateurs/rôles/services/tarifs/actes, CRM patient, audit trail
- `infrastructure-devops`: Docker Compose (PostgreSQL, Redis, Kafka KRaft, 8 services), configuration centralisée, health checks, logs structurés, mode dégradé

### Modified Capabilities

<!-- Aucune capacité existante modifiée - projet greenfield -->

## Impact

- **Codebase**: Nouveau monorepo Maven backend/ avec 8 services + common-lib + gateway (10 modules Maven)
- **APIs**: ~96 endpoints REST documentés OpenAPI 3.0, accessibles via Gateway sur :8080
- **Infrastructure**: Docker Compose avec PostgreSQL 16, Redis 7, Kafka 3.x (KRaft), 9 conteneurs Spring Boot
- **Équipe frontend**: Contrats d'API clairs via OpenAPI, authentification JWT standard, websocket SSE pour temps réel
- **Dépendances externes**: HAPI FHIR 7.x, Spring Cloud Gateway, Spring Kafka, Spring Security, SpringDoc OpenAPI
