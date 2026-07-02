# HACNATION - Logiciel integre de gestion d'etablissement sanitaire

Backend microservices pour la gestion complete d'un etablissement de sante en Cote d'Ivoire.

## Stack technique

| Composant | Technologie |
|-----------|-------------|
| Langage | Java 21 |
| Framework | Spring Boot 3.4.3 |
| Microservices | 17 services + Gateway + Eureka + Config Server |
| Message broker | Apache Kafka 3.9 (KRaft) |
| Base de donnees | PostgreSQL 16 (18 schemas) + Redis 7 |
| Authentification | Keycloak 26.1 (OAuth2/OIDC) |
| Interoperabilite | HAPI FHIR 7.6.1 (R4) |
| API Docs | OpenAPI 3.0 (SpringDoc) |
| Conteneurisation | Docker Compose (22 conteneurs) |

## Demarrage rapide (Hackathon)

### Option 1 : Mode coeur (recommande)
12 services essentiels pour le parcours patient complet.

```bash
# Linux / Mac
./start-hackathon.sh

# Windows PowerShell
powershell -File scripts/start-demo.ps1

# Ou directement
docker compose up -d --build
```

### Option 2 : Mode complet
Tous les 17 services + hospitalisation, urgences, soins, bloc.

```bash
./start-hackathon.sh full
# ou
powershell -File scripts/start-demo.ps1 -Full
```

Apres demarrage (~2-3 minutes), l'API Gateway est accessible sur **http://localhost:8080**.

## Comptes de test

| Utilisateur | Mot de passe | Role |
|-------------|-------------|------|
| `admin` | `admin123` | Administrateur |
| `medecin` | `medecin123` | Medecin |
| `accueil` | `accueil123` | Agent d'accueil |
| `laborantin` | `labo123` | Laborantin |
| `pharmacien` | `pharma123` | Pharmacien |
| `caissier` | `caisse123` | Caissier |
| `patient1` | `patient123` | Patient |

## Endpoints

| Service | URL |
|---------|-----|
| API Gateway | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Keycloak Admin | http://localhost:8180 (admin/admin) |
| Eureka Dashboard | http://localhost:8761 |
| Kafka Broker | localhost:9092 |

## Demo end-to-end

Scripts de demonstration du parcours patient complet :

```bash
# Linux / Mac
./scripts/demo-end-to-end.sh

# Windows
powershell -File scripts/demo-end-to-end.ps1
```

## Collection Postman

Importer `postman/HACNATION.postman_collection.json` dans Postman avec l'environnement `postman/HACNATION.postman_environment.json`.

## Tests

```bash
cd backend
mvn test                     # Tests unitaires
mvn verify                   # Tests + rapport JaCoCo
```

## Modules

```
backend/
├── common-lib/              # Libre partage : DTOs, events, enums, exceptions, Audit AOP
├── gateway/                 # API Gateway (Spring Cloud Gateway + Keycloak OAuth2)
├── discovery-server/        # Netflix Eureka Service Registry
├── config-server/           # Spring Cloud Config Server
├── patient-identity-service/# Gestion des patients (CRUD, doublons)
├── dme-service/             # Dossier Medical Electronique + FHIR Patient
├── rdv-service/             # Rendez-vous, QR codes, creneaux + FHIR Appointment
├── fileattente-service/     # File d'attente temps reel (Redis)
├── consultation-service/    # Consultations, diagnostics + FHIR Observation
├── prescription-service/    # Prescriptions examens/medicaments + Kafka routing
├── laboratoire-service/     # Demandes analyse, resultats, validation + FHIR DiagnosticReport
├── pharmacie-service/       # Ordonnances, stock (FEFO), delivrance
├── facturation-service/     # Factures automatiques, consolidation
├── caisse-service/          # Paiements (Mobile Money, especes, carte)
├── accueil-service/         # Admissions (consultation, urgence, hospitalisation)
├── hospitalisation-service/ # Gestion des lits et sejours
├── urgences-service/        # Triage 5 niveaux
├── soins-service/           # Plans de soins infirmiers
├── bloc-service/            # Interventions chirurgicales
├── notification-service/    # Notifications SMS/WhatsApp/Email/Push
└── reporting-admin-service/ # CRUD referentiels, dashboards, audit trail, parametrage
```

## Cas d'utilisation couverts

29/29 cas d'utilisation du cahier des charges :

| Module | UCs |
|--------|-----|
| Comptes, Securite & Administration | UC-01 a UC-05 |
| Parcours Patient & Files d'Attente | UC-06 a UC-14 |
| Consultation Medicale | UC-15 a UC-18 |
| Laboratoire & Imagerie | UC-19, UC-20 |
| Pharmacie | UC-21 a UC-23 |
| Facturation & Caisse | UC-24 a UC-26 |
| Services Transverses | UC-27 a UC-29 |

## Licence

Proprietary - HACNATION
