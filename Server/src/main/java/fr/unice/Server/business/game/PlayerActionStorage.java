package fr.unice.Server.business.game;

import fr.unice.Server.business.player.Player;
import fr.unice.Server.business.player.PlayerList;
import model.PlayerAction;

import java.util.*;

public class PlayerActionStorage {

    private final PlayerList players;
    private final PlayerActionListener playerActionListener;
    private int currentTurn = 0;
    private volatile Map<UUID, PlayerAction> currentPlayerActions;
    private volatile Map<Integer, Map<UUID, String>> history = new HashMap<>();

    public PlayerActionStorage(PlayerList players, PlayerActionListener playerActionListener) {
        this.players = players;
        this.playerActionListener = playerActionListener;
        this.currentPlayerActions = new HashMap<>();
    }

    public Map<UUID, PlayerAction> getCurrentPlayerActions() {
        return currentPlayerActions;
    }

    public void newTurn(int turn) {
        if (currentTurn == 0) {
            this.currentTurn = turn;
            return;
        }

        Map<UUID, String> playerActions = new HashMap<>();
        for (Map.Entry<UUID, PlayerAction> entry : currentPlayerActions.entrySet()) {
            playerActions.put(entry.getKey(), entry.getValue().toString());
        }
        history.put(currentTurn, playerActions);

        currentPlayerActions.clear();
        this.currentTurn = turn;
    }

    public void addCurrentPlayerAction(UUID playerId, PlayerAction playerAction) throws Exception {
        if (this.currentPlayerActions.containsKey(playerId)) {
            throw new Exception("This player has already played.");
        }

        this.currentPlayerActions.put(playerId, playerAction);
        if (allPlayersPlayed()) {
            this.playerActionListener.notifying();
        }
    }

    public synchronized boolean allPlayersPlayed() {
        return players.size() == currentPlayerActions.size();
    }

    public List<Player> getPlayersDidNotPlay() {
        List<Player> list = new ArrayList<>();
        if (allPlayersPlayed())
            return list;

        for (Player player : players) {
            if (!this.currentPlayerActions.containsKey(player.getPlayerId())) {
                list.add(player);
            }
        }

        return list;
    }
}
