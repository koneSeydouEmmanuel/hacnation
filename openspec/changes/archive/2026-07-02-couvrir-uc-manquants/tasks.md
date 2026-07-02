## 1. Phase 0 -- Infrastructure de test

- [x] 1.1 Ajouter spring-boot-starter-test dans le dependencyManagement du parent POM
- [x] 1.2 Ajouter H2 (runtime, scope test) dans le dependencyManagement du parent POM
- [x] 1.3 Ajouter testcontainers-bom dans le dependencyManagement du parent POM (postgresql, kafka)
- [x] 1.4 Ajouter spring-boot-starter-test dans chaque POM de service (gateway + 17 services + common-lib)
- [x] 1.5 Ajouter H2 dans chaque POM de service avec JPA (16 services)
- [x] 1.6 Ajouter plugin JaCoCo dans le parent POM avec seuil 70%
- [x] 1.7 Creer src/test/resources/application-test.yml dans chaque service
- [x] 1.8 Creer les repertoires src/test/java/com/hacnation/{service}/unit/ et integration/ dans chaque service

## 2. Phase 1 -- UC-05 Parametrage systeme

- [x] 2.1 Creer entite ParametrageSysteme dans reporting-admin-service (id, cle, valeur, description, modifiable)
- [x] 2.2 Creer migration SQL V2__init_parametrage.sql (table parametrage_systeme)
- [x] 2.3 Creer ParametrageRepositoryPort (interface) dans reporting-admin-service
- [x] 2.4 Creer ParametrageJpaAdapter + ParametrageJpaRepository
- [x] 2.5 Ajouter methodes CRUD dans AdminUseCase (interface)
- [x] 2.6 Implementer CRUD parametres dans AdminServiceImpl
- [x] 2.7 Creer ParametrageController (GET/POST/PUT/DELETE, prefixe /api/admin/parametres)
- [x] 2.8 Inserer les parametres par defaut dans V2__init_parametrage.sql (HORAIRES_OUVERTURE, QUOTA_RDV_PAR_CRENEAU, DUREE_CONSULTATION_DEFAUT, DELAI_RAPPEL_RDV_HEURES, SEUIL_ALERTE_STOCK)
- [x] 2.9 Ajouter validation des valeurs numeriques dans AdminServiceImpl

## 3. Phase 1 -- UC-28 Audit Trail global

