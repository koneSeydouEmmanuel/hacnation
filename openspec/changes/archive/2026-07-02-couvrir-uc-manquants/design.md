## Context

Le backend HACNATION implemente compte 17 microservices Spring Boot 3.4 / Java 21 communiquant via Kafka, avec Keycloak pour l'authentification, PostgreSQL (18 schemas) + Redis, et HAPI FHIR 7.6.1. L'analyse de couverture des 29 cas d'utilisation du cahier des charges a revele 2 UC totalement absents et 7 UC partiellement couverts. Le backend est egalement depourvu de tests unitaires (zero fichier de test) et de collection Postman.

Ce design couvre les decisions techniques pour completer la couverture fonctionnelle, ajouter l'infrastructure de test, et produire la collection Postman.

## Goals / Non-Goals

**Goals:**
- Implementer UC-05 (Parametrage systeme) dans reporting-admin-service
- Implementer UC-28 (Audit Trail global) via AOP dans common-lib + persistance dans reporting-admin-service
- Completer les 7 UC partiels dans les services existants
- Ajouter spring-boot-starter-test + H2 + Testcontainers + JaCoCo aux 22 modules Maven
- Ecrire ~46 fichiers de test couvrant use cases, controllers et repositories
- Creer une collection Postman exhaustive (~80 endpoints dans 22 dossiers)
- Ajouter le workflow CI GitHub Actions (build + tests + JaCoCo)

