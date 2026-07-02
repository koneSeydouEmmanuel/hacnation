# Service Hospitalisation

## À quoi ça sert ?

C'est le service qui gère les **séjours des patients hospitalisés** et l'occupation des lits.

## Ce qu'il fait

- **Admettre en hospitalisation** : le patient est installé dans un lit
- **Gérer les lits** : savoir quels lits sont disponibles, occupés ou en maintenance
- **Suivre le séjour** : date d'entrée, motif, évolution
- **Sortie d'hospitalisation** : libérer le lit, clôturer le séjour
- **Notifier la facturation** : pour facturer le séjour

## Exemple concret

Mme Traoré doit être hospitalisée :
1. Le médecin décide l'hospitalisation
2. L'infirmier cherche un lit disponible en médecine → lit 103
3. Admission en hospitalisation : Mme Traoré → lit 103 (statut : OCCUPÉ)
4. Après 3 jours, le médecin autorise la sortie
5. Sortie : le lit 103 redevient DISPONIBLE
6. La facturation est notifiée pour facturer le séjour

## États des lits

| Statut | Signification |
|--------|---------------|
| DISPONIBLE | Lit libre |
| OCCUPÉ | Un patient est dedans |
| MAINTENANCE | Lit en réparation/nettoyage |
| RÉSERVÉ | Lit bloqué pour une arrivée prévue |