- [x] 3.1 Creer entite PisteAudit dans reporting-admin-service (userId, timestamp, service, action, oldValue, newValue, entityId)
- [x] 3.2 Creer migration SQL V3__init_piste_audit.sql
- [x] 3.3 Creer AuditEvent dans common-lib (champs : userId, timestamp, service, action, oldValue, newValue, entityId)
- [x] 3.4 Ajouter topic sic.audit dans KafkaTopics.java (common-lib)
- [x] 3.5 Creer annotation @Auditable dans common-lib (optionnelle, pour methodes specifiques)
- [x] 3.6 Creer AuditAspect.java dans common-lib (@Around @RestController, async Kafka publish, skip /actuator/**)
- [x] 3.7 Creer AuditAutoConfiguration.java dans common-lib (@Configuration, @EnableAspectJAutoProxy)
- [x] 3.8 Creer AuditKafkaConsumer dans reporting-admin-service (consomme sic.audit, persiste PisteAudit)
- [x] 3.9 Creer AuditJpaAdapter + AuditJpaRepository dans reporting-admin-service
- [x] 3.10 Ajouter methodes de consultation dans AdminUseCase (findByAction, findByUser, findByDateRange, findByService)
- [x] 3.11 Implementer la consultation dans AdminServiceImpl
- [x] 3.12 Creer AuditController (GET /api/admin/audit avec parametres de filtre action, userId, dateDebut, dateFin, service)
- [x] 3.13 Ajouter script init topic sic.audit dans infra/kafka/

## 4. Phase 2 -- UC-01 Registration transactionnelle

- [x] 4.1 Modifier AuthController.register dans gateway : etape 1 = Keycloak, etape 2 = POST /api/patients
- [x] 4.2 Ajouter rollback Keycloak (DELETE user) en cas d'echec creation Patient
- [x] 4.3 Ajouter log critique en cas d'echec du rollback
- [x] 4.4 Gerer le timeout Keycloak avec fallback 504

## 5. Phase 2 -- UC-08 QR check-in

- [x] 5.1 Ajouter methode checkinByQr(String qrData) dans CheckInUseCase
- [x] 5.2 Implementer validation RDV (existe, statut CONFIRME, date du jour)
- [x] 5.3 Implementer changement statut RDV HONORE + ajout file d'attente
- [x] 5.4 Ajouter endpoint POST /api/queue/checkin-qr dans QueueController
- [x] 5.5 Gerer cas d'erreur : QR invalide (400), RDV expire (400), deja honore (409)

## 6. Phase 2 -- UC-13 Temps d'attente estime

- [x] 6.1 Ajouter champ tempsEstime dans l'entite FileAttente (non persiste, calcule)
- [x] 6.2 Creer migration V2__add_temps_estime.sql (ajout colonne temps_estime_minutes)
- [x] 6.3 Modifier CheckInUseCase pour calculer tempsEstime = (position-1) * DUREE_CONSULTATION_DEFAUT
- [x] 6.4 Appeler API parametrage pour lire DUREE_CONSULTATION_DEFAUT (feign client ou RestTemplate)
- [x] 6.5 Modifier reponse GET /api/queue/{patientId}/position pour inclure personnesDevant et tempsEstime
- [x] 6.6 Ajouter attribut personnesDevant dans la reponse de position

## 7. Phase 2 -- UC-20 Upload fichiers + visibilite resultats

- [x] 7.1 Ajouter champ fichiers (TEXT/JSON) dans l'entite DemandeAnalyse
- [x] 7.2 Creer migration V2__add_fichiers.sql (colonne fichiers dans demandes_analyse)
- [x] 7.3 Creer configuration Spring pour upload multipart (taille max 10 Mo)
- [x] 7.4 Ajouter endpoint POST /api/labo/demandes/{id}/fichiers (multipart/form-data)
- [x] 7.5 Implementer stockage local dans uploads/labo/{demandeId}/
- [x] 7.6 Valider les formats acceptes (JPG, PNG, DICOM, PDF) et rejeter les autres (400)
- [x] 7.7 Rejeter les fichiers de plus de 10 Mo (413)
- [x] 7.8 Ajouter controle d'acces dans LaboController : extraire X-User-Id, comparer avec medecinPrescripteurId et patientId
- [x] 7.9 Autoriser l'acces si X-User-Id = medecinPrescripteurId OU patientId OU role ADMIN
- [x] 7.10 Retourner 403 pour tout autre utilisateur

## 8. Phase 2 -- UC-24 Auto-generation facture

- [x] 8.1 Ajouter consumer sic.laboratoire dans FacturationKafkaConsumer (ecouter statut VALIDE)
- [x] 8.2 Ajouter consumer sic.pharmacie dans FacturationKafkaConsumer (ecouter statut DELIVREE)
- [x] 8.3 Implementer logique : si facture EN_ATTENTE existe pour patientId+consultationId ajouter ligne, sinon creer nouvelle facture
- [x] 8.4 Ajouter methode addLigneToFacture dans GenerateInvoiceUseCase
- [x] 8.5 Recalculer le total de la facture apres chaque ajout de ligne

## 9. Phase 2 -- UC-25 Immutabilite paiement

- [x] 9.1 Ajouter verification statut PAYEE dans ProcessPaymentUseCase avant toute modification
- [x] 9.2 Lever BusinessException(409, "Ce paiement est valide et ne peut plus etre modifie") si statut PAYEE
- [x] 9.3 Bloquer DELETE /api/caisse/{id} dans PaiementController si paiement PAYEE
- [x] 9.4 Bloquer PUT /api/caisse/{id} dans PaiementController si paiement PAYEE

## 10. Phase 2 -- UC-27 Tous les declencheurs de notification

- [x] 10.1 Ajouter handler sic.patient (CREATE) dans KafkaNotificationConsumer notification accueil
- [x] 10.2 Ajouter handler sic.rdv (CONFIRME) notification patient
- [x] 10.3 Ajouter handler sic.file-attente (position change) notification patient
- [x] 10.4 Ajouter handler sic.prescription (EXAMEN) notification laboratoire
- [x] 10.5 Ajouter handler sic.laboratoire (VALIDE) notification medecin + patient
- [x] 10.6 Ajouter handler sic.consultation (TERMINEE) notification patient
- [x] 10.7 Ajouter handler sic.facturation (CREATE) notification patient
- [x] 10.8 Ajouter handler sic.facturation (PAYEE) notification patient
- [x] 10.9 Ajouter handler sic.pharmacie (DELIVREE) notification patient
- [x] 10.10 Implementer fallback canal secondaire (WhatsApp echec SMS) dans NotificationServiceImpl
- [x] 10.11 Logger echec total sans bloquer le flux

## 11. Phase 3 -- Tests unitaires common-lib

- [x] 11.1 Creer DtoValidationTest.java (validation beans PatientDto, RdvDto, ConsultationDto, PrescriptionDto, FactureDto, FileAttenteDto, NotificationDto)
- [x] 11.2 Creer EnumSerializationTest.java (serialisation/deserialisation JSON de tous les enums)
- [x] 11.3 Creer ExceptionHandlerTest.java (GlobalExceptionHandler, BusinessException, ResourceNotFoundException, ErrorResponse)

## 12. Phase 3 -- Tests unitaires gateway

- [x] 12.1 Creer AuthControllerTest.java (login succes, login echec, register transactionnel avec rollback, JWT claims)
- [x] 12.2 Creer SecurityConfigTest.java (acces public /api/auth/**, prive, CORS, rate limiting)

## 13. Phase 3 -- Tests unitaires patient-identity-service

- [x] 13.1 Creer CreatePatientUseCaseTest.java (creation, dedoublonnage, evenement Kafka)
- [x] 13.2 Creer SearchPatientUseCaseTest.java (recherche par nom, telephone, pagination, liste vide)
- [x] 13.3 Creer UpdatePatientUseCaseTest.java (mise a jour, suppression, evenement Kafka)
- [x] 13.4 Creer PatientControllerTest.java (MockMvc : POST/GET/PUT/DELETE, 201/200/404)

## 14. Phase 3 -- Tests unitaires dme-service

- [x] 14.1 Creer GetDmeUseCaseTest.java (consultation DME, ajout antecedents, notes, documents)
- [x] 14.2 Creer UpdateDmeUseCaseTest.java (mise a jour DME)
- [x] 14.3 Creer DmeControllerTest.java (MockMvc : GET/POST DME, FHIR Patient)

## 15. Phase 3 -- Tests unitaires rdv-service

- [x] 15.1 Creer CreateRdvUseCaseTest.java (creation RDV, conflit creneau 409, QR code)
- [x] 15.2 Creer GetCreneauxUseCaseTest.java (disponibilite creneaux par service/date)
- [x] 15.3 Creer QrCodeServiceTest.java (generation et decodage QR code ZXing)
- [x] 15.4 Creer RdvControllerTest.java (MockMvc : POST/GET/PUT/DELETE RDV, QR, FHIR Appointment)

## 16. Phase 3 -- Tests unitaires fileattente-service

- [x] 16.1 Creer CheckInUseCaseTest.java (check-in standard, checkin-qr, position, temps estime)
- [x] 16.2 Creer CallNextUseCaseTest.java (appel patient suivant, file vide 404)
- [x] 16.3 Creer QueueControllerTest.java (MockMvc : checkin, checkin-qr, position, call-next, Redis mock)

## 17. Phase 3 -- Tests unitaires consultation-service

- [x] 17.1 Creer CreateConsultationUseCaseTest.java (creation, constantes, diagnostic, evenement Kafka)
- [x] 17.2 Creer TerminateConsultationUseCaseTest.java (terminaison, evenements, RDV HONORE)
- [x] 17.3 Creer ConsultationControllerTest.java (MockMvc : POST/GET/PUT, terminer, FHIR Observation)

## 18. Phase 3 -- Tests unitaires prescription-service

- [x] 18.1 Creer CreatePrescriptionUseCaseTest.java (prescription EXAMEN route labo, MEDICAMENT route pharma, Kafka)
- [x] 18.2 Creer PrescriptionControllerTest.java (MockMvc : POST/GET prescriptions)

## 19. Phase 3 -- Tests unitaires laboratoire-service

- [x] 19.1 Creer ProcessAnalysisUseCaseTest.java (reception demande, saisie resultats)
- [x] 19.2 Creer ValidateResultUseCaseTest.java (validation, evenement Kafka)
- [x] 19.3 Creer LaboControllerTest.java (MockMvc : GET demandes, POST resultats/fichiers/valider, controle acces, FHIR DiagnosticReport)

## 20. Phase 3 -- Tests unitaires pharmacie-service

- [x] 20.1 Creer DeliverMedicationUseCaseTest.java (delivrance, decrement stock FEFO, evenement Kafka)
- [x] 20.2 Creer ManageStockUseCaseTest.java (CRUD stock, alertes peremption, seuil bas)
- [x] 20.3 Creer PharmaControllerTest.java (MockMvc : GET ordonnances/stock, POST delivrer/addStock)

## 21. Phase 3 -- Tests unitaires facturation-service

- [x] 21.1 Creer GenerateInvoiceUseCaseTest.java (generation facture, lignes, auto-generation sur labo+pharma, calcul total)
- [x] 21.2 Creer FacturationControllerTest.java (MockMvc : POST generer, GET factures)

## 22. Phase 3 -- Tests unitaires caisse-service

- [x] 22.1 Creer ProcessPaymentUseCaseTest.java (paiement Mobile Money, especes, carte, recu, immutabilite)
- [x] 22.2 Creer PaiementControllerTest.java (MockMvc : POST payer, GET paiements, DELETE/PUT bloques si PAYEE)

## 23. Phase 3 -- Tests unitaires accueil-service

- [x] 23.1 Creer RegisterAdmissionUseCaseTest.java (admission CONSULTATION, URGENCE, HOSPITALISATION, evenement Kafka)
- [x] 23.2 Creer AdmissionControllerTest.java (MockMvc : POST admission)

## 24. Phase 3 -- Tests unitaires hospitalisation-service

- [x] 24.1 Creer ManageHospitalisationUseCaseTest.java (admission avec lit, sortie, gestion lits, conflit lit 409)
- [x] 24.2 Creer HospitalisationControllerTest.java (MockMvc : POST admettre/sortir, GET lits/disponibles)

## 25. Phase 3 -- Tests unitaires urgences-service

- [x] 25.1 Creer ProcessTriageUseCaseTest.java (triage 5 niveaux, orientation, evenement Kafka)
- [x] 25.2 Creer UrgencesControllerTest.java (MockMvc : POST triage, GET file)

## 26. Phase 3 -- Tests unitaires soins-service

- [x] 26.1 Creer SoinsServiceImplTest.java (plan de soins, administration, non-administration avec motif)
- [x] 26.2 Creer SoinsControllerTest.java (MockMvc : POST/GET soins, administrer, non-administre)

## 27. Phase 3 -- Tests unitaires bloc-service

- [x] 27.1 Creer BlocServiceImplTest.java (interventions chirurgicales, conflit salle 409, statuts)
- [x] 27.2 Creer BlocControllerTest.java (MockMvc : POST/GET interventions)

## 28. Phase 3 -- Tests unitaires notification-service

- [x] 28.1 Creer NotificationServiceImplTest.java (envoi WhatsApp, SMS, Email, Push, historique)
- [x] 28.2 Creer KafkaNotificationConsumerTest.java (consommation de tous les topics, fallback canal)

## 29. Phase 3 -- Tests unitaires reporting-admin-service

- [x] 29.1 Creer AdminServiceImplTest.java (CRUD utilisateurs, services, actes, tarifs)
- [x] 29.2 Creer CrmServiceImplTest.java (interactions patient, fidelite)
- [x] 29.3 Creer ReportingServiceImplTest.java (6 dashboards, filtres date)
- [x] 29.4 Creer ParametrageSystemeTest.java (CRUD parametres, valeurs par defaut, validation, cle non-modifiable)
- [x] 29.5 Creer AuditTrailTest.java (consultation pistes audit, filtres, pagination)
- [x] 29.6 Creer AuditAspectTest.java (interception requetes REST, exclusion health, async publish)
- [x] 29.7 Creer AdminControllerTest.java (MockMvc : CRUD utilisateurs/services/actes/tarifs)
- [x] 29.8 Creer ReportingControllerTest.java (MockMvc : GET 6 dashboards)

## 30. Phase 4 -- Collection Postman

- [x] 30.1 Creer HACNATION.postman_collection.json avec 22 dossiers (~80 endpoints)
- [x] 30.2 Creer HACNATION.postman_environment.json (base_url, username, password, token, patient_id, rdv_id, consultation_id)
- [x] 30.3 Ajouter pre-request script (auto-login, stockage token dans variable collection)
- [x] 30.4 Ajouter tests post-response (verification status 200/201, presence champs obligatoires)
- [x] 30.5 Ajouter descriptions et exemples de body pour chaque endpoint

## 31. Phase 5 -- Integration & CI

- [x] 31.1 Ajouter init topic sic.audit dans le script Kafka (infra/kafka/)
- [x] 31.2 Mettre a jour docker-compose.yml si necessaire (variable KAFKA_CFG_AUTO_CREATE_TOPICS)
- [x] 31.3 Creer script PowerShell demo-end-to-end.ps1 (equivalent du demo-end-to-end.sh)
- [x] 31.4 Creer workflow GitHub Actions .github/workflows/ci.yml (mvn verify, cache, JaCoCo report)
- [x] 31.5 Verifier que mvn verify passe sur tous les modules (clean install, tests, JaCoCo 70%)
