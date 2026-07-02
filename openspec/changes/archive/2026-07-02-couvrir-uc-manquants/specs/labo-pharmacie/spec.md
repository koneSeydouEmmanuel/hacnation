## ADDED Requirements

### Requirement: Televersement de fichiers de resultats
Le laborantin SHALL pouvoir joindre des fichiers (images medicales, PDF) aux resultats d'examens via un endpoint multipart.

#### Scenario: Upload d'une image medicale reussi
- **WHEN** le laborantin envoie POST /api/labo/demandes/{id}/fichiers avec un fichier JPG/PNG/DICOM/PDF
- **THEN** le systeme stocke le fichier, ajoute la reference a la demande, et retourne 201 avec l'URL du fichier

#### Scenario: Rejet d'un format non accepte
- **WHEN** le laborantin tente d'uploader un fichier .exe ou .zip
- **THEN** le systeme retourne 400 "Format de fichier non accepte. Formats autorises : JPG, PNG, DICOM, PDF"

#### Scenario: Depassement de la taille maximale
- **WHEN** le laborantin tente d'uploader un fichier de plus de 10 Mo
- **THEN** le systeme retourne 413 "Fichier trop volumineux. Taille maximale : 10 Mo"

#### Scenario: Plusieurs fichiers pour une meme demande
- **WHEN** le laborantin envoie plusieurs fichiers successivement pour la meme demande
- **THEN** chaque fichier est ajoute a la liste des fichiers de la demande, sans ecraser les precedents

### Requirement: Visibilite restreinte des resultats d'examens
Les resultats d'examens SHALL etre accessibles uniquement au medecin prescripteur et au patient concerne.

#### Scenario: Medecin prescripteur consulte les resultats
- **WHEN** le medecin (X-User-Id = medecinPrescripteurId) envoie GET /api/labo/demandes/{id}
- **THEN** le systeme retourne 200 avec les resultats complets

#### Scenario: Patient concerne consulte ses resultats
- **WHEN** le patient (X-User-Id = patientId) envoie GET /api/labo/demandes/{id}
- **THEN** le systeme retourne 200 avec les resultats

#### Scenario: Acces refuse a un tiers
- **WHEN** un autre medecin (non prescripteur) ou un laborantin tente GET /api/labo/demandes/{id}
- **THEN** le systeme retourne 403 "Acces non autorise a ces resultats"

#### Scenario: Administrateur peut tout voir
- **WHEN** un utilisateur avec le role ADMIN consulte GET /api/labo/demandes/{id}
- **THEN** le systeme retourne 200 (bypass de la restriction pour l'administration)
