# Service Bloc Opératoire

## À quoi ça sert ?

C'est le service qui gère la **programmation des interventions chirurgicales** et l'occupation des salles d'opération.

## Ce qu'il fait

- **Programmer une intervention** : date, heure, chirurgien, salle, type d'intervention
- **Vérifier les conflits** : éviter de programmer deux interventions dans la même salle au même moment
- **Suivre le statut** : programmée → en cours → terminée
- **Planning par salle** : voir toutes les interventions prévues pour une salle et une date

## Exemple concret

Le Dr Konan doit opérer M. Koné (appendicite) :
1. Il programme l'intervention : salle 301, mercredi 10h-11h
2. Le système vérifie que la salle 301 est libre à ce créneau
3. Intervention créée avec statut PROGRAMMÉE
4. Le jour J, l'intervention passe en statut EN COURS
5. Une fois terminée : statut TERMINÉE

## Gestion des conflits

Si quelqu'un essaie de programmer une autre intervention salle 301 de 10h à 11h le même jour :
→ Le système refuse : "Conflit de salle : salle 301 déjà occupée par Intervention #12345"

## Statuts d'une intervention

| Statut | Signification |
|--------|---------------|
| PROGRAMMÉE | Planifiée, pas encore commencée |
| EN COURS | L'opération est en train de se dérouler |
| TERMINÉE | L'opération est finie |
| ANNULÉE | L'opération a été annulée |
