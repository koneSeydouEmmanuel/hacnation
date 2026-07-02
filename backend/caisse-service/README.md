# Service Caisse & Mobile Money

## À quoi ça sert ?

C'est le service qui gère les **paiements**. Il permet de payer une facture et garde l'historique de toutes les transactions.

## Ce qu'il fait

- **Encaisser un paiement** : le caissier enregistre le règlement d'une facture
- **Simuler le Mobile Money** : Orange Money, MTN Money, Wave (simulation pour le prototype)
- **Générer un reçu** : après paiement, un reçu est émis
- **Historique des paiements** : toutes les transactions sont enregistrées
- **Résumé de caisse** : total des encaissements par jour et par mode de paiement

## Exemple concret

M. Kouadio va payer sa facture de 21 950 FCFA :
1. Le caissier consulte la facture
2. M. Kouadio paie par Orange Money
3. Le caissier saisit : mode = ORANGE_MONEY, téléphone = 0701020304, montant = 21950
4. Le système simule le paiement Mobile Money → succès
5. Référence de transaction : TXN-ABC12345
6. La facture passe au statut PAYÉE
7. Un reçu est généré

## Modes de paiement supportés

- **Espèces** : paiement en liquide
- **Orange Money** : mobile money Orange (simulé)
- **MTN Money** : mobile money MTN (simulé)
- **Wave** : mobile money Wave (simulé)
- **Carte bancaire** : paiement par carte

## Résumé de caisse

En fin de journée, le caissier peut voir :
- Nombre de factures payées
- Total des encaissements
- Détail par mode de paiement
