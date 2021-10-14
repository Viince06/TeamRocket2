# Release 03

<br>  

### Pour cette troisième itération :

- Tous les modules sont dans des containers Docker et utilisent Spring.
- Travis lance le docker-compose avec l'option scale qui permet de lancer 3 Players qui peuvent rejoindre et lancer une même partie.
- Les modules communiquent entre eux :
    - Dès qu'un Player est lancé il demande au serveur à rejoindre une partie.
    - Quand 3 joueurs ont rejoints, une partie se lance. Le jeu demande alors aux joueurs de jouer à chaque tour.
    - Lorsque la partie est terminée, le Server envoi le nom du gagnant à tous les joueurs ainsi que les statistiques de la partie au serveur de Stats.
- Nous n'avons pas de test d'intégration à proprement parler, mais comme les joueurs lancent la partie lorsqu'ils sont lancés, cela nous sert actuellement de test dans la pipeline de Travis.
