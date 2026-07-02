# Service Laboratoire d'Analyses Médicales

## À quoi ça sert ?

C'est le service qui gère toutes les **analyses de laboratoire** : réception des demandes, saisie et validation des résultats.

## Ce qu'il fait

- **Recevoir les demandes** : automatiquement depuis les prescriptions du médecin
- **Saisir les résultats** : le laborantin entre les valeurs mesurées
- **Valider les résultats** : un responsable biologique valide avant diffusion
- **Notifier le médecin** : quand les résultats sont disponibles
- **Mettre à jour le DME** : les résultats validés sont ajoutés au dossier patient
- **Exposer les résultats en format FHIR DiagnosticReport** : standard international

## Exemple concret

Le laboratoire reçoit la demande d'analyse pour M. Kouadio :
1. La demande apparaît dans la liste "En attente"
2. Le laborantin effectue le prélèvement
3. Il saisit les résultats : hémoglobine 14.5 g/dL, glycémie 0.95 g/L...
4. Le responsable valide les résultats
5. Le médecin est notifié : "Résultats disponibles"
6. Les résultats sont automatiquement dans le DME du patient

## Cycle de vie d'une analyse

```
EN_ATTENTE → EN_COURS → TERMINÉE → VALIDÉE
(reçue)     (prélevée)  (saisie)   (validée)
```
