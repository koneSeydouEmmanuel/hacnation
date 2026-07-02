## Context

Projet greenfield pour un prototype de logiciel intégré de gestion d'établissement sanitaire en Côte d'Ivoire. L'offre technique de référence spécifie 17 microservices autour de Kafka, une persistance polyglotte, HAPI FHIR pour l'interopérabilité, et un contexte ivoirien (Mobile Money, WhatsApp/SMS, connectivité variable). Le backend sera consommé par deux équipes frontend distinctes (React pour le personnel soignant/administratif, Flutter pour les patients). Le prototype doit se lancer en une commande Docker Compose.

## Goals / Non-Goals

**Goals:**
- 8 microservices Spring Boot couvrant 100% des 21 modules fonctionnels
- API Gateway centralisée avec JWT et rôles RBAC
- Communication asynchrone via Apache Kafka (10 topics)
- Intégration FHIR native (HAPI FHIR) pour les ressources Patient, Appointment, Observation, MedicationRequest, ServiceRequest, DiagnosticReport
- Documentation OpenAPI 3.0 auto-générée
- Déploiement one-command via Docker Compose
- Simulation Mobile Money et notifications

**Non-Goals:**
- Frontend (React, Flutter) — géré par une autre équipe
- Déploiement Kubernetes (hors scope hackathon)
- Données réelles ou conformité RGPD/HIPAA complète
- Performance de production (pas de load testing poussé)
- Clustering multi-instances des services

## Decisions

### D1: 8 services vs 17 (offre) vs monolithe

**Décision**: 8 services regroupant les 21 modules par contexte métier.

**Rationnel**: 17 services est trop lourd pour un hackathon (complexité Docker, coordination, tests). Un monolithe ne démontre pas l'architecture microservices. 8 services = compromis optimal : chaque service correspond à un bounded context DDD réel, la communication Kafka reste démontrable, le déploiement reste gérable.

**Alternatives considérées**:
- 3 services → trop peu de granularité, ne démontre pas le pattern
- 17 services → overhead ingérable en hackathon, 17 conteneurs Spring Boot
- Modular monolith → ne respecte pas l'esprit de l'offre technique

### D2: PostgreSQL partagé avec schémas vs instances séparées

**Décision**: 1 instance PostgreSQL avec 8 schémas (un par service).

**Rationnel**: L'offre mentionne 17 services. Avoir 8 instances PostgreSQL dans Docker Compose serait excessif pour un prototype. La séparation logique par schémas garantit l'indépendance des données tout en restant pragmatique. À l'échelle de production, chaque service aurait son instance.

**Alternatives considérées**:
- 8 instances → fidèle à l'offre mais 8 conteneurs PG = ressources excessives en démo
- Base unique sans schémas → violation du pattern microservices

### D3: Polyglot persistence avec Redis

**Décision**: PostgreSQL + Redis. Redis pour le cache, l'état de la file d'attente, et les sessions temporaires.

**Rationnel**: L'offre spécifie une persistance polyglotte. Redis est le complément naturel pour les données volatiles (file d'attente temps réel, positions, cache des créneaux RDV). Démontre deux types de stockage sans complexité excessive.

### D4: HAPI FHIR natif dès le début

**Décision**: Intégration HAPI FHIR dans les services Patient-DME, Consultation-Prescription et Labo-Pharmacie.

**Rationnel**: Mapper FHIR après coup = duplication de modèles et risque d'incohérence. HAPI FHIR est le standard Java pour FHIR. Les endpoints `/fhir/Patient`, `/fhir/Appointment`, `/fhir/Observation` sont exposés nativement. Démontre l'interopérabilité santé dès le Sprint 1.

### D5: JWT stateless vs Keycloak

**Décision**: JWT stateless avec Spring Security, pas Keycloak.

**Rationnel**: Keycloak ajoute 1 conteneur de plus et une complexité de configuration SSO disproportionnée pour un hackathon. JWT stateless avec rôles encodés dans le token est simple, performant, et standard pour les APIs REST. L'équipe frontend consomme juste le token Bearer.

**Alternative considérée**: Keycloak → plus proche de l'offre mais overhead de config, conteneur supplémentaire, courbe d'apprentissage.

### D6: Kafka KRaft vs Zookeeper

**Décision**: Apache Kafka en mode KRaft (sans Zookeeper).

**Rationnel**: KRaft élimine le besoin d'un conteneur Zookeeper séparé. Kafka 3.x le supporte en production. 1 conteneur au lieu de 2. Configuration simplifiée.

