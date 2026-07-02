# Service Rendez-vous & Agenda

## À quoi ça sert ?

C'est le **service de prise de rendez-vous** de la clinique. Il gère l'agenda des praticiens.

## Ce qu'il fait

- **Prendre un rendez-vous** : choisir un service, un praticien, une date et une heure
- **Consulter les créneaux disponibles** : voir les horaires libres par service
- **Générer un QR code** : chaque rendez-vous confirmé reçoit un QR code unique
- **Modifier/Annuler** : changer le statut d'un rendez-vous
- **Envoyer des rappels** : notification automatique 24h avant le rendez-vous
- **Exposer les rendez-vous en format FHIR** : standard international

## Exemple concret

M. Kouadio veut voir un médecin généraliste :
1. L'agent d'accueil consulte les créneaux disponibles du Dr Konan
2. Il choisit le 3 juillet à 9h00
3. Le rendez-vous est créé, un QR code est généré
4. Le patient reçoit son QR code (par email/SMS/WhatsApp)
5. La veille, un rappel automatique est envoyé

## Le QR code

Le QR code contient l'identifiant unique du rendez-vous. En le scannant à son arrivée, le patient fait automatiquement son check-in.
