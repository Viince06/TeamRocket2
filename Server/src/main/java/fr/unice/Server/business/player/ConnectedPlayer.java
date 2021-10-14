package fr.unice.Server.business.player;

import dto.ActionObject;
import dto.EventId;
import model.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectedPlayer implements IPlayerState {

    private final Player player;
    private SseEmitter emitter;

    public ConnectedPlayer(Player player, SseEmitter emitter) {
        this.player = player;
        this.emitter = emitter;
    }

    public void setEmitter(SseEmitter emitter) {
        if (this.emitter != null) {
            this.emitter.complete();
        }
        this.emitter = emitter;
        handleError();
    }

    private void handleError() {
        this.emitter.onTimeout(() -> {
            Player.logger.info("ConnectedPlayer (id: " + this.player.getPlayerId() + ") has been disconnected. Reason : Timeout");
            disconnected();
        });
        this.emitter.onCompletion(() -> {
            Player.logger.info("ConnectedPlayer (id: " + this.player.getPlayerId() + ") has been disconnected. Reason : Complete");
            disconnected();
        });
        this.emitter.onError(throwable -> {
            Player.logger.warn("ConnectedPlayer (id: " + this.player.getPlayerId() + ") has been disconnected. Reason : " + throwable.getMessage());
            this.disconnected();
        });
    }

    @Override
    public PlayerAction chooseAction(Age age, int turn, Inventory inventoryLeft, Inventory inventoryRight) {
        UUID gameId = this.player.getCurrentGameId();
        ActionObject actionObject = new ActionObject(gameId, age, turn, this.player.getInventory(), inventoryLeft, inventoryRight);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                SseEmitter.SseEventBuilder event = SseEmitter.event()
                        .id(gameId + "-" + age + "-" + turn + "-" + this.player.getName())
                        .data(actionObject)
                        .name(EventId.PLAY.name());
                emitter.send(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return null;
    }

    @Override
    public void announceWinner(String winnerName) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                SseEmitter.SseEventBuilder event = SseEmitter.event()
                        .id("winner")
                        .data(winnerName)
                        .name(EventId.WINNER.name());
                emitter.send(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void connected(SseEmitter emitter) {
        this.setEmitter(emitter);
        handleError();
    }

    @Override
    public void disconnected() {
        try {
            this.emitter.complete();
            this.emitter = null;
        } catch (RuntimeException ignored) {
        }
        this.player.setPlayerState(new RandomBot(this.player));
    }
}
