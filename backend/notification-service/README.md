# Service Notifications

## À quoi ça sert ?

C'est le service qui gère l'**envoi de notifications** aux patients et au personnel. Il écoute tous les événements importants et prévient les personnes concernées.

## Ce qu'il fait

- **Écouter les événements** : naissance d'un patient, rendez-vous, résultats d'analyse, paiement...
- **Simuler l'envoi** : WhatsApp, SMS, Email, Push notification (simulation pour le prototype)
- **Historique** : toutes les notifications envoyées sont conservées

## Exemple concret

Quand le labo valide les résultats de M. Kouadio :
1. Le service reçoit l'événement "Résultats disponibles"
2. Il prépare un message : "Vos résultats d'analyse sont disponibles. Connectez-vous pour les consulter."
3. Il simule l'envoi par WhatsApp au 0701020304
4. La notification est enregistrée dans l'historique

## Canaux de notification

| Canal | Usage |
|-------|-------|
| **WhatsApp** | Très utilisé en Côte d'Ivoire, idéal pour les patients |
| **SMS** | Fonctionne même sans smartphone |
| **Email** | Pour les comptes rendus détaillés |
| **Push** | Notification dans l'application mobile |

## Événements qui déclenchent des notifications

- Rendez-vous confirmé
- Rappel 24h avant le rendez-vous
- Résultats d'analyse disponibles
- Ordonnance prête
- Facture générée
- Paiement confirmé
- Patient appelé dans la file d'attente
