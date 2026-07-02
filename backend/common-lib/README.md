# Common Library (Bibliothèque Partagée)

## À quoi ça sert ?

C'est la **boîte à outils commune** de toute la plateforme HACNATION. Tous les autres services l'utilisent.

## Ce qu'elle contient

- **Les modèles de données partagés** : définitions communes d'un patient, d'un rendez-vous, d'une facture, etc.
- **Les événements Kafka** : messages standardisés que les services s'échangent entre eux (ex: "un patient vient d'être créé", "une facture est payée")
- **Les règles métier** : statuts possibles (rendez-vous confirmé, annulé, honoré...), rôles utilisateurs (médecin, pharmacien, caissier...)
- **La gestion des erreurs** : format unique des messages d'erreur renvoyés par l'API

## Pourquoi c'est important ?

Sans cette bibliothèque, chaque service devrait redéfinir les mêmes concepts. Grâce à elle, tous les services parlent le même langage.
