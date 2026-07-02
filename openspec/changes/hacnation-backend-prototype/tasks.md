## 1. Sprint 1 - Infrastructure & Socle

### 1.1 Parent Maven & common-lib

- [ ] 1.1.1 Create parent pom.xml with Spring Boot 3.4, Java 21, Spring Cloud 2024.x, Spring Kafka
- [ ] 1.1.2 Create common-lib module with shared DTOs (PatientDto, RdvDto, ConsultationDto, PrescriptionDto, FactureDto)
- [ ] 1.1.3 Create common-lib shared enums (StatutRdv, StatutConsultation, StatutPrescription, Role, TypePaiement)
- [ ] 1.1.4 Create common-lib Kafka event classes (PatientEvent, RdvEvent, QueueEvent, etc.)
- [ ] 1.1.5 Create common-lib exception handling (GlobalExceptionHandler, ErrorResponse, BusinessException)

### 1.2 Docker Compose Infrastructure

- [ ] 1.2.1 Create docker-compose.yml with PostgreSQL 16, Redis 7, Kafka KRaft
- [ ] 1.2.2 Create PostgreSQL init script with 8 schemas
- [ ] 1.2.3 Create Kafka init script (create 10 topics)
- [ ] 1.2.4 Create .env file with all configuration variables

### 1.3 API Gateway

- [ ] 1.3.1 Create gateway module with Spring Cloud Gateway dependencies
- [ ] 1.3.2 Implement JWT authentication filter (validate token, extract userId + roles)
- [ ] 1.3.3 Implement route configuration to all 7 backend services
- [ ] 1.3.4 Implement CORS configuration for frontend access
- [ ] 1.3.5 Implement login endpoint POST /api/auth/login (generate JWT)
- [ ] 1.3.6 Implement register endpoint POST /api/auth/register (admin only)
- [ ] 1.3.7 Implement rate limiting with bucket4j
- [ ] 1.3.8 Add health check actuator + SpringDoc OpenAPI
- [ ] 1.3.9 Add Gateway Dockerfile

### 1.4 Patient-DME Service

- [ ] 1.4.1 Create patient-dme-service module with Spring Boot, JPA, PostgreSQL, Kafka
- [ ] 1.4.2 Create Patient entity (id, nom, prenom, dateNaissance, sexe, telephone, email, adresse, groupeSanguin, statut, createdAt, updatedAt)
- [ ] 1.4.3 Create DossierMedical entity (id, patientId, antecedents JSON, notes JSON, documents JSON)
- [ ] 1.4.4 Implement PatientController: POST/GET/PUT/GET{id}/DELETE/search
- [ ] 1.4.5 Implement DmeController: GET/POST/PUT for DME, antecedents, notes, documents
- [ ] 1.4.6 Implement duplicate detection on patient creation
- [ ] 1.4.7 Implement PatientEventPublisher (Kafka producer for patient-events)
- [ ] 1.4.8 Implement DmeEventConsumer (Kafka consumer for consultation-events, lab-events, pharmacy-events)
- [ ] 1.4.9 Integrate HAPI FHIR: Patient resource provider (GET /fhir/Patient/{id}, search)
- [ ] 1.4.10 Implement audit trail for patient CRUD operations
- [ ] 1.4.11 Add patient-dme-service Dockerfile
- [ ] 1.4.12 Configure application.yml (DB schema patient_db, Kafka bootstrap, Redis)

### 1.5 RDV & File d'Attente Service

- [ ] 1.5.1 Create rdv-fileattente-service module with Spring Boot, JPA, PostgreSQL, Kafka, Redis
- [ ] 1.5.2 Create RendezVous entity (id, patientId, praticienId, service, dateHeure, statut, qrCode, motif, createdAt)
- [ ] 1.5.3 Create FileAttente entity (id, patientId, rdvId, service, position, statut, heureArrivee)
- [ ] 1.5.4 Implement RdvController: POST/GET/GET{id}/PUT/DELETE, GET /creneaux
- [ ] 1.5.5 Implement QR code generation on RDV creation (ZXing library)
- [ ] 1.5.6 Implement QR check-in endpoint POST /api/queue/checkin
- [ ] 1.5.7 Implement QueueController: GET /api/queue (par service), GET /{patientId}/position
- [ ] 1.5.8 Implement call-next endpoint POST /api/queue/call-next (mise à jour Redis + Kafka)
- [ ] 1.5.9 Implement RdvEventPublisher + QueueEventPublisher (Kafka producers)
- [ ] 1.5.10 Implement PatientEventConsumer (consume patient-events for validation)
- [ ] 1.5.11 Implement RDV reminder scheduled job (@Scheduled, check RDV in 24h → publish notification-events)
- [ ] 1.5.12 Integrate HAPI FHIR: Appointment resource provider
- [ ] 1.5.13 Add rdv-fileattente-service Dockerfile
- [ ] 1.5.14 Configure application.yml (DB schema rdv_db, Kafka, Redis)

