package fr.unice.Server.business.player;

import model.AbstractPlayer;
import model.Age;
import model.Inventory;
import model.PlayerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

public class Player extends AbstractPlayer {

    protected static final Logger logger = LoggerFactory.getLogger(Player.class);

    private final UUID playerId;
    private UUID currentGameId;
    private IPlayerState playerState = new RandomBot(this);

    public Player(UUID playerId, String name, Inventory inventory) {
        super(name, inventory);
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public void setCurrentGameId(UUID currentGameId) {
        this.currentGameId = currentGameId;
    }

    public UUID getCurrentGameId() {
        return currentGameId;
    }

    protected void setPlayerState(IPlayerState playerState) {
        this.playerState = playerState;
    }

    public void connect(SseEmitter emitter) {
        this.playerState.connected(emitter);
    }

    public void disconnect() {
        this.playerState.disconnected();
    }

    @Override
    public PlayerAction chooseAction(Age age, int turn, Inventory inventoryLeft, Inventory inventoryRight) {
        return this.playerState.chooseAction(age, turn, inventoryLeft, inventoryRight);
    }

    public void showWinner(String winnerName) {
        this.playerState.announceWinner(winnerName);
    }

    @Override
    public String getName() {
        if (this.playerState instanceof RandomBot) {
            return this.name + " (Bot)";
        } else {
            return super.getName();
        }
    }
}
