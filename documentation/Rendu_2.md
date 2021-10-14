# Release 02

<br>  

### Cette seconde itération met en place :

#### Travis :
• Créé une image docker du serveur de stat  
• Execute le docker-compose (qui lance le serveur de stat)  
• Doit lancer le serveur de jeu mais échoue (on n'arrive pas à le lancer en background)  
• Lance une requête au serveur de jeu pour vérifier la connection entre lui et les Stats (mais échoue puisque serveur pas lancé)  
• Lance une requête au serveur de jeu pour l'éteindre (mais échoue puisque serveur pas lancé)  
• Stop le serveur de stat en fermant les containers docker lancé.

#### Les communications :
• Player -> Game : Un joueur peut rejoindre une file d'attente pour une partie, mais Player n'étant pas encore un serveur, il faut nous même exécuter l'appel.
• Game -> Stats : Les parties n'étant pas fonctionnel, Game n'appellera jamais la route /stats/add. Par contre on a créé une route /ping-stat dans Game qui lui demande de pinger Stats, donc la communication entre eux marche.
• Game -> Player : Player n'étant pas encore un server, aucune communication n'existe dans ce sens.
#### Les routes :
• Game :  ○ /join permet à un joueur de joindre une partie, il prend en RequestParam un 'name'  ○ /ping-stat demande à Game de requêter / sur le serveur de stat et renvoi sa réponse  ○ /shutdown éteint le serveur
• Stat :  ○ / ping le serveur et retourne un "Hello World !"  ○ /stats/add créé un fichier csv avec les stats envoyés en body (ancienne requête existante au précédent semestre)
<br>
#### Futur chemin REST:
• Transformer Player en server, il hébergera une route Play qui sera interrogée par le moteur de jeu quand le joueur devra jouer.  
• Player hébergera une autre route Result, Game interrogera cette route pour signaler que la partie est finie et donner le nom du gagnant.