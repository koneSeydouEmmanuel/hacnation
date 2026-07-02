## ADDED Requirements

### Requirement: API Gateway routes requests to backend services
L'API Gateway SHALL router les requêtes HTTP entrantes vers les microservices appropriés selon le préfixe de chemin.

#### Scenario: Routing par préfixe de chemin
- **WHEN** une requête arrive sur `/api/patients/**`
- **THEN** le Gateway route vers le service patient-dme sur le port 8081

#### Scenario: Routing des requêtes FHIR
- **WHEN** une requête arrive sur `/fhir/**`
- **THEN** le Gateway route vers le service patient-dme qui expose les endpoints FHIR

### Requirement: JWT authentication and authorization
Le Gateway SHALL valider les tokens JWT sur toutes les routes protégées et extraire les claims utilisateur.

#### Scenario: Token JWT valide
- **WHEN** une requête contient un header `Authorization: Bearer <token_valide>`
- **THEN** le Gateway forwarde la requête avec les headers `X-User-Id` et `X-User-Roles`

#### Scenario: Token JWT invalide ou expiré
- **WHEN** une requête contient un token JWT invalide ou expiré
- **THEN** le Gateway retourne HTTP 401 Unauthorized

#### Scenario: Token JWT absent
- **WHEN** une requête sans header Authorization atteint un endpoint protégé
- **THEN** le Gateway retourne HTTP 401 Unauthorized

### Requirement: Role-based access control
Le Gateway SHALL appliquer des règles RBAC basées sur les rôles contenus dans le JWT.

#### Scenario: Accès autorisé par rôle
- **WHEN** un utilisateur avec le rôle MEDECIN accède à `/api/consultations/**`
- **THEN** la requête est forwardée normalement

#### Scenario: Accès refusé par rôle
- **WHEN** un utilisateur avec le rôle PATIENT tente d'accéder à `/api/admin/**`
- **THEN** le Gateway retourne HTTP 403 Forbidden

### Requirement: User login endpoint
Le Gateway SHALL exposer un endpoint de login qui authentifie les utilisateurs et génère un JWT.

#### Scenario: Login réussi
- **WHEN** un utilisateur fournit des credentials valides sur `POST /api/auth/login`
- **THEN** le système retourne HTTP 200 avec un token JWT contenant userId, username, et roles

#### Scenario: Login échoué
- **WHEN** un utilisateur fournit des credentials invalides
- **THEN** le système retourne HTTP 401 avec un message d'erreur

### Requirement: CORS configuration
Le Gateway SHALL supporter les requêtes Cross-Origin pour permettre l'accès depuis les applications frontend.

#### Scenario: Requête preflight CORS
- **WHEN** une requête OPTIONS est envoyée avec les headers CORS appropriés
- **THEN** le Gateway retourne les headers Access-Control-Allow-* adéquats

### Requirement: Rate limiting
Le Gateway SHALL implémenter un rate limiting basique pour protéger les services backend.

#### Scenario: Limite non atteinte
- **WHEN** un client envoie moins de 100 requêtes par minute
- **THEN** toutes les requêtes sont forwardées normalement

#### Scenario: Limite dépassée
- **WHEN** un client dépasse 100 requêtes par minute
- **THEN** le Gateway retourne HTTP 429 Too Many Requests

### Requirement: Health check endpoint
Le Gateway SHALL exposer un endpoint de health check public.

#### Scenario: Health check
- **WHEN** une requête GET est envoyée sur `/actuator/health`
- **THEN** le système retourne HTTP 200 avec le statut UP et les statuts des services downstream
