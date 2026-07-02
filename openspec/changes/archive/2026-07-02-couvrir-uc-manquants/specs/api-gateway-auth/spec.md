## MODIFIED Requirements

### Requirement: Inscription patient transactionnelle
L'inscription d'un patient via POST /api/auth/register SHALL creer un compte Keycloak ET un profil Patient de maniere atomique. En cas d'echec de l'une des deux operations, l'autre est annulee (rollback).

#### Scenario: Inscription reussie
- **WHEN** un patient envoie POST /api/auth/register avec {email, password, nom, prenom, telephone, dateNaissance, sexe}
- **THEN** le systeme cree l'utilisateur Keycloak, cree le Patient dans patient-identity-service, et retourne 201 avec les donnees combinees

#### Scenario: Echec creation Patient declenche rollback Keycloak
- **WHEN** la creation Keycloak reussit mais la creation Patient echoue (ex: doublon detecte)
- **THEN** le systeme supprime l'utilisateur Keycloak cree et retourne 409 avec le message d'erreur

#### Scenario: Echec creation Keycloak preserve le systeme
- **WHEN** la creation Keycloak echoue (ex: email deja utilise)
- **THEN** aucun Patient n'est cree et le systeme retourne 409

#### Scenario: Echec du rollback Keycloak
- **WHEN** la creation Patient echoue et la suppression Keycloak echoue egalement
- **THEN** le systeme logge l'erreur critique et retourne 500 avec instruction de contacter l'administrateur
