## ADDED Requirements

### Requirement: Intercepteur AOP global de tracabilite
Chaque requete REST entrante dans tout microservice SHALL etre tracee automatiquement via un aspect AOP defini dans common-lib, sans intervention explicite du code applicatif.

#### Scenario: Trace d'une creation reussie
- **WHEN** une requete POST aboutit avec statut 2xx
- **THEN** un AuditEvent contenant userId, timestamp, service, action=CREATE, newValue, entityId est publie sur le topic Kafka `sic.audit`

#### Scenario: Trace d'une modification avec ancienne valeur
- **WHEN** une requete PUT aboutit avec statut 2xx
- **THEN** un AuditEvent contenant oldValue (etat avant modification) et newValue (etat apres) est publie

#### Scenario: Trace d'une suppression
- **WHEN** une requete DELETE aboutit avec statut 2xx
- **THEN** un AuditEvent contenant oldValue et action=DELETE est publie

#### Scenario: Non-bloquant en cas d'echec Kafka
- **WHEN** la publication Kafka echoue
- **THEN** la reponse HTTP est quand meme retournee au client sans erreur, et l'erreur est loggee

#### Scenario: Exclusion des endpoints de sante
- **WHEN** une requete GET /actuator/health est traitee
- **THEN** aucune trace d'audit n'est generee

### Requirement: Consultation des pistes d'audit
L'administrateur SHALL pouvoir consulter et filtrer l'historique complet des actions via un endpoint dedie.

#### Scenario: Filtrer par type d'action
- **WHEN** l'administrateur envoie GET /api/admin/audit?action=DELETE
- **THEN** le systeme retourne la liste paginee de toutes les suppressions

#### Scenario: Filtrer par utilisateur
- **WHEN** l'administrateur envoie GET /api/admin/audit?userId=xxx
- **THEN** le systeme retourne toutes les actions de cet utilisateur

#### Scenario: Filtrer par plage de dates
- **WHEN** l'administrateur envoie GET /api/admin/audit?dateDebut=2026-01-01&dateFin=2026-01-31
- **THEN** le systeme retourne les actions de la periode specifiee

#### Scenario: Filtrer par service
- **WHEN** l'administrateur envoie GET /api/admin/audit?service=patient-identity
- **THEN** le systeme retourne les actions du service patient-identity

#### Scenario: Combinaison de filtres
- **WHEN** l'administrateur combine plusieurs filtres (action + userId + date)
- **THEN** le systeme applique tous les criteres simultanement (ET logique)

### Requirement: Structure de l'entree d'audit
Chaque entree d'audit persiste SHALL contenir les champs obligatoires definis par le cahier des charges.

#### Scenario: Champs obligatoires presents
- **WHEN** une piste d'audit est consultee
- **THEN** elle contient obligatoirement : userId (Qui), timestamp (Quand), service (Ou), action (Quoi), oldValue, newValue, entityId (Reference)
