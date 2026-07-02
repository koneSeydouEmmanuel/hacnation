## Why

L'analyse de couverture des 29 cas d'utilisation (UC-01 a UC-29) du cahier des charges HACNATION face au backend implemente revele **2 UC totalement absents** (parametrage systeme, audit trail global) et **7 UC partiellement couverts** (registration transactionnelle, QR check-in, temps d'attente estime, upload fichiers + visibilite, auto-facturation, immobilite paiement, tous les declencheurs de notification). Le backend est egalement depourvu de tests unitaires (0 fichier de test sur 22 modules Maven) et de collection Postman pour les API.

## What Changes

### UC manquants (absolus)
- **Parametrage systeme (UC-05)** : Nouvelle entite `ParametrageSysteme` dans `reporting-admin-service` pour configurer horaires, quotas, durees par defaut de l'etablissement
- **Audit trail global (UC-28)** : Intercepteur AOP dans `common-lib` tracant chaque requete REST (Qui, Quand, Ou, Quoi, Ancienne/Nouvelle valeur, Reference). Topic Kafka `sic.audit`, persistance dans `reporting-admin-service`

### UC partiels (completion)
- **Self-registration transactionnelle (UC-01)** : Saga manuel Keycloak + Patient dans le gateway
- **Flux QR check-in (UC-08)** : Endpoint dedie `POST /api/queue/checkin-qr` dans `fileattente-service`
- **Temps d'attente estime (UC-13)** : Calcul dynamique base sur la position et `DUREE_CONSULTATION_DEFAUT`
- **Upload fichiers + visibilite (UC-20)** : Televersement fichiers/images + controle acces medecin/patient
- **Auto-generation facture (UC-24)** : Declenchement sur validation examen ET delivrance medicament
- **Immutabilite paiement (UC-25)** : Blocage modification/suppression apres paiement
- **Tous les declencheurs notification (UC-27)** : 10+ handlers dans `KafkaNotificationConsumer`

### Infrastructure qualite
- **Tests unitaires** : Ajout de `spring-boot-starter-test`, H2, Testcontainers, JaCoCo aux 22 modules. Creation d'environ 46 fichiers de test couvrant use cases, controllers, repositories et domain models
- **Collection Postman** : Collection complete avec 22 dossiers, ~80 endpoints, environnement avec variables, pre-request scripts d'authentification

## Capabilities

### New Capabilities

- `parametrage-systeme` : CRUD des parametres globaux de l'etablissement (horaires, quotas, durees). Entite ParametrageSysteme dans reporting-admin-service. Parametres par defaut livres (HORAIRES_OUVERTURE, QUOTA_RDV_PAR_CRENEAU, DUREE_CONSULTATION_DEFAUT, DELAI_RAPPEL_RDV_HEURES, SEUIL_ALERTE_STOCK).
- `audit-trail-global` : Intercepteur AOP dans common-lib tracant chaque requete REST. Publication sur Kafka `sic.audit`, persistance PisteAudit dans reporting-admin-service. Consultation via `GET /api/admin/audit` avec filtres (action, userId, date, service).

### Modified Capabilities

- `api-gateway-auth` : Registration patient transactionnelle avec rollback Keycloak en cas d'echec creation Patient.
- `rdv-fileattente` : Flux QR validaton identite check-in. Calcul temps d'attente estime.
- `labo-pharmacie` : Televersement fichiers/images resultats. Controle acces medecin prescripteur + patient uniquement.
- `facturation-paiement` : Generation facture sur validation examen et delivrance medicament. Immutabilite paiement valide.
- `notification-reporting-admin` : Tous les declencheurs de notification cables dans KafkaNotificationConsumer : patient cree, RDV confirme, position file modifiee, examens prescrits, resultats dispos, consultation terminee, facture generee, paiement valide, ordonnance prete.

## Impact

- **common-lib** : Nouvel aspect AOP `AuditAspect.java`, annotation `@Auditable`, evenement `AuditEvent`, topic `sic.audit`, auto-configuration. Nouvelles dependances test (spring-boot-starter-test, H2).
- **gateway** : Modification `AuthController.java` pour saga Keycloak+Patient.
- **fileattente-service** : Nouvel endpoint `POST /api/queue/checkin-qr`, calcul temps estime, migration `V2__add_temps_estime.sql`.
- **laboratoire-service** : Upload fichiers, controle acces resultats, migration `V2__add_fichiers.sql`.
- **facturation-service** : Nouveaux consumers Kafka (`sic.laboratoire`, `sic.pharmacie`), logique ajout lignes facture.
- **caisse-service** : Verification immutabilite paiement dans `ProcessPaymentUseCase` et `PaiementController`.
- **notification-service** : 10+ handlers ajoutes dans `KafkaNotificationConsumer`, fallback canal secondaire.
- **reporting-admin-service** : Nouvelles entites `ParametrageSysteme` + `PisteAudit`, nouveaux controllers/consumers, migrations SQL.
- **Tous les POMs** : Ajout `spring-boot-starter-test`, `h2` (test scope), `testcontainers`. Plugin JaCoCo.
- **Tous les services** : Creation de `src/test/` avec ~46 fichiers de test.
- **Nouveau fichier** : `postman/HACNATION.postman_collection.json` + `postman/HACNATION.postman_environment.json`.
