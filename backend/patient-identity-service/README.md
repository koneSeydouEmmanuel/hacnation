# Service Identité Patient

## À quoi ça sert ?

C'est le **registre central des patients** de l'établissement. Il gère qui sont les patients.

## Ce qu'il fait

- **Créer un patient** : enregistrer une nouvelle personne (nom, prénom, date de naissance, téléphone, adresse...)
- **Rechercher un patient** : retrouver un patient par son nom, prénom ou numéro de téléphone
- **Modifier un patient** : mettre à jour ses informations
- **Détecter les doublons** : éviter de créer deux fois le même patient

## Exemple concret

À l'accueil de la clinique, l'agent reçoit M. Kouadio pour la première fois :
1. Il recherche "Kouadio" → aucun résultat
2. Il crée une nouvelle fiche avec ses informations
3. Le système vérifie qu'il n'existe pas déjà (même nom + date de naissance)
4. Un message est envoyé aux autres services : "Nouveau patient créé"

## Données gérées

Nom, prénom, date de naissance, sexe, téléphone, email, adresse, groupe sanguin
