## ADDED Requirements

### Requirement: Check-in par scan QR code
L'agent d'accueil SHALL pouvoir valider l'arrivee d'un patient en scannant le QR code de son rendez-vous via un endpoint dedie.

#### Scenario: QR valide et RDV du jour
- **WHEN** l'agent envoie POST /api/queue/checkin-qr avec les donnees du QR code (patientId, rdvId, nom, prenom)
- **THEN** le systeme verifie que le RDV existe, est au statut CONFIRME, et date du jour, puis passe le RDV au statut HONORE, ajoute le patient dans la file d'attente, et retourne 200 avec position et temps estime

#### Scenario: QR code invalide ou corrompu
- **WHEN** l'agent envoie POST /api/queue/checkin-qr avec des donnees QR non reconnues
- **THEN** le systeme retourne 400 "QR code invalide ou corrompu"

#### Scenario: RDV expire (date depassee)
- **WHEN** l'agent scanne un QR dont la date de RDV est anterieure a la date du jour
- **THEN** le systeme retourne 400 "Ce rendez-vous est expire"

#### Scenario: RDV deja honore
- **WHEN** l'agent scanne un QR dont le RDV est deja au statut HONORE
- **THEN** le systeme retourne 409 "Ce patient est deja enregistre"

### Requirement: Calcul du temps d'attente estime
La consultation de la position dans la file d'attente SHALL inclure une estimation du temps d'attente basee sur le parametre systeme DUREE_CONSULTATION_DEFAUT.

#### Scenario: Patient en milieu de file
- **WHEN** un patient consulte GET /api/queue/{patientId}/position et que 3 personnes sont devant lui
- **THEN** le systeme retourne position=4, personnesDevant=3, tempsEstime=3*DUREE_CONSULTATION_DEFAUT minutes

#### Scenario: Patient en premiere position
- **WHEN** le patient est le prochain a etre appele
- **THEN** le systeme retourne position=1, personnesDevant=0, tempsEstime="Moins de 5 minutes" ou 0

#### Scenario: Patient non present dans la file
- **WHEN** le patientId n'est dans aucune file d'attente
- **THEN** le systeme retourne 404 "Patient non trouve dans la file d'attente"
