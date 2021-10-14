# Etude du découpage Joueur ↔ Moteur

Au lancement du moteur (Game), la partie attendra que 3 joueurs rejoignent avant de se lancer.<br>
Cela se fera dans un système "d'inscription".

Il y aura 2 gros modules, ainsi qu'un module commun :

1) Le module Player contiendra les packages Player et Strategy.

2) Le module Game contiendra quant à lui le Moteur du jeu ainsi que le Board.<br>
   Pour le moment, Game garde l'inventaire du jeu, dans l'attente de savoir si l'inventaire doit se trouver dans un autre micro-service.

3) Le module commun, qui se trouvera entre les deux, sera donc le squelette de la partie.
Il contiendra les DTO suivants :
	- Les cards / wonders
	- Les ages
	- Les inventories
	- Les trades / exchanges

La communication se fera dans les deux sens.


### Façade n°1 : Player --> Game
La communication du Player --> Game se fera via REST :<br>
- Lorsqu'il souhaite s'inscrire dans une partie<br>
- Lorsqu'il doit jouer son tour<br>


### Façade n°2 : Game --> Player
Nous proposons de faire la communication du Game --> Player par envois évènementiels.<br>
Lorsque le Player rentrera dans la partie, il "s'abonnera" à celle-ci.<br>
Ainsi, le jeu publiera les évènements de la partie aux Players (abonnés).<br>
Ces évènements seront :
- Lorsqu'un Player devra jouer son tour
- Au lancement de la partie
- A la fin de la partie


Nos façades contiendront les méthodes permettant de faire les actions précédemment listées.