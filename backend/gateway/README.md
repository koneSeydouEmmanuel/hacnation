# API Gateway (Porte d'Entrée)

## À quoi ça sert ?

C'est le **guichet unique** de toute la plateforme. Toutes les applications (mobile, web) passent par ici pour accéder aux services.

## Ce qu'il fait

- **Authentification** : vérifie que l'utilisateur est bien qui il prétend être (via Keycloak)
- **Autorisation** : vérifie que l'utilisateur a le droit d'accéder à ce qu'il demande (ex: un patient ne peut pas voir la caisse)
- **Routage** : dirige chaque requête vers le bon service (patients → service Patient, rendez-vous → service RDV...)
- **Protection** : limite le nombre de requêtes pour éviter les abus

## Exemple concret

Quand un médecin ouvre l'application et consulte un dossier patient :
1. Le Gateway vérifie son identité (login/mot de passe)
2. Le Gateway vérifie qu'il a le rôle MÉDECIN
3. Le Gateway transmet sa demande au service Dossier Médical
4. La réponse revient par le même chemin

C'est le **seul point d'entrée** visible depuis l'extérieur.