## Architecture détaillée

### Structure Maven (monorepo)

```
backend/
├── pom.xml                           # Parent POM: Spring Boot 3.4, Java 21, Cloud 2024.x
├── common-lib/
│   └── src/main/java/com/hacnation/common/
│       ├── dto/                      # DTOs pour communication REST
│       ├── events/                   # Classes événements Kafka
│       ├── enums/                    # Statuts, rôles, types partagés
│       └── exception/               # GlobalExceptionHandler, ErrorResponse
├── gateway/
├── patient-dme-service/
├── rdv-fileattente-service/
├── consultation-prescription-service/
├── labo-pharmacie-service/
├── facturation-paiement-service/
├── admission-etendue-service/
└── notification-reporting-admin-service/
```

### Flux de données — Parcours patient complet

```
[Frontend React] ──HTTP──▶ [Gateway :8080] ──JWT──▶ [Services]

1. Accueil crée Patient ──▶ Patient-DME ──▶ Kafka: patient-created
2. Patient prend RDV ──▶ RDV-FileAttente ──▶ Kafka: rdv-created
3. QR généré, check-in ──▶ RDV-FileAttente ──▶ Kafka: queue-updated
4. Médecin consulte ──▶ Consultation-Prescr ──▶ Kafka: consultation-created
5. Prescription ──▶ Consultation-Prescr ──▶ Kafka: prescription-created
6a. Labo reçoit ──▶ Labo-Pharmacie ──▶ Kafka: lab-result-available
6b. Pharma délivre ──▶ Labo-Pharmacie ──▶ Kafka: drug-dispensed
7. Facture générée ──▶ Facturation-Paiement ──▶ Kafka: invoice-created
8. Notif + Dashboard ──▶ Notif-Report-Admin
```

### Topics Kafka (10)

| Topic | Partitions | Producer | Consumers |
|-------|-----------|----------|-----------|
| patient-events | 3 | Patient-DME | RDV, Consultation, Facturation, Notif |
| rdv-events | 3 | RDV-FileAttente | Consultation, Notif |
| queue-events | 3 | RDV-FileAttente | Notif |
| consultation-events | 3 | Consultation-Prescr | Patient-DME, Facturation, Notif |
| prescription-events | 3 | Consultation-Prescr | Labo-Pharma, Facturation |
| lab-events | 3 | Labo-Pharma | Patient-DME, Consultation, Notif |
| pharmacy-events | 3 | Labo-Pharma | Patient-DME, Facturation, Notif |
| billing-events | 3 | Facturation-Paiement | Notif, Patient-DME |
| admission-events | 3 | Admission-Étendue | Facturation, Notif |
| notification-events | 3 | Tous | Notif-Report-Admin |

### Sécurité

- Tous les endpoints (sauf `/actuator/health` et `/api/auth/login`) nécessitent JWT Bearer
- Rôles RBAC : `PATIENT`, `ACCUEIL`, `MEDECIN`, `LABORANTIN`, `PHARMACIEN`, `CAISSIER`, `DIRECTION`, `ADMIN`
- Le Gateway valide le JWT et forwarde le `X-User-Id` + `X-User-Roles` aux services
- Login endpoint dans le Gateway : `/api/auth/login` → renvoie JWT
- Endpoint `/api/auth/register` pour création initiale d'utilisateurs (admin seulement)

## Risks / Trade-offs

**[R1] 8 services = 8 JVM en local** → 8 conteneurs Spring Boot consomment ~4 Go RAM. Mitigation : Xmx limité à 256 Mo par service dans Docker Compose, profiling léger.

**[R2] Kafka KRaft mono-nœud** → Pas de haute disponibilité. Acceptable pour prototype. Mitigation : documenté comme limitation prototype.

**[R3] Pas de tests E2E entre services** → Risque de régressions inter-services. Mitigation : tests d'intégration par service avec Testcontainers, scénario de démo documenté.

**[R4] FHIR partiel** → Seules 6 ressources FHIR sont modélisées. Mitigation : choix délibéré, couvre le parcours principal, extensible.

**[R5] Mode dégradé simulé** → Pas de véritable offline-first ou cache local. Mitigation : simulation de brouillon sauvegardé, statut indiquant la connectivité.

## Open Questions

- Faut-il un service dédié pour l'observabilité (Prometheus/Grafana) ou des health checks Spring Boot suffisent ?
- Les PDF (ordonnances, résultats) doivent-ils être générés côté backend ou frontend ?
