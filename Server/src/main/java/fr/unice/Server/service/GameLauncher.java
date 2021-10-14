package fr.unice.Server.service;

import fr.unice.Server.business.game.Game;
import fr.unice.Server.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GameLauncher {

    private final Logger logger = LoggerFactory.getLogger(GameLauncher.class);

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private StatisticsService statisticsService;

    @Async
    public void launchGame(UUID gameId) {
        try {
            Game game = gameRepository.getGameById(gameId).orElseThrow();
            game.launchGame();
            statisticsService.addStat(game.getPlayers());
            statisticsService.sendStat(gameId);
        } catch (Exception e) {
            logger.error("An error occurred during a game (gameId: " + gameId + ")", e);
        } finally {
            this.gameService.gameFinished(gameId);
        }
    }
}
