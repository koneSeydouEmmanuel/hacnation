## ADDED Requirements

### Requirement: Generation automatique de facture sur validation d'examen
Une facture SHALL etre generee ou mise a jour automatiquement lorsqu'un resultat d'examen est valide par le laboratoire.

#### Scenario: Resultat valide cree une nouvelle facture
- **WHEN** un evenement sic.laboratoire avec statut VALIDE est consomme et qu'aucune facture n'existe pour ce patient/consultation
- **THEN** le systeme cree une nouvelle facture avec une ligne "Examen {typeAnalyse}" et retourne le statut EN_ATTENTE

#### Scenario: Resultat valide ajoute une ligne a une facture existante
- **WHEN** un evenement sic.laboratoire avec statut VALIDE est consomme et qu'une facture EN_ATTENTE existe deja pour ce patient/consultation
- **THEN** le systeme ajoute une ligne "Examen {typeAnalyse}" a la facture existante et recalcule le total

### Requirement: Generation automatique de facture sur delivrance de medicament
Une facture SHALL etre generee ou mise a jour automatiquement lorsqu'un medicament est delivre par la pharmacie.

#### Scenario: Medicament delivre cree une nouvelle facture
- **WHEN** un evenement sic.pharmacie avec statut DELIVREE est consomme et qu'aucune facture n'existe
- **THEN** le systeme cree une nouvelle facture avec une ligne "Medicament {nom} x{quantite}" et retourne le statut EN_ATTENTE

#### Scenario: Medicament delivre ajoute a une facture existante
- **WHEN** un evenement sic.pharmacie avec statut DELIVREE est consomme et qu'une facture EN_ATTENTE existe deja
- **THEN** le systeme ajoute la ligne medicament a la facture existante et recalcule le total

### Requirement: Immutabilite du paiement valide
Un paiement au statut PAYEE SHALL etre immuable : aucune modification, annulation ou suppression n'est autorisee apres validation.

#### Scenario: Tentative de suppression d'un paiement valide bloquee
- **WHEN** un utilisateur tente DELETE /api/caisse/{id} sur un paiement PAYEE
- **THEN** le systeme retourne 409 "Ce paiement est valide et ne peut plus etre modifie"

#### Scenario: Tentative de modification d'un paiement valide bloquee
- **WHEN** un utilisateur tente PUT /api/caisse/{id} sur un paiement PAYEE
- **THEN** le systeme retourne 409 "Ce paiement est valide et ne peut plus etre modifie"

#### Scenario: Aucun endpoint d'annulation pour un paiement valide
- **WHEN** un paiement est au statut PAYEE
- **THEN** le systeme ne propose aucun mecanisme d'annulation ou de remboursement

#### Scenario: Paiement non valide reste modifiable
- **WHEN** un paiement est au statut EN_ATTENTE ou PARTIELLE
- **THEN** les operations de modification restent autorisees normalement
