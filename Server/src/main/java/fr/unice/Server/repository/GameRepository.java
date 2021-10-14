package fr.unice.Server.repository;

import fr.unice.Server.business.game.Game;
import fr.unice.Server.business.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class GameRepository {

    private final Logger logger = LoggerFactory.getLogger(GameRepository.class);

    private final Map<UUID, Game> gameMap = new HashMap<>();
    private final Map<UUID, Integer> nbPlayersInGame = new HashMap<>();

    @Value("${game.maxQueueSize}")
    private int MAX_QUEUE_SIZE;

    public Optional<Game> getGameById(UUID id) {
        return Optional.ofNullable(gameMap.get(id));
    }

    public UUID createGame() {
        UUID id = UUID.randomUUID();
        Game game = new Game(id);
        gameMap.put(id, game);
        nbPlayersInGame.put(id, 0);

        logger.info("Game created (gameId: {})", id);
        return id;
    }

    public UUID addPlayerToAGame(Player player) {
        Optional<Map.Entry<UUID, Integer>> optionalGame = nbPlayersInGame.entrySet().stream().filter(
                entry -> entry.getValue() < MAX_QUEUE_SIZE && !gameMap.get(entry.getKey()).isGameStarded()
        ).findFirst();

        UUID gameId;
        if (optionalGame.isPresent()) {
            gameId = optionalGame.get().getKey();
        } else {
            gameId = createGame();
        }
        gameMap.get(gameId).addPlayer(player);
        nbPlayersInGame.put(gameId, nbPlayersInGame.get(gameId) + 1);

        logger.info("A player joined a game (playerName: {}, gameId: {})", player.getName(), gameId);
        return gameId;
    }

    public int getNbOfPlayersInGame(UUID id) {
        return nbPlayersInGame.get(id);
    }

    public void removeGame(UUID id) {
        gameMap.remove(id);
        nbPlayersInGame.remove(id);
        logger.info("Game removed (gameId: {})", id);
    }
}
