package fr.unice.Server.service;

import dto.ActionObject;
import fr.unice.Server.business.game.Game;
import fr.unice.Server.business.player.ConnectedPlayer;
import fr.unice.Server.business.player.Player;
import fr.unice.Server.repository.GameRepository;
import fr.unice.Server.repository.PlayerRepository;
import model.PlayerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Optional;
import java.util.UUID;

@Service
public class GameService {

    private final Logger logger = LoggerFactory.getLogger(GameService.class);

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameLauncher gameLauncher;

    @Value("${game.maxQueueSize}")
    private int MAX_QUEUE_SIZE;

    public UUID userJoin(UUID playerId, SseEmitter emitter) {
        Player player = playerService.getPlayerOrThrow(playerId);

        Optional<UUID> currentGameId = this.playerRepository.getGameId(playerId);
        UUID gameId = currentGameId.orElseGet(() -> {
            UUID newGameId = this.gameRepository.addPlayerToAGame(player);
            player.resetInventory();
            this.playerRepository.setInGame(playerId, newGameId);
            return newGameId;
        });

        player.connect(emitter);

        if (gameRepository.getNbOfPlayersInGame(gameId) == MAX_QUEUE_SIZE) {
            logger.info("Pending queue full, a game will be launched (id: {})", gameId);
            gameLauncher.launchGame(gameId);
        }

        return gameId;
    }

    public void userPlay(UUID gameId, UUID playerId, PlayerAction playerAction) {
        Game game = gameRepository.getGameById(gameId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "This game was not found in our game list.")
        );

        try {
            game.storePlayerAction(playerId, playerAction);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The referee refused this action");
        }
    }

    public void gameFinished(UUID gameId) {
        gameRepository.removeGame(gameId);
        playerRepository.removePlayersFromGame(gameId);
    }

    public ActionObject recoverCurrentActionObject(UUID gameId, UUID playerId) {
        Game game = gameRepository.getGameById(gameId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "This game was not found in our game list.")
        );

        return game.getActionObject(playerId);
    }
}
