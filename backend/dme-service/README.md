# Service Dossier Médical Électronique (DME)

## À quoi ça sert ?

C'est le **dossier médical numérique** de chaque patient. Il contient tout son historique de santé.

## Ce qu'il fait

- **Consulter le dossier médical** d'un patient
- **Ajouter des antécédents** : maladies passées, allergies, chirurgies...
- **Ajouter des notes cliniques** : observations des médecins
- **Recevoir les résultats** : analyses de laboratoire, ordonnances exécutées
- **Exposer les données en format FHIR** : standard international d'échange de données de santé

## Exemple concret

Quand le Dr Konan reçoit M. Kouadio en consultation :
1. Il ouvre le DME → voit les antécédents (appendicectomie en 2010, allergie à la pénicilline)
2. Après la consultation, sa note clinique est ajoutée automatiquement
3. Quand le laboratoire termine les analyses, les résultats apparaissent dans le DME
4. Quand la pharmacie délivre les médicaments, c'est noté dans le DME

## Qu'est-ce que FHIR ?

FHIR (Fast Healthcare Interoperability Resources) est le **standard international** pour échanger des données de santé. Ce service expose les dossiers patients au format FHIR, ce qui permet à d'autres hôpitaux ou applications de santé de lire ces données de façon standardisée.
