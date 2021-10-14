package fr.unice.Server.repository;

import fr.unice.Server.business.player.Player;
import model.Inventory;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PlayerRepository {

    private final Map<UUID, Player> playerMap = new HashMap<>();
    private final Map<UUID, UUID> playerGameMap = new HashMap<>(); // <PlayerId, GameId>

    public UUID createPlayer(String name) {
        UUID uuid = UUID.randomUUID();
        Player player = new Player(uuid, name, new Inventory());

        playerMap.put(uuid, player);
        return uuid;
    }

    public Optional<Player> getPlayer(UUID id) {
        return Optional.ofNullable(playerMap.get(id));
    }

    public Optional<UUID> getGameId(UUID playerID) {
        return Optional.ofNullable(playerGameMap.get(playerID));
    }

    public void setInGame(UUID playerId, UUID gameId) {
        playerGameMap.put(playerId, gameId);
    }

    public void removePlayersFromGame(UUID gameId) {
        Set<UUID> playersId = new HashSet<>();
        for (Map.Entry<UUID, UUID> entry : playerGameMap.entrySet()) {
            if (entry.getValue().equals(gameId)) {
                playersId.add(entry.getKey());
            }
        }

        for (UUID playerId : playersId) {
            playerGameMap.remove(playerId);
        }
    }
}
