## ADDED Requirements

### Requirement: Create appointment
Le système SHALL permettre la création d'un rendez-vous pour un patient auprès d'un service ou praticien.

#### Scenario: Création RDV réussie
- **WHEN** un RDV est créé avec patientId, service, praticienId, dateHeure
- **THEN** le système retourne HTTP 201, un QR code est généré, et un événement Kafka `rdv-events` est publié

#### Scenario: Créneau déjà occupé
- **WHEN** un RDV est créé sur un créneau déjà réservé pour le même praticien
- **THEN** le système retourne HTTP 409 avec message "Créneau non disponible"

### Requirement: View available slots
Le système SHALL permettre la consultation des créneaux disponibles par service et praticien.

#### Scenario: Créneaux disponibles
- **WHEN** une requête est faite sur `GET /api/rdv/creneaux?service=CONSULTATION&date=2026-07-03`
- **THEN** le système retourne la liste des créneaux disponibles pour cette date

### Requirement: Update appointment status
Le système SHALL permettre la mise à jour du statut d'un rendez-vous (CONFIRME, ANNULE, HONORE, ABSENT).

#### Scenario: Annulation RDV
- **WHEN** un RDV est marqué comme ANNULE
- **THEN** le créneau est libéré et un événement Kafka est publié

#### Scenario: RDV honoré
- **WHEN** un RDV est marqué comme HONORE après la consultation
- **THEN** le statut est mis à jour et un événement est publié

### Requirement: QR code generation
Le système SHALL générer un QR code unique pour chaque rendez-vous confirmé.

#### Scenario: QR code généré
- **WHEN** un RDV est créé avec statut CONFIRME
- **THEN** un QR code encodant l'ID du RDV est généré et retourné dans la réponse

#### Scenario: Récupération QR code
- **WHEN** un patient ou agent demande le QR code d'un RDV via `GET /api/rdv/{id}/qr`
- **THEN** le système retourne l'image QR ou les données encodées

### Requirement: Patient check-in via QR
Le système SHALL permettre le check-in d'un patient via scan de son QR code de RDV.

#### Scenario: Check-in réussi
- **WHEN** un QR code valide est scanné pour check-in
- **THEN** le patient est ajouté à la file d'attente avec le statut EN_ATTENTE et un événement `queue-events` est publié

#### Scenario: QR code invalide ou expiré
- **WHEN** un QR code invalide ou d'un RDV annulé est scanné
- **THEN** le système retourne HTTP 400 avec message d'erreur

### Requirement: Queue position and status
Le système SHALL maintenir la position de chaque patient dans la file d'attente.

#### Scenario: Consultation position
- **WHEN** un patient demande sa position via `GET /api/queue/{patientId}/position`
- **THEN** le système retourne la position actuelle et le temps d'attente estimé

#### Scenario: File d'attente par service
- **WHEN** le personnel consulte la file d'attente via `GET /api/queue?service=CONSULTATION`
- **THEN** le système retourne la liste ordonnée des patients en attente

### Requirement: Call patient
Le système SHALL permettre au praticien d'appeler le prochain patient dans la file d'attente.

#### Scenario: Appel patient
- **WHEN** un médecin appelle le prochain patient via `POST /api/queue/call-next`
- **THEN** le statut du patient passe à APPELE, sa position est retirée de la file, et un événement Kafka est publié

#### Scenario: File vide
- **WHEN** un appel est fait sur une file d'attente vide
- **THEN** le système retourne HTTP 404 avec message "Aucun patient en attente"

### Requirement: Appointment via FHIR
Le système SHALL exposer les rendez-vous au format FHIR Appointment.

#### Scenario: FHIR Appointment
- **WHEN** une requête est faite sur `/fhir/Appointment/{id}`
- **THEN** le système retourne une ressource FHIR Appointment valide

### Requirement: Appointment reminders
Le système SHALL publier des événements de rappel avant un rendez-vous.

#### Scenario: Rappel 24h avant
- **WHEN** un job planifié détecte des RDV dans 24h
- **THEN** un événement `notification-events` est publié pour chaque RDV concerné