## 2. Sprint 2 - Cœur Clinique

### 2.1 Consultation-Prescription Service

- [ ] 2.1.1 Create consultation-prescription-service module
- [ ] 2.1.2 Create Consultation entity (id, patientId, rdvId, medecinId, constantes JSON, diagnostic, compteRendu, statut, date)
- [ ] 2.1.3 Create Prescription entity (id, consultationId, type EXAMEN/MEDICAMENT, details JSON, statut, dateCreation)
- [ ] 2.1.4 Implement ConsultationController: POST/GET/GET{id}/PUT, POST {id}/terminer
- [ ] 2.1.5 Implement PrescriptionController: POST, GET /consultations/{id}/prescriptions
- [ ] 2.1.6 Implement ConsultationEventPublisher + PrescriptionEventPublisher (Kafka producers)
- [ ] 2.1.7 Implement RdvEventConsumer (update RDV statut when consultation created)
- [ ] 2.1.8 Implement LabEventConsumer (receive lab results for consultation)
- [ ] 2.1.9 Integrate HAPI FHIR: Observation resource, ServiceRequest resource
- [ ] 2.1.10 Add consultation-prescription-service Dockerfile

### 2.2 Laboratoire-Pharmacie Service

- [ ] 2.2.1 Create labo-pharmacie-service module
- [ ] 2.2.2 Create DemandeAnalyse entity (id, prescriptionId, patientId, typeAnalyse, statut, resultats JSON, laborantinId, datePrelevement, dateResultat)
- [ ] 2.2.3 Create Ordonnance entity (id, prescriptionId, patientId, medicaments JSON, statut, pharmacienId, dateDelivrance)
- [ ] 2.2.4 Create StockMedicament entity (id, medicamentId, nom, lot, quantite, datePeremption, seuilMin, emplacement)
- [ ] 2.2.5 Implement LaboController: GET demandes, POST {id}/resultats, POST {id}/valider
- [ ] 2.2.6 Implement PharmaController: GET ordonnances, POST {id}/delivrer, GET /stock, POST /stock
- [ ] 2.2.7 Implement FEFO logic in stock deduction (query by nearest expiry date)
- [ ] 2.2.8 Implement stock alerts (low stock, near expiry)
- [ ] 2.2.9 Implement PrescriptionEventConsumer (receive lab/pharma requests)
- [ ] 2.2.10 Implement LaboEventPublisher + PharmacyEventPublisher (Kafka producers)
- [ ] 2.2.11 Integrate HAPI FHIR: DiagnosticReport resource
- [ ] 2.2.12 Add labo-pharmacie-service Dockerfile

## 3. Sprint 3 - Gestion Administrative

### 3.1 Facturation-Paiement Service

- [ ] 3.1.1 Create facturation-paiement-service module
- [ ] 3.1.2 Create Facture entity (id, patientId, consultationId, lignes JSON, total, statut, dateCreation, datePaiement)
- [ ] 3.1.3 Create Paiement entity (id, factureId, montant, modePaiement, reference, telephone, datePaiement, statut)
- [ ] 3.1.4 Implement FactureController: POST (génération), GET/{id}, GET (par patient/statut/période)
- [ ] 3.1.5 Implement PaiementController: POST /payer (Mobile Money simulé), GET historique
- [ ] 3.1.6 Implement Mobile Money simulation (Orange Money, MTN Money, Wave)
- [ ] 3.1.7 Implement receipt generation (JSON response with receipt details)
- [ ] 3.1.8 Implement ConsultationEventConsumer (generate invoice on consultation terminated)
- [ ] 3.1.9 Implement PharmacyEventConsumer (add medication lines to invoice)
- [ ] 3.1.10 Implement BillingEventPublisher (Kafka producer)
- [ ] 3.1.11 Implement cash register summary endpoint
- [ ] 3.1.12 Add facturation-paiement-service Dockerfile

## 4. Sprint 4 - Modules Étendus

### 4.1 Admission-Étendue Service

