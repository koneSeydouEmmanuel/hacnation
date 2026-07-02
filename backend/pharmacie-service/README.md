# Service Pharmacie & Gestion des Stocks

## À quoi ça sert ?

C'est le service qui gère la **pharmacie de l'établissement** : réception des ordonnances, délivrance des médicaments et gestion des stocks.

## Ce qu'il fait

- **Recevoir les ordonnances** : automatiquement depuis les prescriptions du médecin
- **Délivrer les médicaments** : le pharmacien prépare et remet les médicaments
- **Gérer le stock** : suivre les quantités disponibles
- **Règle FEFO** : First Expired, First Out — les médicaments qui périment le plus tôt sont délivrés en premier
- **Alerter** : quand un médicament est presque en rupture ou proche de la péremption

## Qu'est-ce que le FEFO ?

C'est une règle de gestion de stock essentielle en pharmacie. Quand on a plusieurs lots du même médicament, on délivre d'abord celui dont la date de péremption est la plus proche. Cela évite le gaspillage.

## Exemple concret

La pharmacie reçoit l'ordonnance pour M. Kouadio :
1. L'ordonnance apparaît dans la liste "En attente"
2. Le pharmacien prépare les médicaments :
   - Amoxicilline 500mg : il prend dans le lot qui périme en août 2026 (FEFO)
   - Oméprazole 20mg : stock OK
3. Il valide la délivrance → le stock est mis à jour automatiquement
4. Le DME du patient est notifié
5. La facturation est notifiée

## Alertes automatiques

- Stock d'Ibuprofène passe à 5 boîtes (seuil minimum : 10) → alerte
- Lot de Paracétamol périme dans 30 jours → alerte
