## ADDED Requirements

### Requirement: Notification a chaque evenement metier
Le service de notification SHALL consommer les evenements Kafka de tous les services metier et emettre une notification pour chaque type d'evenement pertinent.

#### Scenario: Patient cree
- **WHEN** un evenement sic.patient de type CREATE est consomme
- **THEN** une notification est emise vers le canal d'accueil : "Nouveau patient {nom} {prenom} enregistre"

#### Scenario: RDV confirme
- **WHEN** un evenement sic.rdv avec statut CONFIRME est consomme
- **THEN** une notification est emise vers le patient : "Votre rendez-vous du {date} a {heure} est confirme. QR code disponible."

#### Scenario: Position dans la file modifiee
- **WHEN** un evenement sic.file-attente indiquant un changement de position est consomme
- **THEN** une notification est emise vers le patient : "Votre position dans la file : {position}. Temps estime : {tempsEstime} min."

#### Scenario: Examens prescrits
- **WHEN** un evenement sic.prescription de type EXAMEN est consomme
- **THEN** une notification est emise vers le laboratoire : "Nouvelle demande d'examen pour {patient} - {typeAnalyse}"

#### Scenario: Resultats disponibles
- **WHEN** un evenement sic.laboratoire avec statut VALIDE est consomme
- **THEN** une notification est emise vers le medecin prescripteur ET le patient : "Resultats de l'examen {typeAnalyse} disponibles"

#### Scenario: Consultation terminee
- **WHEN** un evenement sic.consultation avec statut TERMINEE est consomme
- **THEN** une notification est emise vers le patient : "Votre consultation avec Dr {medecin} est terminee"

#### Scenario: Facture generee
- **WHEN** un evenement sic.facturation de type CREATE est consomme
- **THEN** une notification est emise vers le patient : "Votre facture de {montant} FCFA est disponible"

#### Scenario: Paiement valide
- **WHEN** un evenement sic.facturation avec statut PAYEE est consomme
- **THEN** une notification est emise vers le patient : "Paiement de {montant} FCFA confirme. Merci."

#### Scenario: Ordonnance prete
- **WHEN** un evenement sic.pharmacie avec statut DELIVREE est consomme
- **THEN** une notification est emise vers le patient : "Vos medicaments sont prets a la pharmacie"

### Requirement: Fallback sur canal secondaire
En cas d'echec d'envoi sur le canal primaire, le systeme SHALL tenter le canal secondaire.

#### Scenario: Echec WhatsApp, fallback SMS
- **WHEN** l'envoi WhatsApp echoue (timeout ou echec API)
- **THEN** le systeme tente l'envoi par SMS et logge la degradation

#### Scenario: Echec total logge
- **WHEN** tous les canaux echouent pour une notification
- **THEN** le systeme logge l'echec avec le detail (patientId, canaux tentes, raison) sans bloquer le flux
