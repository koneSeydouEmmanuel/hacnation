# Service Facturation

## À quoi ça sert ?

C'est le service qui **génère les factures** des patients. Il consolide automatiquement tous les actes (consultation, examens, médicaments) en une seule facture.

## Ce qu'il fait

- **Générer une facture** : automatiquement quand une consultation est terminée
- **Consolider les actes** : regrouper tous les frais (consultation + labo + pharmacie)
- **Détailler les lignes** : chaque acte est listé avec son prix
- **Suivre le statut** : en attente de paiement, payée, annulée

## Exemple concret

La consultation de M. Kouadio est terminée :
1. Le service reçoit la notification "Consultation terminée"
2. Il consolide tous les actes :
   - Consultation généraliste : 5 000 FCFA
   - NFS : 3 500 FCFA
   - Glycémie : 2 500 FCFA
   - Sérologie : 5 000 FCFA
   - Amoxicilline 500mg x21 : 3 150 FCFA
   - Oméprazole 20mg x14 : 2 800 FCFA
3. **Total : 21 950 FCFA**
4. La facture est créée, prête pour le paiement

## Avantage clé

Avant, le caissier devait additionner manuellement tous les actes. Maintenant, la facture se génère automatiquement dès que la consultation est terminée.
