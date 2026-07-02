# Service File d'Attente & QR Code (Module Innovant)

## À quoi ça sert ?

C'est le **service de gestion de la file d'attente** de la clinique. Il remplace la file d'attente physique par une file numérique.

## Ce qu'il fait

- **Check-in** : le patient se présente, scanne son QR code → il entre dans la file
- **Position dans la file** : le patient sait combien de personnes sont avant lui
- **Temps d'attente estimé** : indication du temps restant
- **Appel du patient** : le médecin appelle le prochain patient
- **File par service** : chaque service (consultation, laboratoire, urgences) a sa propre file

## Exemple concret

M. Kouadio arrive à la clinique :
1. Il scanne son QR code à la borne d'accueil → check-in automatique
2. Il est en position 3 dans la file de consultation
3. Temps d'attente estimé : 25 minutes
4. Le médecin appelle "patient suivant" → M. Kouadio est appelé
5. Sa position est libérée, les autres patients avancent

## Pourquoi c'est innovant ?

- Plus de file d'attente physique debout
- Le patient peut attendre assis et voir sa position sur son téléphone
- Le QR code fait le lien entre le rendez-vous et l'arrivée effective
- La clinique sait exactement qui attend, pour quel service, depuis combien de temps