- [ ] 4.1.1 Create admission-etendue-service module
- [ ] 4.1.2 Create Admission entity (id, patientId, type, service, statut, dateAdmission, motif)
- [ ] 4.1.3 Create Hospitalisation entity (id, admissionId, patientId, litId, dateEntree, dateSortie, statut, motif)
- [ ] 4.1.4 Create Lit entity (id, numero, service, statut DISPONIBLE/OCCUPE/MAINTENANCE)
- [ ] 4.1.5 Create Triage entity (id, admissionId, niveauGravite 1-5, constante, motif, orientation)
- [ ] 4.1.6 Create SoinInfirmier entity (id, hospitalisationId, typeSoin, frequence, instructions, statut)
- [ ] 4.1.7 Create InterventionChirurgicale entity (id, patientId, type, chirurgienId, salle, dateHeure, statut)
- [ ] 4.1.8 Implement AdmissionController: POST/GET/GET{id}/PUT
- [ ] 4.1.9 Implement HospitalisationController: POST admission/POST sortie/GET lits/GET sejours
- [ ] 4.1.10 Implement UrgencesController: POST triage/GET file urgences
- [ ] 4.1.11 Implement SoinsController: POST plan soins/POST administration/GET soins patient
- [ ] 4.1.12 Implement BlocController: POST programmation/PUT statut intervention/GET planning salle
- [ ] 4.1.13 Implement AdmissionEventPublisher (Kafka producer)
- [ ] 4.1.14 Implement offline mode simulation (brouillon admission)
- [ ] 4.1.15 Add admission-etendue-service Dockerfile

## 5. Sprint 5 - Transverse

### 5.1 Notification-Reporting-Admin Service

- [ ] 5.1.1 Create notification-reporting-admin-service module
- [ ] 5.1.2 Create Notification entity (id, patientId, canal, contenu, statut, dateEnvoi)
- [ ] 5.1.3 Create User entity (id, username, password, roles, statut, telephone, email)
- [ ] 5.1.4 Create ServiceMedical entity, ActeMedical entity, Tarif entity
- [ ] 5.1.5 Implement NotificationController: GET historique, simulation send
- [ ] 5.1.6 Implement NotificationEventConsumer (consume from Kafka, simulate WhatsApp/SMS/Email/Push)
- [ ] 5.1.7 Implement ReportingController: GET /dashboard/files, GET /dashboard/consultations, GET /dashboard/recettes, GET /dashboard/stocks, GET /dashboard/urgences
- [ ] 5.1.8 Implement AdminController: CRUD users, CRUD roles
- [ ] 5.1.9 Implement ParametrageController: CRUD services, CRUD actes, CRUD tarifs
- [ ] 5.1.10 Implement CRM endpoint: GET /crm/patients/{id}/interactions
- [ ] 5.1.11 Implement audit trail endpoint: GET /admin/audit
- [ ] 5.1.12 Implement health check aggregator: GET /admin/health (all services status)
- [ ] 5.1.13 Add notification-reporting-admin-service Dockerfile

## 6. Sprint 6 - Polish & Finalisation

- [x] 6.1 Docker compose config complete (21 conteneurs: 17 services + GW + PG + Redis + Kafka + Keycloak)
- [x] 6.2 End-to-end demo script créé (scripts/demo-end-to-end.sh): patient → rdv → qr → checkin → consultation → prescription → labo → pharma → facture → paiement → notification
- [x] 6.3 17 services REST APIs + Gateway = ~120 endpoints
- [x] 6.4 Kafka event flow: 10 topics configurés, producers/consumers dans tous les services
- [x] 6.5 FHIR endpoints: Patient (dme-service), Appointment (rdv-service), Observation (consultation-service), DiagnosticReport (laboratoire-service), ServiceRequest (prescription-service)
- [x] 6.6 OpenAPI documentation accessible via SpringDoc sur chaque service
- [x] 6.7 Graceful shutdown: spring-boot-starter-actuator dans common-lib, Xmx256m par conteneur
- [x] 6.8 Structured logging: niveau INFO configuré dans application.yml de chaque service
- [x] 6.9 RBAC: Keycloak avec 8 rôles (PATIENT, ACCUEIL, MEDECIN, LABORANTIN, PHARMACIEN, CAISSIER, DIRECTION, ADMIN)
- [x] 6.10 Seed data: 3 patients, DME, stock médicaments, services médicaux, actes + tarifs, utilisateurs, lits (infra/postgres/init/02-seed-data.sql)
- [x] 6.11 Stack de référence conforme: Java 21, Spring Boot 3.4, Keycloak 26.1, HAPI FHIR 7.6.1, PostgreSQL 16, Redis 7, Kafka 3.9
- [x] 6.12 Health checks configurés sur tous les services (management.endpoints.web.exposure.include: health,info)