**Non-Goals:**
- Modifier l'architecture microservices existante (17 services conserves)
- Remplacer Keycloak par JWT stateless (Keycloak est deja en place)
- Ajouter de nouveaux services (tout s'integre dans les services existants)
- Tests end-to-end multi-services (complexite excessive pour le perimetre)
- Deploiement Kubernetes ou production

## Decisions

### D1: Audit via AOP Spring dans common-lib (vs intercepteur par service)
**Decision**: Un aspect AOP unique dans common-lib (`@Around("@within(RestController)")`) trace toutes les requetes REST de tous les services automatiquement.
**Rationnel**: Un intercepteur par service dupliquerait le code 17 fois et necessiterait des modifications dans chaque module. L'AOP dans common-lib garantit une couverture uniforme et zero cout d'integration pour les services existants. L'annotation `@RestController` est le point d'entree universel.
**Alternatives considerees**:
- Filtre HTTP par service : trop intrusif, modification de chaque SecurityConfig
- Intercepteur Kafka-only : ne couvre pas les operations synchrones
- Aspect Hibernate (EntityListener) : ne couvre que la persistance, pas la logique metier

### D2: Audit non-bloquant, publication Kafka asynchrone
**Decision**: La publication de l'AuditEvent sur Kafka est asynchrone et non-bloquante. En cas d'echec Kafka, l'erreur est loggee mais la reponse HTTP est retournee normalement.
**Rationnel**: L'audit est une preoccupation transverse qui ne doit jamais degrader la performance ou la disponibilite des endpoints metier. Le pattern fire-and-forget via `CompletableFuture.runAsync()` ou `@Async` est approprie.
**Alternatives considerees**:
- Synchrone avec retry : risque de latence et de timeout en cascade
- Batch periodic : perte d'audit en cas de crash entre deux batchs

### D3: Parametrage dans reporting-admin-service (meme base que les autres referentiels)
**Decision**: L'entite `ParametrageSysteme` est ajoutee dans reporting-admin-service, aux cotes de `ServiceMedical`, `ActeMedical`, `Tarif` et `Utilisateur`.
**Rationnel**: Le parametrage est un referentiel systeme comme les autres, gere par l'administrateur. Le regrouper dans le meme service evite un 18e microservice pour une table de 10-15 lignes. Les autres services lisent les parametres via appel REST (GET /api/admin/parametres/{cle}) — acceptable pour des donnees peu volatiles.
**Alternatives considerees**:
- Nouveau service dedie : trop lourd pour 1 table
- Fichier de configuration statique : pas modifiable sans redeploiement
- Cache Redis : complexite supplementaire non justifiee pour des lectures rares

### D4: Fichiers resultats en stockage local (pas MinIO/S3)
**Decision**: Les fichiers uploades (images medicales, PDF) sont stockes dans un dossier local `uploads/labo/` sur le conteneur du laboratoire-service.
**Rationnel**: Pour un prototype, le stockage local est suffisant. Le champ `fichiers` dans `DemandeAnalyse` stocke les chemins relatifs en JSON. La migration vers un stockage objet (MinIO) serait triviale ulterieurement.
**Alternatives considerees**:
- MinIO : conteneur supplementaire, non justifie pour un prototype
- Base64 dans la BDD : gonfle la base, lent
- Volume Docker partage : complexite de montage non necessaire

### D5: Saga manuel pour la registration (pas d'orchestrateur)
**Decision**: La registration transactionnelle dans `AuthController.register` suit un pattern Saga manuel : creation Keycloak → creation Patient → si echec Patient, suppression Keycloak.
**Rationnel**: Seulement 2 etapes, pas besoin d'un orchestrateur (Camunda, Temporal) qui ajouterait une complexite disproportionnee. Le rollback est simple : `DELETE /admin/realms/hacnation/users/{id}` via l'API admin Keycloak.
**Alternatives considerees**:
- Orchestrateur Saga : overkill pour 2 etapes
- Outbox pattern : complexite inutile, les 2 etapes sont synchrones (HTTP)
- Transaction distribuee 2PC : non supporte par Keycloak + PostgreSQL

### D6: Calcul temps estime via parametre systeme
**Decision**: Le temps d'attente estime est calcule comme `(position - 1) * DUREE_CONSULTATION_DEFAUT`, ou `DUREE_CONSULTATION_DEFAUT` est lu depuis l'API de parametrage a chaque appel.
**Rationnel**: Simple, configurable par l'administrateur sans redeploiement. La lecture a chaque appel est acceptable vu la faible frequence (consultation de position ~1/min/patient max).
**Alternatives considerees**:
- Cache local avec TTL : optimisation prematuree
- Moyenne mobile des temps reels : complexe, necessite un historique

### D7: Facturation evolutive (ajout de lignes)
**Decision**: Une facture peut evoluer : creee a la fin de consultation, puis enrichie avec des lignes d'examen et de medicament au fur et a mesure de leur validation.
**Rationnel**: Le patient peut avoir des examens et medicaments valides apres la fin de sa consultation. Plutot que de creer plusieurs factures, une seule facture consolidee est preferable.
**Alternatives considerees**:
- Une facture par acte : fragmentation excessive
- Facture finale uniquement en fin de parcours : le patient ne peut pas payer par etape

### D8: Immutabilite au niveau use case (pas base de donnees)
**Decision**: La regle d'immutabilite du paiement est implementee dans `ProcessPaymentUseCase` et `PaiementController`, pas via un trigger SQL ou une contrainte.
**Rationnel**: Retourner un code 409 avec un message explicite est plus maintenable et testable qu'un trigger SQL. La regle reste enfoncee au niveau applicatif pour les tests unitaires.
**Alternatives considerees**:
- Trigger PostgreSQL : difficile a tester, messages d'erreur opaques
- Colonne `verrouillee` : redondant avec le statut PAYEE

### D9: Fallback canal secondaire pour les notifications
**Decision**: En cas d'echec du canal primaire (WhatsApp), le systeme tente le canal secondaire (SMS) avant d'abandonner.
**Rationnel**: En Cote d'Ivoire, WhatsApp peut etre intermittent selon la connectivite. Le SMS a une meilleure deliverabilite. Le fallback est simple : try/catch avec canal primaire → secondaire.
**Alternatives considerees**:
- Retry sur le meme canal : ne resout pas le probleme de deliverabilite
- Queue de retry : complexite Kafka supplementaire

### D10: Tests unitaires par couche (use case, controller, repository)
**Decision**: Chaque service recoit 2-5 fichiers de test organises en `unit/` (Mockito, tests de use case et domain model) et `integration/` (H2, tests de controller via MockMvc et repository via @DataJpaTest).
**Rationnel**: La separation unit/integration suit les best practices Spring Boot. Les tests unitaires (Mockito) sont rapides et valident la logique metier. Les tests d'integration (H2) valident le wiring Spring et les requetes HTTP. Pas de Testcontainers PostgreSQL pour les tests unitaires (H2 suffisant), mais Testcontainers disponible pour les tests Kafka si necessaire.
**Alternatives considerees**:
- Testcontainers pour tous les tests : lent, lourd en CI
- Tests 100% unitaires : ne verifient pas le mapping JPA/JSON
- Tests 100% integration : lent, mauvais feedback

### D11: Collection Postman avec pre-request script d'authentification
**Decision**: La collection Postman inclut un pre-request script au niveau de la collection qui obtient un token JWT via `POST /api/auth/login` et le stocke dans la variable `{{token}}`. Tous les endpoints utilisent `Authorization: Bearer {{token}}`.
**Rationnel**: Evite a l'utilisateur de copier-coller manuellement le token. Les credentials sont definis dans l'environnement Postman (`username`, `password`). Un seul appel login est fait, le token est reutilise pour toutes les requetes suivantes.
**Alternatives considerees**:
- Token hardcode : expire, non reproductible
- OAuth2 flow complet : complexe, Keycloak n'est pas toujours accessible

### D12: CI GitHub Actions avec cache Maven
**Decision**: Le workflow CI execute `mvn verify` (compilation + tests + JaCoCo) avec cache du repository Maven local. Le seuil JaCoCo est fixe a 70% de couverture de branches.
**Rationnel**: Le cache Maven reduit le temps de build de ~8min a ~2min. 70% est un seuil realiste pour un projet existant auquel on ajoute des tests (les classes DTO/config ne sont pas testees individuellement).
**Alternatives considerees**:
- 80% seuil : trop ambitieux pour un premier passage
- Pas de seuil : pas d'incitation a maintenir la couverture

## Risks / Trade-offs

| Risque | Mitigation |
|--------|-----------|
| R1: L'AOP peut interferer avec les appels internes (@Async, @Scheduled) | Filtrer uniquement les methodes publiques des classes annotees @RestController |
| R2: Le stockage local des fichiers est perdu au redeploiement du conteneur | Documente comme limitation prototype. Volume Docker mountable en production. |
| R3: H2 peut avoir des differences de comportement vs PostgreSQL (types, fonctions) | Les tests d'integration utilisent H2 en mode PostgreSQL (`MODE=PostgreSQL`). Les tests complexes peuvent utiliser Testcontainers. |
| R4: Le saga registration a une fenetre d'inconsistance (Keycloak cree, Patient pas encore) | La fenetre est de l'ordre de la milliseconde. Le rollback est immediat en cas d'echec. |
| R5: L'ajout de dependances de test a tous les POMs est fastidieux (22 modules) | Script bash ou maven property dans le parent POM pour heriter automatiquement |
| R6: La collection Postman peut se desynchroniser des APIs reelles | Les endpoints sont documentes avec les chemins exacts des controllers. Un export OpenAPI pourrait automatiser la synchro ulterieurement. |

## Migration Plan

1. **Phase 0** (pre-requis) : Mise a jour du parent POM et des 22 POMs avec les dependances de test. Ajout des `src/test/` et `application-test.yml`. Configuration JaCoCo.
2. **Phase 1** (fondations) : Implementation de UC-05 (parametrage) et UC-28 (audit) — nouvelles entites, migrations, controllers, aspect AOP, topic Kafka.
3. **Phase 2** (partiels) : Completion des 7 UC dans les services existants — modifications isolees, pas d'impact sur les autres services.
4. **Phase 3** (tests) : Ecriture des ~46 fichiers de test — peut etre parallelisee par service.
5. **Phase 4** (Postman) : Creation de la collection et de l'environnement — independant du code.
6. **Phase 5** (CI) : Workflow GitHub Actions + verification JaCoCo 70%.

**Rollback**: Chaque phase est independante. Un rollback consiste a annuler les commits de la phase concernee. Les entites/migrations ajoutees sont additives (pas de modification de schema existant).

## Open Questions

- Faut-il un volume Docker pour `uploads/labo/` des maintenant ou plus tard ?
- Le seuil JaCoCo de 70% est-il acceptable ou faut-il viser 80% ?
- La collection Postman doit-elle etre generee automatiquement depuis OpenAPI ou maintenue manuellement ?
