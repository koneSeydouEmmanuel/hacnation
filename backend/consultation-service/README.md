# Service Consultation Médicale

## À quoi ça sert ?

C'est le **cœur de l'acte médical**. Il gère tout ce qui se passe pendant une consultation.

## Ce qu'il fait

- **Créer une consultation** : le médecin ouvre une nouvelle consultation pour un patient
- **Saisir les constantes** : tension artérielle, pouls, température, poids, taille, saturation en oxygène
- **Poser un diagnostic** : le médecin note son diagnostic
- **Rédiger un compte rendu** : résumé de la consultation
- **Clôturer la consultation** : marquer la consultation comme terminée
- **Exposer les constantes en format FHIR Observation** : standard international

## Exemple concret

Le Dr Konan reçoit M. Kouadio en consultation :
1. Il ouvre une nouvelle consultation liée au rendez-vous
2. Il saisit les constantes : TA 12/8, pouls 72, température 37.2°C
3. Après examen, il diagnostique une gastro-entérite aiguë
4. Il rédige son compte rendu
5. Il prescrit des examens et des médicaments (via le service Prescription)
6. Il clôture la consultation → le DME est mis à jour, la facturation est notifiée

## Lien avec les autres services

- Reçoit les rendez-vous honorés du service RDV
- Envoie les prescriptions au service Prescription
- Met à jour le DME du patient
- Notifie la facturation en fin de consultation
