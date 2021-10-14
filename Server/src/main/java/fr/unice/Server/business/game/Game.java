package fr.unice.Server.business.game;

import dto.ActionObject;
import fr.unice.Server.business.board.Deck;
import fr.unice.Server.business.board.DeckWonders;
import fr.unice.Server.business.player.Player;
import fr.unice.Server.business.player.PlayerList;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class Game {

    private static final Logger log = LoggerFactory.getLogger(Game.class);
    private static final int MAX_REQUEST_ATTEMPT = 3;

    private final UUID uuid;
    private boolean isGameStarded = false;
    private final Deck deck;
    private final DeckWonders deckWonders;
    private final Referee referee;
    private final PlayerActionListener playerActionListener;
    private final PlayerActionStorage playerActionStorage;
    private final PlayerActionExecutor playerActionExecutor;
    private final PlayerList players;
    private int turn;
    private Age currentAge;

    public Game(UUID uuid) {
        this.uuid = uuid;

        // Création de la partie
        referee = new Referee();
        playerActionListener = new PlayerActionListener();

        //Création de la liste des joueurs
        players = new PlayerList();
        playerActionStorage = new PlayerActionStorage(players, playerActionListener);
        playerActionExecutor = new PlayerActionExecutor(players);

        // Création du paquet de carte
        deck = new Deck();
        deck.initialize();

        // Création de la liste des merveilles
        deckWonders = new DeckWonders();
        deckWonders.initialize();

    }

    public boolean isGameStarded() {
        return this.isGameStarded;
    }

    public void storePlayerAction(UUID playerId, PlayerAction playerAction) throws Exception {
        Player player = players.getByUUID(playerId);
        Inventory leftInventory = players.leftPlayerOf(player).getInventory();
        Inventory rightInventory = players.rightPlayerOf(player).getInventory();
        boolean playable = referee.isChoicePlayable(playerAction, player.getInventory(), leftInventory, rightInventory);
        log.info("Le joueur '{}' choisi d'exécuter l'action {} sur la carte '{}', l'arbitre {}", player.getName(), playerAction.getChoice(), playerAction.getCard().getName(), playable ? "accepte" : "refuse");
        if (!playable) {
            throw new Exception("Unplayable action");
        }

        this.playerActionStorage.addCurrentPlayerAction(playerId, playerAction);
    }


    public void addPlayer(Player player) {
        if (isGameStarded) {
            throw new IllegalStateException("The game has already started.");
        }

        players.add(player);
        player.setCurrentGameId(this.uuid);
    }

    public PlayerList getPlayers() {
        return players;
    }

    /**
     * Appelé par les services externes pour regénérer l'ActionObject envoyé aux players lorsqu'ils doivent joué.
     * @param playerId
     * @return
     */
    public ActionObject getActionObject(UUID playerId) {
        Player player = this.players.getByUUID(playerId);
        Inventory leftInventory = players.leftPlayerOf(player).getInventory();
        Inventory rightInventory = players.rightPlayerOf(player).getInventory();
        return new ActionObject(this.uuid, this.currentAge, this.turn, player.getInventory(), leftInventory, rightInventory);
    }

    /**
     * Game interroge Player en lui envoyant une copie de son inventaire
     * Avec ça Player décide d'exécuter une action et renvoie à Game un objet Choice qui contient l'action et la carte que le joueur aura choisie
     * Game récupère ce Choice et l'envoie à Referee
     * Game récupère la réponse de Referee, si c'est oui l'action est exécutée par Game, si c'est non on revient à l'étape 1 et on boucle
     */
    public void launchGame() throws Exception {
        isGameStarded = true;
        //Répetition de ce qui suit pour chaque Age

        deckWonders.wondersDistribution(players);

        for (Age age : Age.values()) {
            log.debug("====== AGE {} ======", age);
            currentAge = age;

            // Distribution des cartes
            deck.cardDistribution(age, players, deck);

            turn = 0;
            while (twoOrMoreCardsInHandLeft(players)) {
                turn++;
                log.debug("== TURN {} ==", turn);
                playerActionStorage.newTurn(turn);
                playerActionListener.reset();

                //Les joueurs choisissent le coup à jouer indépendamment du choix des autres joueurs
                int currentTry = 0;
                do {
                    ++currentTry;
                    // On demande à tous les joueurs qui n'ont pas joué de jouer.
                    for (Player player : playerActionStorage.getPlayersDidNotPlay()) {
                        Inventory leftInventory = players.leftPlayerOf(player).getInventory();
                        Inventory rightInventory = players.rightPlayerOf(player).getInventory();
                        PlayerAction playerAction = player.chooseAction(age, turn, leftInventory, rightInventory);

                        // Pour les joueurs déconnectés
                        if (playerAction != null) {
                            this.playerActionStorage.addCurrentPlayerAction(player.getPlayerId(), playerAction);
                        }
                    }
                    playerActionListener.waiting();
                } while (!playerActionStorage.allPlayersPlayed() && currentTry < MAX_REQUEST_ATTEMPT);

                // Déconnecter et remplacer les joueurs qui n'ont pas répondu par un RandomBot
                for (Player disconnectedPlayer : playerActionStorage.getPlayersDidNotPlay()) {
                    log.info("Le joueur '{}' est remplacé par un bot", disconnectedPlayer.getName());
                    disconnectedPlayer.disconnect();
                    Inventory leftInventory = players.leftPlayerOf(disconnectedPlayer).getInventory();
                    Inventory rightInventory = players.rightPlayerOf(disconnectedPlayer).getInventory();
                    playerActionStorage.addCurrentPlayerAction(disconnectedPlayer.getPlayerId(), disconnectedPlayer.chooseAction(age, turn, leftInventory, rightInventory));
                }

                playerActionExecutor.executePlayerActions(this.playerActionStorage.getCurrentPlayerActions());

                Rotate.giveCards(age.getSens(), players);
            }

            //Calcul des victoires militaire pour l'age actuel et ajout des points dans l'inventaire
            Map<AbstractPlayer, Integer> ptsMilitaire = PointsCalculator.calculMilitaryPoints(players, age);
            for (AbstractPlayer player : players) {
                player.getInventory().addQtyResource(Resources.MILITARY_VICTORY_POINT, ptsMilitaire.get(player));
                log.debug("Le joueur '{}' a {} points de victoire militaire au tour {}", player.getName(), player.getInventory().getQtyResource(Resources.MILITARY_VICTORY_POINT), age);
            }
        }
        defineWinner();
    }

    /**
     * Vérifie si toutes les mains des joueurs sont vide ou pas
     * return true si il y a au moins une main qui n'est pas vide
     * return false si toutes les mains sont vide
     */
    private boolean twoOrMoreCardsInHandLeft(List<? extends AbstractPlayer> players) {
        for (AbstractPlayer player : players) {
            if (player.getInventory().getCardsInHand().size() >= 2) {
                return true;
            }
        }
        return false;
    }

    /**
     * Trouve le gagnant, l'affiche et stocke les infos à envoyer au serveur
     */
    private void defineWinner() {
        //Affichage du gagnant et du nombre de points en fin de partie pour ce joueur
        List<AbstractPlayer> winners = referee.getWinner(players);
        String winnerNames = winners.stream().map(AbstractPlayer::getName).collect(Collectors.joining(" et "));
        if (winners.size() > 1) {
            log.info("Les joueurs {} sont a égalité", winnerNames);
        } else {
            log.info("{} gagne la partie", winnerNames);
        }

        for (Player player : this.players) {
            player.showWinner(winnerNames);
        }
    }
}
