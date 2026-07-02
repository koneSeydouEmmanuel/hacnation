## ADDED Requirements

### Requirement: CRUD des parametres systeme
L'administrateur SHALL pouvoir creer, lire, modifier et lister les parametres globaux de l'etablissement via des endpoints REST dedies.

#### Scenario: Creer un parametre
- **WHEN** l'administrateur envoie POST /api/admin/parametres avec {cle, valeur, description}
- **THEN** le systeme cree le parametre et retourne 201 avec l'entite creee

#### Scenario: Modifier un parametre modifiable
- **WHEN** l'administrateur envoie PUT /api/admin/parametres/{cle} avec {valeur}
- **THEN** le systeme met a jour la valeur et retourne 200

#### Scenario: Modifier un parametre non-modifiable
- **WHEN** l'administrateur tente de modifier une cle marquee `modifiable=false`
- **THEN** le systeme retourne 403 FORBIDDEN

#### Scenario: Consulter un parametre
- **WHEN** l'utilisateur envoie GET /api/admin/parametres/{cle}
- **THEN** le systeme retourne 200 avec la valeur, ou 404 si la cle est inexistante

#### Scenario: Lister tous les parametres
- **WHEN** l'utilisateur envoie GET /api/admin/parametres
- **THEN** le systeme retourne 200 avec la liste complete des parametres

### Requirement: Parametres par defaut au demarrage
Le systeme SHALL etre livre avec des parametres pre-configures lors de l'initialisation de la base de donnees.

#### Scenario: Valeurs initiales presentes
- **WHEN** l'application demarre pour la premiere fois
- **THEN** les parametres suivants existent : HORAIRES_OUVERTURE="08:00-18:00", QUOTA_RDV_PAR_CRENEAU="5", DUREE_CONSULTATION_DEFAUT="30", DELAI_RAPPEL_RDV_HEURES="24", SEUIL_ALERTE_STOCK="10"

### Requirement: Validation des valeurs
Le systeme SHALL valider le format des valeurs selon le type du parametre.

#### Scenario: Rejet d'une valeur invalide
- **WHEN** l'administrateur tente de definir QUOTA_RDV_PAR_CRENEAU avec une valeur non numerique
- **THEN** le systeme retourne 400 BAD REQUEST avec un message d'erreur explicite
