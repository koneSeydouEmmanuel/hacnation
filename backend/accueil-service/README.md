# Service Accueil & Admission

## À quoi ça sert ?

C'est le service qui gère l'**arrivée des patients** dans l'établissement. C'est la première étape du parcours patient.

## Ce qu'il fait

- **Admettre un patient** : enregistrer son arrivée (consultation, urgence ou hospitalisation)
- **Orienter** : diriger le patient vers le bon service
- **Mode dégradé** : fonctionner même si le réseau est coupé (brouillon sauvegardé)

## Exemple concret

M. Kouadio arrive à la clinique :
1. L'agent d'accueil crée une admission : type = CONSULTATION, service = MÉDECINE GÉNÉRALE
2. Le système enregistre l'arrivée
3. Le patient est orienté vers la salle d'attente de consultation

Si M. Traoré arrive aux urgences :
1. Admission type = URGENCE
2. Elle est orientée vers le service des urgences pour triage

## Types d'admission

- **Consultation** : patient qui vient pour un rendez-vous
- **Urgence** : patient qui arrive sans rendez-vous, en urgence
- **Hospitalisation** : patient qui doit être hospitalisé

## Mode dégradé

Dans les zones où la connexion internet est instable, l'agent d'accueil peut enregistrer l'admission en mode brouillon. Dès que la connexion revient, les données sont synchronisées.
