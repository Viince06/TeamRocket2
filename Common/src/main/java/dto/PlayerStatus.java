package dto;

import java.util.UUID;

public class PlayerStatus {
    private String name;
    private UUID playerId;
    private boolean isInGame;
    private UUID gameId;

    public PlayerStatus() {
    }

    public PlayerStatus(String name, UUID playerId, boolean isInGame, UUID gameId) {
        this.name = name;
        this.playerId = playerId;
        this.isInGame = isInGame;
        this.gameId = gameId;
    }

    public String getName() {
        return this.name;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public boolean isInGame() {
        return this.isInGame;
    }

    public UUID getGameId() {
        return this.gameId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public void setInGame(boolean isInGame) {
        this.isInGame = isInGame;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }
}
